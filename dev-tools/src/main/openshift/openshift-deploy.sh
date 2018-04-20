#!/usr/bin/env bash

###############################################################################
# Copyright (c) 2016, 2017 Red Hat Inc and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Red Hat Inc - initial API and implementation
#     Eurotech
###############################################################################

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. ${SCRIPT_DIR}/openshift-common.sh

: DOCKER_ACCOUNT=${DOCKER_ACCOUNT:=kapua}
: IMAGE_VERSION=${IMAGE_VERSION:=latest}
: JAVA_OPTS_EXTRA=${JAVA_OPTS_EXTRA:=''}

### Test if the project is already created ... fail otherwise

${OC} login ${OPENSHIFT_HOST} --username=${OPENSHIFT_USER} --password=${OPENSHIFT_PASS} ${OPENSHIFT_LOGIN_OPTS}

${OC} describe "project/${OPENSHIFT_PROJECT_NAME}" &>/dev/null || die "Project '${OPENSHIFT_PROJECT_NAME}' not created or OpenShift is unreachable. Try with:\n\n\toc new-project ${OPENSHIFT_PROJECT_NAME}\n\n"

### Create secrets

echo Creating Kapua secrets ...

CONFIG=${SCRIPT_DIR}/../../../../assembly/events-broker/etc/

${OC} create secret generic events-broker-conf \
  --from-file=${CONFIG}/broker.xml \
  --from-file=${CONFIG}/bootstrap.xml \
  --from-file=${CONFIG}/artemis-users.properties \
  --from-file=${CONFIG}/artemis-roles.properties \
  --from-file=${CONFIG}/login.config \
  --from-file=${CONFIG}/logging.properties \
  --from-file=${CONFIG}/artemis.profile

### Create Kapua from template

echo Creating Kapua from template ...

#${OC} new-app -n "${OPENSHIFT_PROJECT_NAME}" -f kapua-template.yml -p "DOCKER_ACCOUNT=${DOCKER_ACCOUNT}" -p "IMAGE_VERSION=${IMAGE_VERSION}" -p "JAVA_OPTS_EXTRA=${JAVA_OPTS_EXTRA}"
${OC} new-app -n "${OPENSHIFT_PROJECT_NAME}" -f kapua-template-core.yml -p "DOCKER_ACCOUNT=${DOCKER_ACCOUNT}" -p "IMAGE_VERSION=${IMAGE_VERSION}" -p "JAVA_OPTS_EXTRA=${JAVA_OPTS_EXTRA}"
${OC} new-app -n "${OPENSHIFT_PROJECT_NAME}" -f kapua-template-broker.yml -p "DOCKER_ACCOUNT=${DOCKER_ACCOUNT}" -p "IMAGE_VERSION=${IMAGE_VERSION}" -p "JAVA_OPTS_EXTRA=${JAVA_OPTS_EXTRA}"
${OC} new-app -n "${OPENSHIFT_PROJECT_NAME}" -f kapua-template-console.yml -p "DOCKER_ACCOUNT=${DOCKER_ACCOUNT}" -p "IMAGE_VERSION=${IMAGE_VERSION}" -p "JAVA_OPTS_EXTRA=${JAVA_OPTS_EXTRA}"
${OC} new-app -n "${OPENSHIFT_PROJECT_NAME}" -f kapua-template-api.yml -p "DOCKER_ACCOUNT=${DOCKER_ACCOUNT}" -p "IMAGE_VERSION=${IMAGE_VERSION}" -p "JAVA_OPTS_EXTRA=${JAVA_OPTS_EXTRA}"

echo Creating Kapua from template ... done!
