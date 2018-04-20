#!/usr/bin/env bash

###############################################################################
# Copyright (c) 2016, 2017 Red Hat Inc and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
###############################################################################

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. ${SCRIPT_DIR}/openshift-common.sh

### Remove Kapua

echo Undeploying Eclipse Kapua from Openshift

${OC} login ${OPENSHIFT_HOST} --username=${OPENSHIFT_USER} --password=${OPENSHIFT_PASS} ${OPENSHIFT_LOGIN_OPTS}

${OC} delete project "${OPENSHIFT_PROJECT_NAME}"

echo Eclipse Kapua undeployed from Openshift