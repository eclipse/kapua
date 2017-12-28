![Eclipse Kapua™ logo](docs/user-manual/en/images/kapua-logo.png)

# Eclipse Kapua&trade;

[![Build Status](https://travis-ci.org/eclipse/kapua.svg?branch=develop)](https://travis-ci.org/eclipse/kapua/branches)
[![Hudson](https://img.shields.io/jenkins/s/https/hudson.eclipse.org/kapua/job/Develop.svg)](https://hudson.eclipse.org/kapua/job/develop)

[Eclipse Kapua&trade;](http://eclipse.org/kapua) is a modular platform providing the services required to manage IoT gateways and smart edge devices. Kapua provides a core integration framework and an initial set of core IoT services including a device registry, device management services, messaging services, data management, and application enablement.


## Quick Start Guide

Running an Eclipse Kapua&trade; demo instance on your local machine is a simple task.

Eclipse Kapua&trade; runs as distributed application that exposes three basic services:
* The Messaging Service
* The RESTful API
* The Web Administration Console

Two more backend services are required that implement the data tier:
* The Event Bus Service
* The SQL database
* The NoSQL datastore

Eclipse Kapua&trade; can be deployed in a variety of modes. A practical way for running a local demo is through Docker containers.

### Requirements

Before starting, check that your environment has the following prerequisites:

* 64 bit architecture
* Java VM Version 8
* Docker Version 1.2+
* Internet Access (needed to download the artifacts)

### Demo Setup

The team maintains some docker images in a Docker Hub repository at [Kapua Repository](https://hub.docker.com/r/kapua/). Check the repo to view the list of available images, if you haven't found one fitting your needs you may create your own. Please refer to the paragraph [More deployment info](#more-deployment-info) to find more about creating your own images and/or alternative demo deployment scenarios.

***
**Note :** the Docker Hub repository mentioned above is not the official project repository from Eclipse Foundation.
***

Suppose the target is the current snapshot 1.0.0-SNAPSHOT.

* Clone Eclipse Kapua into a local directory
* Open an OS shell and move to Kapua root dir
* Start Docker runtime
* Start Kapua:

    kapua/dev-tools/src/main/docker/docker-deploy.sh

The command starts all the Kapua containers using Docker Compose.

By default, the `latest` version of images will be used. If you want to run some other version of Kapua, set the `IMAGE_VERSION` environment variable, like

    export IMAGE_VERSION=0.2.0

You can check if the containers are running by typing the following command:

    docker ps -as

Docker will list the containers currently running.

To stop Kapua, run

    kapua/dev-tools/src/main/docker/docker-undeploy.sh

### Access

Once the containers are running, the Kapua services can be accessed. Kapua is a multi tenant
system. The demo installation comes with one default tenant, called _kapua-sys_, which is also the root tenant. In Eclipse Kapua a tenant is commonly referred to as an _account_.

#### The console

The administration console is available at http://localhost:8080/. Copy paste the URL above to a Web browser, as the login screen appears, type the following credentials:

* Username: `kapua-sys`
* Password: `kapua-password`

Press _Login_ button and start working with the console.

**Note**: If you are using Docker on Windows the hostname will most likely not be `localhost` but
the IP address of your docker instance.

#### RESTful APIs

The documentation of RESTful API is available at http://localhost:8081/doc/ while the mount points are available at http://localhost:8081/v1/.

The documentation is available through Swagger UI which greatly helps testing and exploring the exposed services.

In order to get access a REST resource through an API, an authentication token is needed. Fortunately the token can be easily obtained by executing the authentication API. There are several ways to invoke the API, an easy one is by using the Swagger UI:

* Open the URL http://localhost:8081/doc/
* Select item _Authentication_
* Select item _/authentication/user_
* Using the test feature run 'POST /authentication/user' by specifying the following body:

```
{
  "password" : [ "kapua-password" ],
  "username": "kapua-sys"
}
```

**Note:** as an alternative to the previous, if curl is available on your machine, execute the following from the shell:

```
curl -X POST  'http://localhost:8081/v1/authentication/user' --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{
  "password": [
    "kapua-password"
  ],
  "username": "kapua-sys"
}'
```

The system will return a JSON object.

* Copy the value of the field _tokenId_
* Paste the value in the box labelled _api\_key_ at the top of the web page

Swagger will automatically add the authentication token to each subsequent request done using the Swagger UI. You're ready to try executing the documented APIs.

**Note**: If you are using Docker on Windows the hostname will most likely not be `localhost` but
the IP address of your docker instance.

#### The Broker

The broker container exposes an [Mqtt](http://mqtt.org/) end point at tcp://localhost:1883/.
The broker can be accessed through either [Eclipse Kura](http://www.eclipse.org/kura/) or a plain Mqtt client like, for example, [Eclipse Paho](http://www.eclipse.org/paho/).

In order for a client to establish an Mqtt connection with the broker, a client must provide a valid identity. The _kapua-sys_ account provides the user named _kapua-broker_ which has been pre-seeded and profiled for the purpose.

The credentials for the user kapua-broker are the following:

* Username: `kapua-broker`
* Password: `kapua-password`

**Note:** do not use the user `kapua-sys` to establish Mqtt connections.

**Note**: If you are using Docker on Windows the hostname will most likely not be `localhost` but
the IP address of your docker instance.

#### Simulation

Kapua comes with a framework that you can use to simulate Kura gateways. It can be used to test your Kapua deployments easily. See [Simulator documentation](docs/user-manual/en/simulator.md) for more info.

#### More deployment info
Installing and running a demo using Docker is easy but it's not the only way. There are other scenarios that the users may be interested to. We provide advanced setup scenarios in the following guides:

* [Running with Docker](assembly/README.md)
* [Running with Vagrant](dev-tools/src/main/vagrant/README.md#demo-machine-quick-start)
* [Running with OpenShift](docs/developer-guide/en/running.md#openshift)

They will provide more advanced deployment scenarios.

### User & Developer guides

* [User Manual](http://download.eclipse.org/kapua/docs/develop/user-manual/en)
* [Developer Guide](http://download.eclipse.org/kapua/docs/develop/developer-guide/en)

### Contributing

If you're interested to get involved in IoT and Eclipse Kapua&trade; project, join the community and give your contribution to the project, please read [how to contribute to Eclipse Kapua&trade;](https://github.com/eclipse/kapua/blob/develop/CONTRIBUTING.md).
