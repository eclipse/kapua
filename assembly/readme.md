## Docker

### Build

    mvn -Pdocker

### Run

    docker run -td --name kapua-sql -p 8181:8181 -p 3306:3306 kapua/kapua-sql
    docker run -td --name kapua-broker --link kapua-sql:db -p 1883:1883 kapua/kapua-broker
    docker run -td --name kapua-console --link kapua-sql:db -p 8080:8080 kapua/kapua-console
    docker run -td --name kapua-api --link kapua-sql:db -p 8081:8080 kapua/kapua-api

### Access

Navigate your browser to http://localhost:8080/console and log in using the following credentials:
`kapua-sys` : `kapua-password`
