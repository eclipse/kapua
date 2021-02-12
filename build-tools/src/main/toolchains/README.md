# Pre-configured Maven toolchain configurations

Maven uses [toolchains](https://maven.apache.org/guides/mini/guide-using-toolchains.html) in order to manage different JDK versions during build time.

As those toolchains reference local directories, those file can look different for each local system setup.

This directory contains a few pre-configured toolchains for well known configurations. You can either use
those directly or create a new toolchain file which works for you.

## `fedora-rhel-centos.xml`

This file is for Fedora/RHEL/CentOS based distributions. In order to install required JDKs issue
the following commands with root privileges:

    yum install java-1.6.0-openjdk-devel java-1.7.0-openjdk-devel java-1.8.0-openjdk-devel

Or (when using `dnf` instead of `yum`):

    dnf install java-1.6.0-openjdk-devel java-1.7.0-openjdk-devel java-1.8.0-openjdk-devel

Either copy or sym-link this file to `~/.m2/toolchains.xml` to activate it.