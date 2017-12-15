## Web Console

This is the old, GXT-based web console, if you are looking for the new, Patternfly web console
please see the branch `impl-consoleV2`.

### Running locally

It is possible to run the web console locally by starting the docker containers `kapua-sql`, `kapua-broker` and `kapua-elasticsearch` according to "[Running docker containers](../assembly/README.md#run)" first and then by running:


    mvn org.eclipse.jetty:jetty-maven-plugin:9.4.6.v20170531:run-exploded -nsu -Pdev -DuseTestScope=true -Djetty.daemon=false -Dcommons.db.connection.host=localhost

**Note:** Be sure that the port 8080 is open before starting the web console.
 