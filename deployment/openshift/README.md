# Kapua on OpenShift

To learn more on how to run Kapua in Openshift, please consult [developer manual](https://github.com/eclipse/kapua/blob/develop/docs/developer-guide/en/running.md#openshift).

The most recent version of the documentation can be viewed online at:

* http://download.eclipse.org/kapua/docs/develop/user-manual/en/
* http://download.eclipse.org/kapua/docs/develop/developer-guide/en/

## How to run Kapua on OpenShift.

You are required to have OpenShift Origin installed.<br>
If you are running on MacOS or Windows machine, please go to the _Minishift_ deployment.
OpenShift Origin is supported only on Linux OSs.

### Running
First, you need the OpenShift client and a OpenShift Origin cluster running.<br>
Just run:
```bash
./openshift-start.sh
```
This script will download the OpenShift Client and start the OpenShift cluster.

Then you need to initialize the OpenShift Cluster by running:
```bash
./openshift-initialize.sh
```
This script will create a new project into OpenShift.

Final step of the deployment, is the deployment of the Docker images themself.<br>
Run:
```bash
./openshift-deploy.sh
```
This script will create new apps for each Kapua component.

### Accessing components
After deployment and startup of containers, they can be accessed at the following endpoints

| Application/Service | Endpoint                                              | User         | Password       | Others                            |
|---------------------|-------------------------------------------------------|--------------|----------------|-----------------------------------|
| H2 SQL              | _None_                                                | _None_       | _None_         | This service is not exposed       |
| Elasticsearch       | _None_                                                | _None_       | _None_         | This service is not exposed       |
| Broker              | broker-eclipse-kapua.<openshift-default-domain>:31883 | kapua-broker | kapua-password |                                   |
| Admin WEB Console   | console-eclipse-kapua.<openshift-default-domain>:80   | kapua-sys    | kapua-password |                                   |
| REST API endpoint   | api-eclipse-kapua.<openshift-default-domain>:80       | kapua-sys    | kapua-password | API KEY: `12345678kapua-password` |

### Checking
You can check the status of the pods using the OpenShift Client from the command line or accessing the OpenShift Web Console.


### Tear down
To stop and remove the Eclipse Kapua project from OpenShift, run:
```bash
./openshift-destroy.sh
```
After this command, you'll need to run again `./openshift-initialize.sh` if you want to deploy again Kapua.

### Advanced options

#### Setting the Kapua version
Other than the default deployment it is possible to run other versions of Kapua.

By default the `latest` version of Kapua will be brought up.
You can change the version of Kapua by exporting the environment variable `IMAGE_VERSION`.

Example:
```bash
export IMAGE_VERSION=1.4.0
``` 

#### Passing additional JAVA_OPTS
If you want to pass to the JVM additional optional parameters you can set the `JAVA_OPTS_EXTRA` environment variable.<br>
Example:
```bash
export JAVA_OPTS_EXTRA="-Ddevice.management.request.timeout=60000"
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