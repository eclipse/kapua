# How to Execute Kapua Tests

There are a couple of ways to run tests, either pure unit tests, component tests or integration tests.
Integration tests can be run:

- using dockerized servers
- using embedded servers

## With Dockerized Kapua
With fabric8 plugin usage tests can now be run without explicitly running dockerized environment.
Docker containers providing Kapua infrastructure are started with maven itself.
Nevertheless, in order to run these integration tests, you first have to build project from root with command:

    mvn clean install -DskipTests -Pdocker

and afterwards switch to qa folder and run the following command:

    mvn test -PI9nTests

This will run integration tests only, those are tests written in Gherkin and being annotated with
``@integration``

If tests fail and Docker containers are still running, use this command:

    mvn docker:stop -PI9nTests

Example response with time:

    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 53:15 min
    [INFO] Finished at: 2020-11-05T22:10:15+01:00
    [INFO] Final Memory: 177M/728M
    [INFO] ------------------------------------------------------------------------    

## Using Embedded Servers
By default, tests are run with embedded servers providing Kapua infrastructure services, such as
database, event-broker, message broker, ElasticSearch.

To run those use default profile and run:

    mvn clean install
    or
    mvn test

Those two commands will use following defaults:

    cucumber.options="--tags ~@rest"
    groups='!org.eclipse.kapua.qa.markers.Categories$junitTests'
    commons.settings.hotswap=true
    commons.db.schema.update=true
    commons.db.schema=kapuadb
    broker.host=localhost

Example response with time:

    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 31:14 min
    [INFO] Finished at: 2019-03-04T14:51:24+01:00
    [INFO] Final Memory: 244M/1761M
    [INFO] ------------------------------------------------------------------------

## Running Pure JUnit Tests

    mvn test -PjunitTests

Example response with time:

    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 05:26 min
    [INFO] Finished at: 2019-03-04T15:01:04+01:00
    [INFO] Final Memory: 133M/1250M
    [INFO] ------------------------------------------------------------------------
