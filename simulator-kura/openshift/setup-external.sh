#!/usr/bin/env bash

###############################################################################
# Copyright (c) 2017 Red Hat Inc
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
###############################################################################

set -e

: ${OPENSHIFT_PROJECT_NAME:=kura-simulator}
: ${DOCKER_HUB_ACCOUNT:=ctron}
: ${BROKER_URL:=tcp://localhost:1883}

# Set up new simulator instance

oc new-app "$DOCKER_HUB_ACCOUNT/kura-simulator:0.1.2" -n "$OPENSHIFT_PROJECT_NAME" \
  "-eKSIM_BROKER_URL=${BROKER_URL}" \
  -eKSIM_NAME_FACTORY=host:addr \
  -eKSIM_NUM_GATEWAYS=10

oc patch dc/kura-simulator -n "$OPENSHIFT_PROJECT_NAME" -p '{"spec":{"strategy":{"type":"Recreate"}}}'
