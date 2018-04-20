# Building

We use Apache Maven as the build tool of choice.

We use `gitbook` to build the documentation.

## Kapua

Kapua is being compiled with Maven. 

In order to perform a full build of Kapua first you have to build the external 
resources of Kapua with the following command in the root of the Git repository:

    mvn clean install -f external/pom.xml

Than you can run the Kapua full build issuing the command:

    mvn clean install

If you only want to run Kapua locally for testing you can speed up the build
by using:

    mvn clean install -Pdev -DskipTests=true

## Documentation

Before you can build documentation, you need to install `gitbook`

### gitbook

To install gitbook run

    $ npm install -g gitbook-cli

If you don't have `npm` installed then you would need to install it first.

#### Install npm On Fedora

    $ yum install npm

#### Install npm On Fedora 24

This is what you should do if you are using Fedora 24+.

    $ dnf install nodejs

#### Install npm On Mac-OS

The easiest way would be through brew [brew]

You first install brew using the instructions on the [Brew][brew] website.

After you installed brew you can install npm by:

    brew install npm

[brew]: <http://brew.sh>

## Building the docs

To build documentation, run `gitbook build` from either `docs/developer-guide/en` or `docs/user-manual/en`

## Continuous integration
 
Kapua is running CI builds against two public environments:

- Travis CI  [![Build](https://api.travis-ci.org/eclipse/kapua.svg)](https://travis-ci.org/eclipse/kapua/) 
- Eclipse Hudson [![Hudson](https://img.shields.io/jenkins/s/https/hudson.eclipse.org/kapua/job/Develop.svg)](https://hudson.eclipse.org/kapua/)

Please be sure that both environments are "green" (i.e. all tests pass) after you commit any changes into `develop` branch.

We also use CI server sponsored by [Red Hat](https://www.redhat.com/en) to automatically push latest Docker images to 
[Kapua DockerHub account](https://hub.docker.com/r/kapua/). Red Hat CI server checks for code changes every 15 minutes and pushes updated version
of images if needed.

## Docker images

Kapua Docker images are hosted under [Kapua DockerHub account](https://hub.docker.com/r/kapua/). The latest snapshots of images are updated every 15 minutes.

In order to build Kapua Docker images yourself, execute Maven build with `docker` profile enabled:

    mvn clean install -Pdocker

If you want to speed up the build process you can ask Maven to ignore `-SNAPSHOT` updates
force it to use only locally present artifacts with the `dev` profile. You can also skip unit tests to speed things even more.

    mvn clean install -Pdocker,dev -DskipTests

### Pushing

Pushing with default settings:

    mvn -Pdocker deploy

Pushing to a specific docker registry:

    mvn -Pdocker deploy -Ddocker.push.registry=registry.hub.docker.com

Pushing to a specific docker registry under a specific account:

    mvn -Pdocker deploy -Ddocker.push.registry=registry.hub.docker.com -Ddocker.account=eclipse

By default Kapua applies the following tags to the published images:
- `latest`
- daily timestamp in format `YYYY-MM-DD`
- current project version (for example `0.0.1` or `0.1.2-SNAPSHOT`)