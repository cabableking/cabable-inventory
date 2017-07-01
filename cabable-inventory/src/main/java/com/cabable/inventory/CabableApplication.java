package com.cabable.inventory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.apifest.client.OAuthClient;
import com.apifest.client.OAuthScope;
import com.cabable.inventory.auth.CabableAuthorizer;
import com.cabable.inventory.auth.OAuthAuthenticator;
import com.cabable.inventory.cli.RenderCommand;
import com.cabable.inventory.core.Car;
import com.cabable.inventory.core.Device;
import com.cabable.inventory.core.Driver;
import com.cabable.inventory.core.Operator;
import com.cabable.inventory.core.Plan;
import com.cabable.inventory.core.RateCard;
import com.cabable.inventory.core.Relationship;
import com.cabable.inventory.core.Role;
import com.cabable.inventory.core.User;
import com.cabable.inventory.db.CarDAO;
import com.cabable.inventory.db.DeviceDAO;
import com.cabable.inventory.db.DriverDAO;
import com.cabable.inventory.db.OAuthRedisDAO;
import com.cabable.inventory.db.OperatorDAO;
import com.cabable.inventory.db.PlanDAO;
import com.cabable.inventory.db.RateCardDAO;
import com.cabable.inventory.db.RelationshipDAO;
import com.cabable.inventory.db.UserDAO;
import com.cabable.inventory.filter.DateRequiredFeature;
import com.cabable.inventory.resources.CarResource;
import com.cabable.inventory.resources.DeviceResource;
import com.cabable.inventory.resources.DriverResource;
import com.cabable.inventory.resources.OAuth2Resource;
import com.cabable.inventory.resources.OperatorResource;
import com.cabable.inventory.resources.RateCardResource;
import com.cabable.inventory.resources.RelationshipResource;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

public class CabableApplication extends Application<CabableConfiguration> {

	Logger LOGGER = Logger.getLogger(CabableApplication.class);

	public static void main(String[] args) throws Exception {
		new CabableApplication().run(args);
	}

