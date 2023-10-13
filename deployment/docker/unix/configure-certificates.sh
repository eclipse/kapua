#!/usr/bin/env bash
###############################################################################
# Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Eurotech
###############################################################################

set -eo pipefail

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

docker_common() {
    . "${SCRIPT_DIR}"/docker-common.sh
}

create_certificates() {
    if [[ ! -f "${KAPUA_CRT_DIR}/${KAPUA_CERT_FILE}" ]]; then
        openssl req -x509 -newkey rsa:4096 -keyout "${KAPUA_CRT_DIR}/${KAPUA_KEY_FILE}" -out "${KAPUA_CRT_DIR}/${KAPUA_CERT_FILE}" -days 365 -nodes -subj "/CN=Kapua"
    fi

    export KAPUA_CRT="${KAPUA_CRT:=$(cat "${KAPUA_CRT_DIR}"/"${KAPUA_CERT_FILE}")}"
    export KAPUA_KEY="${KAPUA_KEY:=$(cat "${KAPUA_CRT_DIR}"/"${KAPUA_KEY_FILE}")}"
    export KAPUA_CA="${KAPUA_CA:=$(cat "${KAPUA_CRT_DIR}"/"${KAPUA_CA_FILE}")}"
}

docker_common

echo "Creating Certificates..."
create_certificates || {
  echo "Creating Certificates... ERROR!";
  exit 1;
}
echo "Creating Certificates... DONE!"