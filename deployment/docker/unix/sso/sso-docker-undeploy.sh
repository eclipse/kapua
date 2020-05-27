#!/usr/bin/env bash
###############################################################################
# Copyright (c) 2016, 2020 Red Hat Inc and others
#
# All rights reserved. This program and the accompanying materials are made
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

echo "Undeploying Eclipse Kapua..."

docker-compose -f ${SCRIPT_DIR}/../../compose/sso/sso-docker-compose.yml down
docker rmi "kapua/kapua-console:sso"
rm -R "${SSO_CRT_DIR}"

echo "Undeploying Eclipse Kapua... DONE!"
