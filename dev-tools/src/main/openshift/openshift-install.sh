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

# Configuration
OPENSHIFT_PROJECT_NAME="eclipse-kapua"
OPENSHIFT_DIR=/tmp/openshift

# Install and start OpenShift
mkdir -p $OPENSHIFT_DIR
curl -fsSL https://github.com/openshift/origin/releases/download/v1.4.1/openshift-origin-server-v1.4.1-3f9807a-linux-64bit.tar.gz | tar xvz -C /tmp/openshift
$OPENSHIFT_DIR/openshift-origin-server-v1.4.1+3f9807a-linux-64bit/openshift start > /dev/null 2>&1 &
sleep 5

# Create Kapua project
$OPENSHIFT_DIR/openshift-origin-server-v1.4.1+3f9807a-linux-64bit/oc login
$OPENSHIFT_DIR/openshift-origin-server-v1.4.1+3f9807a-linux-64bit/oc new-project "$OPENSHIFT_PROJECT_NAME" --description="Open source IoT Platform" --display-name="Eclipse Kapua"