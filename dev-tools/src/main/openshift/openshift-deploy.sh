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

. openshift-common.sh

 : OPENSHIFT_PROJECT_NAME=${OPENSHIFT_PROJECT_NAME:=eclipse-kapua}
 : DOCKER_ACCOUNT=${DOCKER_ACCOUNT:=kapua}

# print error and exit when necessary

die() { printf "$@" "\n" 1>&2 ; exit 1; }

# test if the project is already created ... fail otherwise 

$OC describe "project/$OPENSHIFT_PROJECT_NAME" &>/dev/null || die "Project '$OPENSHIFT_PROJECT_NAME' not created or OpenShift is unreachable. Try with:\n\n\toc new-project eclipse-kapua\n\n"

### Create Kapua from template

echo Creating Kapua from template ...
$OC new-app -f kapua-template.yml -p "DOCKER_ACCOUNT=$DOCKER_ACCOUNT"
echo Creating Kapua from template ... done!

### ElasticSearch

echo Creating ElasticSearch server...

if [ -z "${ELASTIC_SEARCH_MEMORY}" ]; then
  ELASTIC_SEARCH_MEMORY=512M
fi

$OC new-app -e ES_JAVA_OPTS="-Xms${ELASTIC_SEARCH_MEMORY} -Xmx${ELASTIC_SEARCH_MEMORY}" elasticsearch:2.4 -n "$OPENSHIFT_PROJECT_NAME"

echo ElasticSearch server created