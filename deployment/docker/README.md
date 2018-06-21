# Kapua on Docker

To learn more on how to run Kapua using Docker, please consult [developer manual](https://github.com/eclipse/kapua/blob/develop/docs/developer-guide/en/running.md#docker).

The most recent version of the documentation can be viewed online at:

* http://download.eclipse.org/kapua/docs/develop/user-manual/en/
* http://download.eclipse.org/kapua/docs/develop/developer-guide/en/

## How to run Kapua on Docker.

You are required to have Docker installed and running.<br>
It is advisable to give to Docker at least 6GB of ram and 2 CPUs. 

### Running
Just simply run:
```bash
./docker-deploy.sh
```

### Accessing components
After deployment and startup of containers, they can be accessed at the following endpoints

| Application/Service | Endpoint       | User         | Password       | Others                            |
|---------------------|----------------|--------------|----------------|-----------------------------------|
| H2 SQL              | localhost:3306 | kapua        | kapua          | Schema name: `kapuadb`            |
| Elasticsearch       | localhost:9200 |              |                |                                   |
| Broker              | localhost:1883 | kapua-broker | kapua-password |                                   |
| Admin WEB Console   | localhost:8080 | kapua-sys    | kapua-password |                                   |
| REST API endpoint   | localhost:8081 | kapua-sys    | kapua-password | API KEY: `12345678kapua-password` |

### Checking
You can check Docker containers logs to check that everything has started and is running properly, by running:
```bash
docker-compose -f compose/docker-compose.yml logs -f
```

### Tear down
To stop and remove all containers, simply run:
```bash
./docker-undeploy.sh
```

### Advanced options

#### Setting the Kapua version
Other than the default deployment it is possible to run other versions of Kapua.

By default the `latest` version of Kapua will be brought up. 
You can change the version of Kapua by exporting the environment variable `IMAGE_VERSION`.

Example:
```bash
export IMAGE_VERSION=1.0.0
``` 
#### Building containers from scratch
If you want to build containers from the code, you'll need to build the whole Kapua Project.

From the project root directory, run:
```bash
mvn clean install -f external/pom.xml
mvn clean install -Pdocker
```

To build also the Admin Web Console container, which is excluded by default, add the `console` profile:
```bash
mvn clean install -f external/pom.xml
mvn clean install -Pconsole,docker
```

After the build has completed follow the steps from the [Running](#Running) section.
