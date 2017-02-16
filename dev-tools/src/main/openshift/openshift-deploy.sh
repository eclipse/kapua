###############################################################################
# Copyright (c) 2016, 2017 Red Hat Inc and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
###############################################################################

#!/usr/bin/env bash

set -e

OPENSHIFT_PROJECT_NAME="eclipse-kapua"

# print error and exit when necessary
die() { printf "$@" "\n" 1>&2 ; exit 1; }

#minishift start
#eval $(minishift docker-env)

# test if the project is already created ... fail otherwise 

oc describe "project/$OPENSHIFT_PROJECT_NAME" &>/dev/null || die "Project '$OPENSHIFT_PROJECT_NAME' not created or OpenShift is unreachable. Try with:\n\n\toc new-project eclipse-kapua\n\n"

#oc login
#oc new-project "$OPENSHIFT_PROJECT_NAME" --description="Open source IoT Platform" --display-name="Eclipse Kapua"

if [ -z "${DOCKER_ACCOUNT}" ]; then
  DOCKER_ACCOUNT=kapua
fi

echo Creating ElasticSearch server...

if [ -z "${ELASTIC_SEARCH_MEMORY}" ]; then
  ELASTIC_SEARCH_MEMORY=512M
fi

oc new-app -e ES_JAVA_OPTS="-Xms${ELASTIC_SEARCH_MEMORY} -Xmx${ELASTIC_SEARCH_MEMORY}" elasticsearch:2.4 -n "$OPENSHIFT_PROJECT_NAME"

echo ElasticSearch server created

### SQL database

echo Creating SQL database

oc new-app ${DOCKER_ACCOUNT}/kapua-sql --name=sql -n "$OPENSHIFT_PROJECT_NAME"
oc set probe dc/sql --readiness --open-tcp=3306

echo SQL database created

### Broker

echo Creating broker

oc new-app ${DOCKER_ACCOUNT}/kapua-broker:latest -name=kapua-broker -n "$OPENSHIFT_PROJECT_NAME" '-eACTIVEMQ_OPTS=-Dcommons.db.connection.host=localhost -Dcommons.db.connection.port=3306 -Dcommons.db.schema='
oc set probe dc/kapua-broker --readiness --open-tcp=1883

echo Broker created

## Build assembly module with
## mvn -Pdocker

echo Creating web console

oc new-app ${DOCKER_ACCOUNT}/kapua-console:latest -n "$OPENSHIFT_PROJECT_NAME" '-eCATALINA_OPTS=-Dcommons.db.connection.host=localhost -Dcommons.db.connection.port=3306 -Dcommons.db.schema='
oc set probe dc/kapua-console --readiness --liveness --initial-delay-seconds=30 --get-url=http://:8080/console

echo Web console created

### REST API

echo 'Creating Rest API'

oc new-app ${DOCKER_ACCOUNT}/kapua-api:latest -n "$OPENSHIFT_PROJECT_NAME" '-eCATALINA_OPTS=-Dcommons.db.connection.host=localhost -Dcommons.db.connection.port=3306 -Dcommons.db.schema='
oc set probe dc/kapua-api --readiness --liveness --initial-delay-seconds=30 --get-url=http://:8080/api

echo 'Rest API created'

## Applying DB schema

# Create batch job for liquibase
oc set image -f liquibase_job.yml "liquibase=$DOCKER_ACCOUNT/kapua-liquibase:latest" --local --source=docker -o yaml | oc create -f -

## Start router

#oc adm policy add-scc-to-user hostnetwork -z router
#oc adm router --create

## Expose web console

oc expose svc/kapua-console
oc expose svc/kapua-api

