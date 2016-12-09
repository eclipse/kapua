###############################################################################
# Copyright (c) 2016 Red Hat and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
###############################################################################

#!/usr/bin/env bash

#oc login
#oc new-project eclipse-kapua --description="Open source IoT Platform" --display-name="Eclipse Kapua"

if [ -z "${DOCKER_ACCOUNT}" ]; then
  DOCKER_ACCOUNT='kapua'
fi

echo 'Starting ElasticSearch server...'

if [ -z "${ELASTIC_SEARCH_MEMORY}" ]; then
  ELASTIC_SEARCH_MEMORY='512M'
fi

oc new-app -e ES_JAVA_OPTS="-Xms${ELASTIC_SEARCH_MEMORY} -Xmx${ELASTIC_SEARCH_MEMORY}" elasticsearch -n eclipse-kapua

echo 'ElasticSearch server started.'

### SQL database

echo 'Staring SQL database'

oc new-app ${DOCKER_ACCOUNT}/kapua-sql --name=sql -n eclipse-kapua

echo 'SQL database started'

### Broker

echo 'Starting broker'

oc new-app ${DOCKER_ACCOUNT}/kapua-broker:latest -name=kapua-broker -n eclipse-kapua

echo 'Broker started'

## Build assembly module with
## mvn -Pdocker

echo 'Starting web console'

oc new-app ${DOCKER_ACCOUNT}/kapua-console:latest -n eclipse-kapua

##oc expose svc/kapua-console

echo 'Web console started.'

echo 'Starting rest api'

oc new-app ${DOCKER_ACCOUNT}/kapua-api:latest -n eclipse-kapua

echo 'Rest api started'

## Applying DB schema

SQL_HOST=`oc get service | grep sql | awk '{print $2}'`
docker run -it -e SQL_SERVICE_HOST=${SQL_HOST} ${DOCKER_ACCOUNT}/kapua-liquibase