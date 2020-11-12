#!/usr/bin/env bash
###############################################################################
# Copyright (c) 2016, 2020 Red Hat Inc and others
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

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. "${SCRIPT_DIR}/sso-docker-common.sh"

# Build the Keycloak image if missing
if [[ "$(docker images -q ${KEYCLOAK_IMAGE} 2> /dev/null)" == "" ]] ; then
  cd "${SCRIPT_DIR}/../../../commons/sso/keycloak/" || exit
  . "./build"
  cd "${SCRIPT_DIR}" || exit
fi

# If the certificate is missing, generate it (self-signed certificate) with a private key too
if [[ ! -s ${SSO_CRT} ]]; then
  echo "Generating certificate and private key..."
  if [[ ! -d ${SSO_CRT_DIR} ]]; then
    mkdir "${SSO_CRT_DIR}"
  fi
  openssl req -x509 -newkey rsa:4096 -keyout "${SSO_KEY}" -out "${SSO_CRT}" -days 365 -nodes -subj '/CN=localhost/O=Eclipse Kapua/C=XX' -reqexts SAN -extensions SAN -config <(cat /etc/ssl/openssl.cnf <(printf "[SAN]\nsubjectAltName=DNS:localhost,IP:127.0.0.1,IP:${EXTERNAL_IP}"))
  echo "Generating certificate and private key... DONE!"
fi

echo "Building Console with certificate..."
cp "${SSO_CRT}" .
docker build -t "${DOCKER_ACCOUNT}"/kapua-console:sso -f ./Dockerfile .
rm "${SSO_CERT_FILE}"
echo "Building Console with certificate... DONE!"

echo "Deploying Eclipse Kapua..."
docker-compose -f ${SCRIPT_DIR}/../../compose/sso/sso-docker-compose.yml up -d
echo "Deploying Eclipse Kapua... DONE!"
echo "Run \"docker-compose -f ${SCRIPT_DIR}/../../compose/sso/sso-docker-compose.yml logs -f\" for container logs"
