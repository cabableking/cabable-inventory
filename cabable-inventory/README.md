# Introduction

This is the cabable inventory API code. The apis are hosted on the amazon instance provided later. 

# Overview
Following are the APIs included. 
 auth:
login POST
refreshtoken POST
register POST
scopes POST
validatetoken GET
 car:
create POST
delete DELETE
get GET
getAll GET
update POST
 device:
create POST
delete DELETE
get GET
getAll GET
update POST
 driver:
create POST
delete DELETE
get GET
getAll GET
update POST
 operator:
create POST
delete DELETE
get GET
getAll GET
update POST
 ratecard:
create POST
delete DELETE
get GET
plannames: GET
plans GET
update POST
 relationship:
delete DELETE
get GET PUT
startonboarding PUT
update POST

# Running The Application

To test the example application run the following commands.

* To package the example run.

        mvn package

* To setup the h2 database run.

        java -jar target/<jar-name> db migrate <cabable.yml>

* To run the server run.

        java -jar target/<jar-name> server <cabable.yml>

