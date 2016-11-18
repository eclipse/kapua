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

echo 'Starting ElasticSearch server...'

if [ -z "${ELASTIC_SEARCH_MEMORY}" ]; then
  ELASTIC_SEARCH_MEMORY='512M'
fi

oc new-app -e ES_JAVA_OPTS="-Xms${ELASTIC_SEARCH_MEMORY} -Xmx${ELASTIC_SEARCH_MEMORY}" elasticsearch

echo 'ElasticSearch server started.'

echo 'Staring SQL database'

oc new-app hekonsek/h2 --name=sql

echo 'SQL database started'


## Build assembly module with
## mvn -Pdocker

##echo 'Starting broker'

##oc new-app --docker-image kapua-broker:latest -name=kapua-broker -n eclipse-kapua

##echo 'Broker started'

##echo 'Starting web console'

##oc new-app --docker-image kapua-console:latest -name=kapua-console -n eclipse-kapua

##oc expose svc/kapua-console --hostname=kapua-console.com

##echo 'Web console started'

##echo 'Starting rest api'

##oc new-app --docker-image kapua-api:latest -name=kapua-api -n eclipse-kapua

##echo 'Rest api started'