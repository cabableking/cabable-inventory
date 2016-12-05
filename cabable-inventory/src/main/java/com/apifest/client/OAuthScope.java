/*
 * Copyright 2013-2015, ApiFest project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.apifest.client;

import javax.validation.constraints.NotNull;

public class OAuthScope {

	@NotNull
    String scope;
	@NotNull
    String description="";
    String cc_expires_in = "900";
    String pass_expires_in = "900";
    String refresh_expires_in = "3600";

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCc_expires_in() {
        return cc_expires_in;
    }

    public void setCc_expires_in(String cc_expires_in) {
        this.cc_expires_in = cc_expires_in;
    }

    public String getPass_expires_in() {
        return pass_expires_in;
    }

    public void setPass_expires_in(String pass_expires_in) {
        this.pass_expires_in = pass_expires_in;
    }

	public String getRefresh_expires_in() {
		return refresh_expires_in;
	}

	public void setRefresh_expires_in(String refresh_expires_in) {
		this.refresh_expires_in = refresh_expires_in;
	}

}
