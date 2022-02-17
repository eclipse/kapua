#!/usr/bin/env bash
###############################################################################
# Copyright (c) 2016, 2022 Red Hat Inc and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Red Hat Inc - initial API and implementation
#     Eurotech
###############################################################################

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. ${SCRIPT_DIR}/openshift-common.sh

### Create Kapua project

${OC} login ${OPENSHIFT_HOST} --username=${OPENSHIFT_USER} --password=${OPENSHIFT_PASS} ${OPENSHIFT_LOGIN_OPTS}

${OC} new-project "${OPENSHIFT_PROJECT_NAME}" --description="Open source IoT Platform" --display-name="Eclipse Kapua"
