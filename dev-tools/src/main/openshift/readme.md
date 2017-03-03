# Kapua on OpenShift

Before you start, make sure that you have OpenShift Origin properly installed in your  machine:

    $ oc version
    oc v1.4.1+3f9807a
    kubernetes v1.4.0+776c994
    features: Basic-Auth GSSAPI Kerberos SPNEGO

You don't have OpenShift Origin installed on your machine? No worries...

## Installing OpenShift Origin on CentOS 7 minimal

Before you install OpenShift, make sure you have Docker server properly installed:

    curl -sSL https://get.docker.com/ | sh
    systemctl enable docker.service
    systemctl start docker

Now download OpenShift Origin binary distribution and install it into your file system:

    wget -nc https://github.com/openshift/origin/releases/download/v1.4.1/openshift-origin-client-tools-v1.4.1-3f9807a-linux-64bit.tar.gz
    tar xzf openshift-origin-client-tools-v1.4.1-3f9807a-linux-64bit.tar.gz
    ln -s openshift-origin-client-tools-v1.4.1+3f9807a-linux-64bit openshift
    ln -s openshift/oc /usr/local/bin/oc

In order to start a local OpenShift installation simply run:

    oc cluster up
    
Or the following command if you want to enable metrics support:

    oc cluster up --metrics

An create a new project using:

    oc new-project eclipse-kapua

## Ensuring enough entropy

It may happen that firing up docker containers and starting up application which use
Java's `SecureRandom` (which happens in the next step a few times) run dry the Linux
Kernel's entropy pool. The result is that some application will block during startup
(even longer than 30 seconds) which will trigger OpenShift to kill the pods since they
are considered unresponsive (which they actually are).

You can check the amount of entropy the kernel has available with the following command:

    cat /proc/sys/kernel/random/entropy_avail

If this number drops to zero, then the kernel has run out of entropy and application will
block.

One solution (there are a few others) is to install `haveged` a user-space daemon
which provides entropy to the kernel.

On CentOS 7 it can be installed with the following commands (all as `root`):

    yum install epel-release # only if you 
    yum install haveged
    systemctl enable --now haveged

As the package comes from the [EPEL repositories](https://fedoraproject.org/wiki/EPEL "Information about EPEL").
If you haven't yet enabled those repositories, then you need to do this before trying to
install `haveged`:

    yum install epel-release

For more information about `haveged` see http://www.issihosts.com/haveged/

For more information about the "EPEL repositories" see https://fedoraproject.org/wiki/EPEL

## Starting Kapua on OpenShift

Execute the following command:

    oc new-app -f https://raw.githubusercontent.com/eclipse/kapua/develop/dev-tools/src/main/openshift/kapua-template.yml -p DOCKER_ACCOUNT=hekonsek

Now open the following URL in your web browser - `http://localhost:8080/console`. And log-in into Kapua UI using default
credentials:

<dl>
	<dt>username</dt><dd>kapua-sys</dd>
	<dt>password</dt><dd>kapua-password</dd>
</dl>
