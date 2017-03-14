# Kapua QA process

This chapter describes a quality assurance process of Kapua. Whenever you commit changes to the `develop` branch, be sure that you have followed those steps:

## Build tests

Kapua comes with a unit and simple integration tests as a part of regular build process. In order to be sure that Kapua is not broken, execute full build
using the following command:

    `mvn`
    
## Keep CI green

Also be always sure that our CI environments are always green:

- Travis CI  [![Build](https://api.travis-ci.org/eclipse/kapua.svg)](https://travis-ci.org/eclipse/kapua/) 
- Eclipse Hudson [![Hudson](https://img.shields.io/jenkins/s/https/hudson.eclipse.org/kapua/job/Develop.svg)](https://hudson.eclipse.org/kapua/)

## Testing OpenShift distribution

You should also verify that OpenShift regression test suite works as expected. First of all, start local OpenShift cluster as described 
[here](https://github.com/eclipse/kapua/blob/develop/docs/developer-guide/en/running.md#openshift).

Then navigate to `kapua/qa-openshift` directory and execute the following command:

    cd kapua/qa-openshift
    mvn -Dmaven.test.skip=false

