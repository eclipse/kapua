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

: OPENSHIFT_DIR=${OPENSHIFT_DIR:='/var/tmp/openshift'}

if which oc &>/dev/null; then
    export OC=oc
else
    export OC=${OC:="${OPENSHIFT_DIR}/oc"}
fi

: OPENSHIFT_HOST=${OPENSHIFT_HOST:='localhost:8443'}
: OPENSHIFT_USER=${OPENSHIFT_USER:='admin'}
: OPENSHIFT_PASS=${OPENSHIFT_PASS:='admin'}
: OPENSHIFT_LOGIN_OPTS=${OPENSHIFT_LOGIN_OPTS:=''}

: OPENSHIFT_PROJECT_NAME=${OPENSHIFT_PROJECT_NAME:='eclipse-kapua'}

# print error and exit when necessary
die() {
    printf "$@" "\n" 1>&2 ;
    exit 1;
}
