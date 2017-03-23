## Docker

The section describes how Eclipse Kapua docker images can be used.

### Build

    mvn -Pdocker

If you want to speed up the build process you can ask Maven to ignore `-SNAPSHOT` updates
force it to use only locally present artifacts with the argument `-nsu`:

    mvn -Pdocker -nsu

### Pushing

Pushing with default settings:

    mvn -Pdocker-push

Pushing to a specific docker registry:

    mvn -Pdocker-push -Ddocker.push.registry=registry.hub.docker.com

Pushing to a specific docker registry under a specific account:

    mvn -Pdocker-push -Ddocker.push.registry=registry.hub.docker.com -Ddocker.account=eclipse

### Run

    docker run -td --name kapua-sql -p 8181:8181 -p 3306:3306 kapua/kapua-sql
    docker run -td --name kapua-elasticsearch -p 9200:9200 -p 9300:9300 elasticsearch:2.4 -Dcluster.name=kapua-datastore -Ddiscovery.zen.minimum_master_nodes=1
    docker run -td --name kapua-broker --link kapua-sql:db --link kapua-elasticsearch:es -p 1883:1883 -p 61614:61614 kapua/kapua-broker
    docker run -td --name kapua-console --link kapua-sql:db --link kapua-broker:broker -p 8080:8080 kapua/kapua-console-jetty
    docker run -td --name kapua-api --link kapua-sql:db --link kapua-broker:broker -p 8081:8080 kapua/kapua-api-jetty

### Access

Navigate your browser to http://localhost:8080 and log in using the following credentials:
`kapua-sys` : `kapua-password`

You can access the API using: http://localhost:8081

### Tomcat images

It is also possible to use Tomcat as a web container. For this use the following run commands instead:

    docker run -td --name kapua-console --link kapua-sql:db --link kapua-broker:broker -p 8080:8080 kapua/kapua-console
    docker run -td --name kapua-api --link kapua-sql:db --link kapua-broker:broker -p 8081:8080 kapua/kapua-api

Please note that in this case you also have to append `/console` and `/api` to the URL.


