#!/usr/bin/env bash
###############################################################################
# Copyright (c) 2016, 2019 Red Hat Inc and others
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

echo "Deploying Eclipse Kapua..."

docker-compose -f ${SCRIPT_DIR}/../../compose/sso/docker-compose.yml up -d

echo "Deploying Eclipse Kapua... DONE!"
echo "Run \"docker-compose -f ${SCRIPT_DIR}/../compose/sso/docker-compose.yml logs -f\" for container logs"