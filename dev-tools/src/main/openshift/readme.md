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

    wget -nc https://github.com/openshift/origin/releases/download/v1.4.1/openshift-origin-server-v1.4.1-3f9807a-linux-64bit.tar.gz
    tar xpf openshift-origin-server-v1.4.1-3f9807a-linux-64bit.tar.gz
    mv openshift-origin-server-v1.4.1+3f9807a-linux-64bit openshift
    ln -s ~/openshift/oc /usr/local/bin/oc

Don't forget to start OpenShift server, log into it and creating new project before you proceed:

    nohup openshift/openshift start &
    oc login --insecure-skip-tls-verify=true https://localhost:8443 -u admin -p admin
    oc new-project eclipse-kapua

## Starting Kapua on OpenShift

Execute the following command:

    DOCKER_ACCOUNT=hekonsek bash <(curl -sL https://raw.githubusercontent.com/eclipse/kapua/develop/dev-tools/src/main/openshift/openshift-deploy.sh)

Now open the following URL in your web browser - `http://localhost:8080/console`. And log-in into Kapua UI using default
credentials:

    username: kapua-sys
    password: kapua-password
