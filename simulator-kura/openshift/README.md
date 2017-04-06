# Running on OpenShift

This is a short documentation about how to spin up the Eclipse Kura™ simulator in OpenShift.


## Setting up inside Kapua

First of all you need some setup of [Eclipse Kapua™](https://eclipse.org/kapua "Eclipse Kapua™").
The easiest solution with OpenShift is to follow the readme in the Kapua GitHub repository: [dev-tools/src/main/openshift](https://github.com/eclipse/kapua/tree/develop/dev-tools/src/main/openshift "Setting up Kapua on OpenShift").

Afterwards start the following script

    ./setup.sh

## Setting up as external project

Assuming your have already set up Kapua, either using OpenShift or in a different way, you can simply create
a Kura simulator by:

    oc new-app -n "$OPENSHIFT_PROJECT_NAME" -f external-template.yml -p "DOCKER_ACCOUNT=ctron" -p "BROKER_URL=tcp://localhost:1833" -p "IMAGE_VERSION=0.1.2"

The `BROKER_URL` parameter must point to the MQTT broker you are using. It is also possible to use Websockets and user
credentials:

    ws://kapua-sys:kapua-password@localhost:80

## Scaling

The scripts will create a new OpenShift application named `kura-simulator` which will connect to the broker in
the local project. By default every pod will spin up 10 gateway instances. You can start more pods which will then
multiply the instances:

    oc scale --replicas=10 dc kura-simulator

It is also possible to change the number of instances inside each pod by setting an environment variable:

    oc set env dc/simulator KSIM_NUM_GATEWAYS=100

## Loading a simple data simulator model

Loading a JSON simulator data model works using OpenShift's `ConfigMap` feature:

    oc create configmap data-simulator-config --from-file=KSIM_SIMULATION_CONFIGURATION=../src/test/resources/example1.json
    oc set env --from=configmap/data-simulator-config dc/simulator
