# How to execute Kapua tests

There are couple of ways to run tests, either pure unit tests, component tests or integration tests.
Integration tests can be run:

- using embedded servers
- using dockerized servers

## With dockerized Kapua
With fabric8 plugin usage tests can now be run without explicitly running dockerized environment.
Docker containers providing Kapua infrastructure are started with maven itself.
But to run these integration tests, you have to switch to qa folder and run following command

    mvn test -PI9nTests
    
This will run integration tests only, those are tests written in gherkin and being annotated with
``@integration``

If tests fail and dockers are still running use this command:

    mvn docker:stop -PI9nTests

Example response with time:

    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 18:11 min
    [INFO] Finished at: 2019-03-04T14:16:32+01:00
    [INFO] Final Memory: 48M/728M
    [INFO] ------------------------------------------------------------------------    

##Without dockerized Kapua
By default tests are run with embedded servers providing kapua infrastructure services, such as
database, event-broker, message broker, elaticsearch.

So to run those use default profile and run:

    mvn clean install
    or
    mvn test

Those two commands will use following defaults:

    cucumber.options="--tags ~@rest"
    groups='!org.eclipse.kapua.test.junit.JUnitTests'
    commons.settings.hotswap=true
    commons.db.schema.update=true
    commons.db.schema=kapuadb
    broker.host=localhost

Example response with time:

    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 31:14 min
    [INFO] Finished at: 2019-03-04T14:51:24+01:00
    [INFO] Final Memory: 244M/1761M
    [INFO] ------------------------------------------------------------------------

## Run pure junit tests

    mvn test -Dgroups='org.eclipse.kapua.test.junit.JUnitTests'

Example response with time:

    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 05:26 min
    [INFO] Finished at: 2019-03-04T15:01:04+01:00
    [INFO] Final Memory: 133M/1250M
    [INFO] ------------------------------------------------------------------------
