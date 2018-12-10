# How to execute Kapua tests

This is document in preparation. It contains instructions how to prepare environment for testing
and how to execute unit, integration and functional tests. Test environment is either based on embedded servers or external setup based on Docker Compose.

## With dockerized Kapua
Start dockerized Kapua
    
      mvn -Dcucumber.options="--tags @integration" -Dgroups='!org.eclipse.kapua.test.junit.JUnitTests' -Dcommons.settings.hotswap=true -Dorg.eclipse.kapua.qa.noEmbeddedServers=true -Dcommons.db.jdbcConnectionUrlResolver=DEFAULT -Dcommons.db.schema.update=true -Dcommons.db.connection.host=localhost -Dcommons.db.connection.port=3306 -Dcommons.db.schema=kapuadb -Dcommons.db.connection.scheme=jdbc:h2:tcp -Dbroker.host=localhost verify  

Example response with time:

    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 22:06 min
    [INFO] Finished at: 2018-12-06T10:43:19+01:00
    [INFO] Final Memory: 187M/1726M
    [INFO] ------------------------------------------------------------------------

##Without dockerized Kapua

    mvn -fae -Dcucumber.options="--tags ~@rest" -Dgroups='!org.eclipse.kapua.test.junit.JUnitTests' -Dcommons.settings.hotswap=true -Dcommons.db.schema.update=true -Dcommons.db.schema=kapuadb -Dbroker.host=localhost verify


Example response with time:

    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 34:53 min
    [INFO] Finished at: 2018-12-04T11:41:10+01:00
    [INFO] Final Memory: 187M/1827M
    [INFO] ------------------------------------------------------------------------
