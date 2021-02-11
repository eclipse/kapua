#!/usr/bin/env bash
###############################################################################
# Copyright (c) 2016, 2021 Red Hat Inc and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Red Hat Inc - initial API and implementation
#     Eurotech
###############################################################################

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

docker_common() {
    #shellcheck source=./docker-common.sh
    . "${SCRIPT_DIR}"/docker-common.sh
}

docker_compose() {
    declare -a COMPOSE_FILES;

    if [[ -n "${KAPUA_BROKER_DEBUG_PORT}" ]]; then
        if [[ "${KAPUA_BROKER_DEBUG_SUSPEND}" == "true" ]]; then
            KAPUA_BROKER_DEBUG_SUSPEND="y"
        else
            KAPUA_BROKER_DEBUG_SUSPEND="n"
        fi
        COMPOSE_FILES+=(-f "${SCRIPT_DIR}/../compose/extras/docker-compose.broker-debug.yml")
    fi

    if [[ -n "${KAPUA_ELASTICSEARCH_DATA_DIR}" ]]; then
        COMPOSE_FILES+=(-f "${SCRIPT_DIR}/../compose/extras/docker-compose.es-storage-dir.yml")
    fi

    docker-compose -f "${SCRIPT_DIR}/../compose/docker-compose.yml" "${COMPOSE_FILES[@]}" up -d
}

check_if_docker_logs() {
    if [[ "$1" == '--logs' ]]; then
        #shellcheck source=./docker-logs.sh
        . "${SCRIPT_DIR}/docker-logs.sh"
    else
        echo "Unrecognised parameter: ${1}"
        print_usage_deploy
    fi
}

print_usage_deploy() {
    echo "Usage: $(basename "$0") [--logs]" >&2
}

docker_common

echo "Deploying Eclipse Kapua..."
docker_compose || {
    echo "Deploying Eclipse Kapua... ERROR!"
    exit 1
}


container=1
while ((container)); do
    echo "waiting for containers to start..."
    docker logs compose_kapua-api_1 >& docker-log.log
    if grep -q "org.eclipse.jetty.server.Server - Started" docker-log.log ; then
        container=0
    else
        sleep 5s
    fi
done
echo "Docker containers are up and running!"
rm docker-log.log

echo "Deploying Eclipse Kapua... DONE!"

if [[ -z "$1" ]]; then
    echo "Run \"docker-compose -f ${SCRIPT_DIR}/../compose/docker-compose.yml logs -f\" for container logs"
else
    check_if_docker_logs "$1"
fi
