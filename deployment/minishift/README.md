# Kapua on Minishift

To learn more on how to run Kapua in Minishift, please consult [developer manual](https://github.com/eclipse/kapua/blob/develop/docs/developer-guide/en/running.md#minishift).

The most recent version of the documentation can be viewed online at:

* http://download.eclipse.org/kapua/docs/develop/user-manual/en/
* http://download.eclipse.org/kapua/docs/develop/developer-guide/en/

## How to run Kapua on MiniShift.

You are required to have _Docker_, _Minishift_ and _VirtualBox_ installed.<br>
VirtualBox will be used as VM driver for Minishift.

### Fetching Docker images
You'll need to have the images on your local Docker registry.<br>
You can pull them from a Docker registry or build them from the code.

If you choose to build them from the code please go to [Building containers from scratch](#Building-containers-from-scratch) section.

If you want to pull them from a Docker registry, run:
```bash
./minishift-pull-images.sh
```

### Running
First, you need the create and start the Minishift VM.<br>
Run:
```bash
./minishift-initialize.sh
```
This script will set _VirtualBox_ as VM driver of Minishift and start a Minishift VM with 6GB of RAM, 3 CPUs and 20GB of storage.

After the Minishift VM has started you can deploy the Kapua components by running:
```bash
./minishift-deploy.sh
```
This script will create new apps for each Kapua component.

### Accessing components
After deployment and startup of containers, they can be accessed at the following endpoints

| Application/Service | Endpoint                                         | User         | Password       | Others                            |
|---------------------|--------------------------------------------------|--------------|----------------|-----------------------------------|
| H2 SQL              | _None_                                           | _None_       | _None_         | This service is not exposed       |
| Elasticsearch       | _None_                                           | _None_       | _None_         | This service is not exposed       |
| Broker              | broker-eclipse-kapua.192.168.99.100.nip.io:31883 | kapua-broker | kapua-password |                                   |
| Admin WEB Console   | console-eclipse-kapua.192.168.99.100.nip.io:80   | kapua-sys    | kapua-password |                                   |
| REST API endpoint   | api-eclipse-kapua.192.168.99.100.nip.io:80       | kapua-sys    | kapua-password | API KEY: `12345678kapua-password` |

Please note that endpoints are available only from the host machine.

### Checking
You can check the status of the pods using the OpenShift Client from the command line or accessing the OpenShift Web Console.

To access the OpenShift Web Console you can run:
```bash
minishift dashboard
```

### Tear down
To stop and remove the Eclipse Kapua project from OpenShift, run:
```bash
./minishift-undeploy.sh
```
This command can take a while to be executed. Even if the `Eclipse Kapua` project has disappeared, processing still going on underneath.

To destroy the Minishift VM, runL
```bash
./minishift-destroy.sh
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

#### Passing additional JAVA_OPTS
If you want to pass to the JVM additional optional parameters you can set the `JAVA_OPTS_EXTRA` environment variable.<br>
Example:
```bash
export JAVA_OPTS_EXTRA="-Drequest.timeout=60000"
``` 

#### Building containers from scratch
If you want to build containers from the code, you'll need to build the whole Kapua Project.

From the project root directory, run:
```bash
mvn clean install -Pdocker
```

To build also the Admin Web Console container, which is excluded by default, add the `console` profile:
```bash
mvn clean install -Pconsole,docker
```

After the build has completed follow the steps from the [Running](#Running) section.