	private final HibernateBundle<CabableConfiguration> hibernateBundle =
			new HibernateBundle<CabableConfiguration>(Car.class, Operator.class, Device.class, Driver.class, User.class, Relationship.class, Plan.class, RateCard.class, String.class) {
		@Override
		public DataSourceFactory getDataSourceFactory(CabableConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	@Override
	public String getName() {
		return "Cabable Inventory";
	}

	@Override
	public void initialize(Bootstrap<CabableConfiguration> bootstrap) {


		// Enable variable substitution with environment variables
		bootstrap.setConfigurationSourceProvider(
				new SubstitutingSourceProvider(
						bootstrap.getConfigurationSourceProvider(),
						new EnvironmentVariableSubstitutor(false)
						)
				);

		bootstrap.addCommand(new RenderCommand());
		bootstrap.addBundle(new AssetsBundle());
		bootstrap.addBundle(new MigrationsBundle<CabableConfiguration>() {
			@Override
			public DataSourceFactory getDataSourceFactory(CabableConfiguration configuration) {
				return configuration.getDataSourceFactory();
			}
		});
		bootstrap.addBundle(hibernateBundle);
		bootstrap.addBundle(new ViewBundle<CabableConfiguration>() {
			@Override
			public Map<String, Map<String, String>> getViewConfiguration(CabableConfiguration configuration) {
				return configuration.getViewRendererConfiguration();
			}
		});
		bootstrap.addBundle(new MultiPartBundle());
	}

	@Override
	public void run(CabableConfiguration configuration, Environment environment) {

		this.setCacheHeaders(environment, "/*", 86400);

		// Enable CORS headers
		final FilterRegistration.Dynamic cors =
				environment.servlets().addFilter("CORS", CrossOriginFilter.class);

		// Configure CORS parameters
		cors.setInitParameter("allowedOrigins", "*");
		cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
		cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

		// Add URL mapping
		cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

		//initialize redis 
		final OAuthRedisDAO redisDAO = new OAuthRedisDAO(configuration.getRedis());
		if(!redisDAO.initializeScopes()){
			throw new WebApplicationException("Not able to initialize scopes on redis");
		}

		//initialize other DAOs
		final CarDAO dao = new CarDAO(hibernateBundle.getSessionFactory());
		final OperatorDAO opDao = new OperatorDAO(hibernateBundle.getSessionFactory());
		final DeviceDAO devDao = new DeviceDAO(hibernateBundle.getSessionFactory());
		final DriverDAO driverDao = new DriverDAO(hibernateBundle.getSessionFactory());
		final UserDAO userDao = new UserDAO(hibernateBundle.getSessionFactory());
		final RelationshipDAO relDao = new RelationshipDAO(hibernateBundle.getSessionFactory());
		final RateCardDAO rcDao = new RateCardDAO(hibernateBundle.getSessionFactory());
		final PlanDAO planDao = new PlanDAO(hibernateBundle.getSessionFactory());


		//initialize oauthclient
		OAuthClient oauthClient = new UnitOfWorkAwareProxyFactory(hibernateBundle)
				.create(OAuthClient.class, new Class[]{String.class, UserDAO.class}, new Object[]{configuration.getOauthURL(), userDao});

		//register scopes
		for(Role role: Role.values()){
			OAuthScope scope = new OAuthScope();
			scope.setScope(role.toString());
			scope.setCc_expires_in(Integer.toString(role.getCc_expires_in()));
			scope.setPass_expires_in(Integer.toString(role.getPass_expires_in()));
			scope.setRefresh_expires_in(Integer.toString(role.getRefresh_expires_in()));
			Response response = oauthClient.registerScope(scope);
			if(response.getStatus()!=200){
				LOGGER.info("Scope already exists, not added");
			}else{
				LOGGER.info("Scope added");
			}
		}

		environment.jersey().register(DateRequiredFeature.class);

		environment.jersey().register(new CabableAuthDynamicFeature(
				new OAuthCredentialAuthFilter.Builder<User>()
				.setAuthenticator(new OAuthAuthenticator(oauthClient))
				.setAuthorizer(new CabableAuthorizer())
				.setPrefix("Bearer")
				.buildAuthFilter()));
		environment.jersey().register(RolesAllowedDynamicFeature.class);
		//If you want to use @Auth to inject a custom Principal type into your resource
		environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

		//init resources
		environment.jersey().register(new OAuth2Resource(oauthClient,userDao));
		environment.jersey().register(new CarResource(dao));
		environment.jersey().register(new DriverResource(driverDao));
		environment.jersey().register(new DeviceResource(devDao));
		environment.jersey().register(new OperatorResource(opDao, userDao, oauthClient));
		environment.jersey().register(new RelationshipResource(relDao));
		environment.jersey().register(new RateCardResource(rcDao, planDao));

	}

 protected void setCacheHeaders(Environment environment, String urlPattern, int seconds) {
		FilterRegistration.Dynamic filter = environment.servlets().addFilter(
				"cacheControlFilter",
				new Filter() {
					@Override
					public void init(FilterConfig filterConfig) throws ServletException {

					}

					@Override
					public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

						HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
						HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

						String[] cacheFileTypes = {"js","css","png","jpg","gif","svg", "html"};
						String filetypeRequested = FilenameUtils.getExtension(httpServletRequest.getRequestURL().toString());

						if (httpServletRequest.getMethod() == "GET" && seconds > 0 && Arrays.asList(cacheFileTypes).contains(filetypeRequested)) {
							httpServletResponse.setHeader("Cache-Control", "public, max-age=" + seconds);
							Calendar c = Calendar.getInstance();
							c.setTime(new Date());
							c.add(Calendar.SECOND, seconds);
							SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz", Locale.US);
							format.setTimeZone(TimeZone.getTimeZone("GMT"));
							httpServletResponse.setHeader("Expires", format.format(c.getTime()));
						} else {
							httpServletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, no-transform");
							httpServletResponse.setHeader("Expires", "0");
							httpServletResponse.setHeader("Pragma", "no-cache");
						}

						filterChain.doFilter(servletRequest, servletResponse);

					}

					@Override
					public void destroy() {

					}
				}
				);
		filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, urlPattern);
	}
}
