# Kapua on Docker

To learn more on how to run Kapua using Docker, please
consult [developer manual](https://github.com/eclipse/kapua/blob/develop/docs/developer-guide/en/running.md#docker).

The most recent version of the documentation can be viewed online at:

* http://download.eclipse.org/kapua/docs/develop/user-manual/en/
* http://download.eclipse.org/kapua/docs/develop/developer-guide/en/

## How to run Kapua on Docker.

You are required to have Docker installed and running.<br>
It is advisable to give to Docker at least 6GB of ram and 2 CPUs.

### Running

Just simply run:

```bash
./unix/docker-deploy.sh
```

PowerShell scripts are also available for Windows OS:

```bash
.\win\docker-deploy.ps1
```

### Accessing components

After deployment and startup of containers, they can be accessed at the following endpoints

| Application/Service | Endpoint       | User         | Password       | Others                            |
|---------------------|----------------|--------------|----------------|-----------------------------------|
| H2 SQL              | localhost:3306 | kapua        | kapua          | Schema name: `kapuadb`            |
| Elasticsearch       | localhost:9200 |              |                |                                   |
| Message Broker      | localhost:1883 | kapua-broker | kapua-password |                                   |
| Admin WEB Console   | localhost:8080 | kapua-sys    | kapua-password |                                   |
| REST API endpoint   | localhost:8081 | kapua-sys    | kapua-password | API KEY: `12345678kapua-password` |

### Checking

You can check Docker containers logs to check that everything has started and is running properly, by running:

```bash
./unix/docker-logs.sh
```

Powershell:

```bash
.\win\docker-logs.ps1
```

You can also start check logs with the `./docker-deploy.sh` script by providing the `--logs` option:

```bash
./unix/docker-deploy.sh --logs
```

Powershell

```bash
.\win\docker-deploy.ps1 --logs
```

### Tear down

To stop and remove all containers, run:

```bash
./unix/docker-undeploy.sh
```

Powershell:

```bash
.\win\docker-undeploy.ps1
```

### Single Sign On (OpenID Connect)

*This section needs to be updated*

To enable Single Sign On (SSO), please refer to `deployment/docker/unix/sso/README.md`. For more details about the SSO
deployment, please check the `docs/developer-guide/en/sso.md` SSO developer guide.

---

## Advanced options

### Setting the Kapua version

Other than the default deployment it is possible to run other versions of Kapua.

By default, the `latest` version of Kapua will be brought up. You can change the version of Kapua by exporting the
environment variable `IMAGE_VERSION`. Please also remember to checkout the related git tag first.

Example:

```bash
git checkout 1.0.0-M5
export IMAGE_VERSION=1.0.0-M5
./docker-deploy.sh
```

---

### Enabling development mode

`kapua-sql` container can optionally enable its web admin console. To enable it, provide the `--dev` option.

Example:

```bash
./docker-deploy.sh --dev
```

---

### Enabling remote JVM debug mode

Containers can also be remotely debugged. To enable it, provide the `--debug` option.

Example:

```bash
./docker-deploy.sh --debug
```

Following ports will be opened

| Application/Service | Remote JVM debug endpoint |
|---------------------|---------------------------|
| Message Broker      | localhost:5005            |
| Consumer Lifecycle  | localhost:8002            |
| Consumer Telemetry  | localhost:8001            |
| Admin WEB Console   | localhost:5007            |
| REST API endpoint   | localhost:5006            |
| Job Engine          | localhost:5008            |

---

### Building containers from scratch

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

---

### Enabling Single Sing On (SSO)

Kapua can be deployed with an SSO provider. To enable it, provide the `--sso` option.

Example:

```bash
./docker-deploy.sh --sso
```

By adding this option Kapua will be deployed with an instance of [Keycloak](https://www.keycloak.org/) SSO provider.

The Keycloack instance can be accessed at the following endpoints:

| Application/Service | Endpoint       | User  | Password | Notes             |
|---------------------|----------------|-------|----------|-------------------|
| Keycloak Console    | localhost:9090 | admin | admin    |                   |

This option is not available on Windows OS at the moment.

---

### Enabling SSL

To enable SSL in the Jetty (Console and REST API) and Broker containers, set the `KAPUA_DISABLE_SSL` environment
variable to `false`, and other variables according to the desired behavior, **before** running the `docker-deploy.sh`
script:

```bash
export KAPUA_DISABLE_SSL=false
```

Additionally, the SSL can be configured in two ways: providing a certificate, a private key and an optional CA chain, or
providing a keystore.

##### Providing certificates and private key

To use an existing certificate, a private key and a CA you can set the following environment variables:

- **KAPUA_CRT**: The certificate to use
- **KAPUA_CA**: *Optional* - The CA chain that validates the certificate
- **KAPUA_KEY** The private key for the certificate
- **KAPUA_KEY_PASSWORD** *Optional* - The password for the private key

All the values should be exported as inline values. e.g.:

```bash
export KAPUA_CA=$(cat /path/to/myCA.pem)
export KAPUA_CRT=$(cat /path/to/certificate.crt)
export KAPUA_KEY=$(cat /path/to/private.key)
export KAPUA_KEY_PASSWORD=private_key_password
``` 

##### Providing a Keystore

Otherwise, two environment variables can be used to provide an already existing keystore and its password:

- **KAPUA_KEYSTORE**: The base64 encoded keystore
- **KAPUA_KEYSTORE_PASSWORD**: The password for the keystore

Again, All the values should be exported as inline values. e.g.:

```bash
export KAPUA_KEYSTORE=$(base64 /path/to/keystore.pkcs)
export KAPUA_KEYSTORE_PASSWORD=keystore_password
```

---

### Providing custom logging level

The `info` logging level is used as default root level in container logs. To provide a different logging level you can
set the following environment variable:

- **LOGBACK_LOG_LEVEL**: The logging level value

Allowed logging level values are: `trace`, `debug`, `info`, `warn`, `error`, `all` or `off`. e.g.:

```bash
export LOGBACK_LOG_LEVEL=debug
```
