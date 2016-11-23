## Docker

### Build

mvn -Pdocker 

### Run

docker run -td --name kapua-broker -p 1883:1883 kapua-broker

docker run -td --name kapua-console -p 8080:8080 kapua-console

docker run -td --name kapua-api -p 8081:8080 kapua-api