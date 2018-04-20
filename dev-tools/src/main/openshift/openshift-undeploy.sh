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

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. $SCRIPT_DIR/openshift-common.sh

  : OPENSHIFT_HOST=${OPENSHIFT_HOST:=localhost:8443}

echo Undeploying Eclipse Kapua from Openshift

# deleting entire project with related resources
$OC login $OPENSHIFT_HOST -u admin -p admin
oc delete project eclipse-kapua

echo Eclipse Kapua undeployed from Openshift