#!/usr/bin/env bash
###############################################################################
# Copyright (c) 2016, 2018 Red Hat Inc and others
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

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. ${SCRIPT_DIR}/docker-common.sh

echo "Undeploying Eclipse Kapua..."

if [ "$1" == 'HONO' ]; then
    docker-compose -f ${SCRIPT_DIR}/compose/docker-compose-hono.yml down
else
    docker-compose -f ${SCRIPT_DIR}/compose/docker-compose.yml down
fi

echo "Undeploying Eclipse Kapua... DONE!"