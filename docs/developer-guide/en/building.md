# Building

We use Apache Maven as the build tool of choice.

We use `gitbook` to build the documentation.

## Kapua

Kapua is being compiled with Maven. In order to perform a full build
of Kapua simply issue the following command in the root of the Git repository:

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

You first install brew using the instructions on the [brew] website.

After you installed brew you can install npm by:

    brew install npm

[brew]: <http://brew.sh>

## Building the docs

To build documentation, run `gitbook build` from either `docs/developer-guide/en` or `docs/user-manual/en`

## Continuous integration
 
Kapua is running CI builds against two public environments:

- Travis CI  [![Build](https://api.travis-ci.org/eclipse/kapua.svg)](https://travis-ci.org/eclipse/kapua/) 
- Eclipse Hudson [![Hudson](https://img.shields.io/jenkins/s/https/hudson.eclipse.org/kapua/job/Develop.svg)](https://hudson.eclipse.org/kapua/)

We also use CI server sponsored by [Red Hat](https://www.redhat.com/en) to automatically push latest Docker images to 
[Kapua DockerHub account](https://hub.docker.com/r/kapua/). Red Hat CI server checks for code changes every 15 minutes and pushes updated version
of images if needed.