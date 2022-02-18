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

: ${OPENSHIFT_PROJECT_NAME:=kura-simulator}
: ${DOCKER_HUB_ACCOUNT:=ctron}
: ${BROKER_URL:=tcp://kapua-broker:kapua-password@localhost:1883}
: ${IMAGE_VERSION:=0.1.2}

# Set up new simulator instance

oc new-app -n "$OPENSHIFT_PROJECT_NAME" -f external-template.yml -p "DOCKER_ACCOUNT=$DOCKER_HUB_ACCOUNT" -p "BROKER_URL=$BROKER_URL" -p "IMAGE_VERSION=$IMAGE_VERSION"

