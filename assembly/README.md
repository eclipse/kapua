## Docker

The section describes how Eclipse Kapua docker images can be used.

### Build

    mvn -Pdocker,console install

If you want to speed up the build process you can ask Maven to ignore `-SNAPSHOT` updates
force it to use only locally present artifacts with the argument `-nsu`:

    mvn -Pdocker,console install -nsu

### Pushing

Pushing with default settings:

    mvn -Pdocker deploy

Pushing to a specific docker registry:

    mvn -Pdocker deploy -Ddocker.push.registry=registry.hub.docker.com

Pushing to a specific docker registry under a specific account:

    mvn -Pdocker deploy -Ddocker.push.registry=registry.hub.docker.com -Ddocker.account=eclipse

### Run

    docker run -td --name kapua-sql -p 8181:8181 -p 3306:3306 kapua/kapua-sql
    docker run -td --name kapua-elasticsearch -p 9200:9200 -p 9300:9300 elasticsearch:5.4.0 -Ecluster.name=kapua-datastore -Ediscovery.type=single-node -Etransport.host=_site_ -Etransport.ping_schedule=-1 -Etransport.tcp.connect_timeout=30s
    docker run -td --name kapua-broker --link kapua-sql:db --link kapua-elasticsearch:es --env commons.db.schema.update=true -p 1883:1883 -p 61614:61614 kapua/kapua-broker
    docker run -td --name kapua-console --link kapua-sql:db --link kapua-broker:broker --link kapua-elasticsearch:es --env commons.db.schema.update=true -p 8080:8080 kapua/kapua-console
    docker run -td --name kapua-api --link kapua-sql:db --link kapua-broker:broker --link kapua-elasticsearch:es --env commons.db.schema.update=true -p 8081:8080 kapua/kapua-api

### Access

Navigate your browser to http://localhost:8080 and log in using the following credentials:
`kapua-sys` : `kapua-password`

You can access the API using: http://localhost:8081

**Note**: If you are using Docker on Windows the hostname will most likely not be `localhost` but
the IP address of your docker instance.

### SSO testing

**Note:** This is only a setup for testing SSO support.

You can also start a Keycloak instance in addition:

    docker run -td --name sso -p 8082:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=secret jboss/keycloak

Starting the `kapua-console` image with the following command line instead:

    docker run -td --name kapua-console --link sso --link kapua-sql:db --link kapua-broker:broker --link kapua-elasticsearch:es -p 8080:8080 -e KEYCLOAK_URL=http://$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' sso):8080 -e KAPUA_URL=http://localhost:8080 kapua/kapua-console

You will also need to create a new realm named `kapua` in the Keycloak web UI and create a new client called `console`.
Assigning `http://localhost:8080/sso/callback` as a valid redirect URI.

### Tomcat images

It is also possible to use Tomcat as a web container. For this use the following run commands instead:

    docker run -td --name kapua-console --link kapua-sql:db --link kapua-broker:broker --link kapua-elasticsearch:es -p 8080:8080 kapua/kapua-console
    docker run -td --name kapua-api --link kapua-sql:db --link kapua-broker:broker --link kapua-elasticsearch:es -p 8081:8080 kapua/kapua-api

Please note that in this case you also have to append `/admin` and `/api` to the URL.

