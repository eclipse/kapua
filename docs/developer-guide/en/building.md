# Building

We use Apache Maven as the build tool of choice.

We use `gitbook` to build the documentation.

## Kapua

Kapua is being compiled with Maven.
For a quick build of Kapua, for testing purposes and packaged with the web console build, we recommend the command:

`mvn clean install -DskipTests=true -Pdev,console`

There are 3 build options you can use, based on the need to perform the various tests, these are now reported in ascending order of building times.

1. Build without tests:

   `mvn clean install -DskipTests=true`

2. Build executing only unit tests:
```
    mvn clean install -Dcommons.settings.hotswap=true -Dgroups='org.eclipse.kapua.qa.markers.junit.JUnitTests' -DskipITs=true
```

For the next build option, considering that some integration tests require access to services deployed in a docker container, first of all, you have to launch these 2 commands in order to build and create the docker containers (NB: now make sure the docker daemon is running!)
```
    mvn clean install -DskipTests=true  -Pconsole
    mvn clean install -f ./assembly -DskipTests  -Pconsole,docker
```
Attention: if the kapua containers are already running in your environment, for example in the case of a previous building that terminated abnormally, please stop their execution before proceeding with the next build commands

3. Build executing both unit tests and integration tests. The first 2 commands execute the integration tests, the last execute a complete build with unit tests:
```
    mvn verify -Dcommons.db.schema=kapuadb -Dcommons.settings.hotswap=true -Dbroker.host=localhost -Dgroups='!org.eclipse.kapua.qa.markers.junit.JUnitTests' -Dcucumber.filter.tags="@env_none"
    mvn verify -Dcommons.db.schema=kapuadb -Dcommons.settings.hotswap=true -Dbroker.host=localhost -Dgroups='!org.eclipse.kapua.qa.markers.junit.JUnitTests' -Dcucumber.filter.tags="@env_docker_base"
    mvn clean install -Dcommons.settings.hotswap=true -Dgroups='org.eclipse.kapua.qa.markers.junit.JUnitTests' -DskipITs=true
```

Note: there are 2 maven profiles than can be used in the building options above
1. The "console" profile allows building the web console if needed.

   Usage example, building without tests & with the web console:

   `mvn clean install -DskipTests=true -Pconsole`

2. The "dev" profile speeds up some building processes.

   Usage example, in the scenario in which you only want to run Kapua locally for testing:

   `mvn clean install -DskipTests=true -Pdev`

   then, if needed, add the console profile as well:

   `mvn clean install -DskipTests=true -Pdev, console`
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

Kapua is running CI builds in the following public environments:

- GitHub Actions  ![GitHub Actions CI](https://img.shields.io/github/workflow/status/eclipse/kapua/kapua-continuous-integration?label=GitHub%20Action%20CI&logo=GitHub)
- Eclipse Hudson  ![Hudson](https://img.shields.io/jenkins/build?jobUrl=https:%2F%2Fci.eclipse.org%2Fkapua%2Fjob%2Fdevelop-build&label=Jenkins%20Build)

Please be sure that both environments are "green" (i.e. all tests pass) after you commit any changes into `develop` branch.

We also use CI server sponsored by [Red Hat](https://www.redhat.com/en) to automatically push latest Docker images to
[Kapua DockerHub account](https://hub.docker.com/r/kapua/). Red Hat CI server checks for code changes every 15 minutes and pushes updated version
of images if needed.

## Docker images

Kapua Docker images are hosted under [Kapua DockerHub account](https://hub.docker.com/r/kapua/). The latest snapshots of images are updated every 15 minutes.

In order to build Kapua Docker images yourself, execute Maven build with `docker` profile enabled:

    mvn clean install -Pdocker

Just like building Kapua from the source code, also add the `console` profile to generate the Web Console docker image:

    mvn clean install -Pdocker,console

If you want to speed up the build process you can ask Maven to ignore `-SNAPSHOT` updates
force it to use only locally present artifacts with the `dev` profile. You can also skip unit tests to speed things even more.

    mvn clean install -Pdocker,dev -DskipTests

Again, don't forget the `console` profile if the Web Console image is needed:

    mvn clean install -Pdocker,dev,console -DskipTests

### Pushing

Pushing with default settings:

    mvn -Pdocker deploy

Pushing to a specific docker registry:

    mvn -Pdocker deploy -Ddocker.push.registry=registry.hub.docker.com

Pushing to a specific docker registry under a specific account:

    mvn -Pdocker deploy -Ddocker.push.registry=registry.hub.docker.com -Ddocker.account=eclipse

Don't forget to add the `console` Maven profile to the console above if you're interested in pushing the Web Console image as well.

By default Kapua applies the following tags to the published images:
- `latest`
- daily timestamp in format `YYYY-MM-DD`
- current project version (for example `0.0.1` or `0.1.2-SNAPSHOT`)
