#!/usr/bin/env bash
###############################################################################
# Copyright (c) 2016, 2020 Red Hat Inc and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Red Hat Inc - initial API and implementation
#     Eurotech
###############################################################################

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
. ${SCRIPT_DIR}/docker-common.sh

echo "Deploying Eclipse Kapua..."
if [ -n "${KAPUA_BROKER_DEBUG_PORT}" ]; then
  if [ "${KAPUA_BROKER_DEBUG_SUSPEND}" == "true" ]; then
    KAPUA_BROKER_DEBUG_SUSPEND="y"
  else
    KAPUA_BROKER_DEBUG_SUSPEND="n"
  fi
  docker-compose -f ${SCRIPT_DIR}/../compose/docker-compose.yml -f ${SCRIPT_DIR}/../compose/docker-compose.broker-debug.yml up -d
else
  docker-compose -f ${SCRIPT_DIR}/../compose/docker-compose.yml up -d
fi
echo "Deploying Eclipse Kapua... DONE!"
echo "Run \"docker-compose -f ${SCRIPT_DIR}/../compose/docker-compose.yml logs -f\" for container logs"
