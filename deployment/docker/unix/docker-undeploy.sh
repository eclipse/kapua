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

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

docker_common() {
    #shellcheck source=./docker-common.sh
    . "${SCRIPT_DIR}"/docker-common.sh
}

docker_undeploy() {
    docker-compose -f "${SCRIPT_DIR}"/../compose/docker-compose.yml down
}

docker_common

echo "Undeploying Eclipse Kapua..."
docker_undeploy || { echo "Undeploying Eclipse Kapua... ERROR!"; exit 1; }
echo "Undeploying Eclipse Kapua... DONE!"
