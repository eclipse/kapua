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

: OPENSHIFT_DOWNLOAD_LINK=${OPENSHIFT_DOWNLOAD_LINK:='https://github.com/openshift/origin/releases/download/v1.4.1/openshift-origin-server-v1.4.1-3f9807a-linux-64bit.tar.gz'}

### Install OpenShift distribution
if [ -f "${OPENSHIFT_DIR}/oc" ]; then
    echo "Found OpenShift client in ${OPENSHIFT_DIR}. Skipping download."
else
    mkdir -p ${OPENSHIFT_DIR}

    pushd ${OPENSHIFT_DIR}
    curl -L ${OPENSHIFT_DOWNLOAD_LINK} | tar xvz --strip 1
    popd
fi

### Start OpenShift cluster

if [ "${DOCKERIZED}" == "FALSE" ]; then
    ${OPENSHIFT_DIR}/openshift start
else
    ${OC} cluster up --metrics
fi

echo "OpenShift client has been installed in ${OPENSHIFT_DIR}."
echo "Add ${OPENSHIFT_DIR} to your \$PATH or export OC=${OPENSHIFT_DIR}/oc before invoking other scripts."
