#!/usr/bin/env bash

###############################################################################
# Copyright (c) 2017, 2022 Red Hat Inc
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
###############################################################################

set -e

: ${OPENSHIFT_PROJECT_NAME:=eclipse-kapua}
: ${DOCKER_HUB_ACCOUNT:=ctron}

# Set up new simulator instance

oc new-app "$DOCKER_HUB_ACCOUNT/kura-simulator:0.1.2" -n "$OPENSHIFT_PROJECT_NAME" \
  '-eKSIM_BROKER_HOST=${KAPUA_BROKER_SERVICE_HOST}' \
  '-eKSIM_BROKER_PORT=${KAPUA_BROKER_SERVICE_PORT}' \
  -eKSIM_NAME_FACTORY=host:addr \
  -eKSIM_NUM_GATEWAYS=10

oc patch dc/kura-simulator -n "$OPENSHIFT_PROJECT_NAME" -p '{"spec":{"strategy":{"type":"Recreate"}}}'
