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

if [ "${DOCKERIZED}" == "FALSE" ]; then
    wget -nc https://github.com/openshift/origin/releases/download/v1.4.1/openshift-origin-server-v1.4.1-3f9807a-linux-64bit.tar.gz -O /tmp/openshift-origin-server-v1.4.1-3f9807a-linux-64bit.tar.gz
fi

set -e

### Configuration

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. $SCRIPT_DIR/openshift-common.sh

### Install OpenShift distribution

mkdir -p $OPENSHIFT_DIR
tar xvzf /tmp/openshift-origin-server-v1.4.1-3f9807a-linux-64bit.tar.gz -C /tmp/openshift

### Start OpenShift cluster

if [ "${DOCKERIZED}" == "FALSE" ]; then
    cd $OPENSHIFT_DIR
    $OPENSHIFT_DIR/openshift-origin-server-v1.4.1+3f9807a-linux-64bit/openshift start
else
    $OC cluster up --metrics
fi