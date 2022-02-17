#!/usr/bin/env bash
###############################################################################
# Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Eurotech- initial API and implementation
###############################################################################

#
# This script starts and configures the Minishift VM.
#
# Needs to be run before `minishift-deploy.sh` script.
#
# The Minishift VM starts by default with 6GB of memory and 3 available CPUs.
# Those values can be changed but this can be considered the minimum requirement to run Kapua on Minishift.
#
# After the Minishift VM starts local Docker images are copied into the Minishift Docker registry.
#

set -e

MINISHIFT_MEMORY=${MINISHIFT_MEMORY:=6GB}
MINISHIFT_CPUS=${MINISHIFT_CPUS:=3}

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo "Configuring 'vm-driver' property..."
minishift config set vm-driver virtualbox
echo "Configuring 'vm-driver' property... DONE"
echo ""

echo "Starting Minishift..."
minishift start --memory ${MINISHIFT_MEMORY} --cpus ${MINISHIFT_CPUS}
echo "Starting Minishift... DONE!"
echo ""

echo "Running Minishift Importer script..."
${SCRIPT_DIR}/minishift-importer.sh ||
    {
        echo "Running Minishift Importer script... FAILED!";

        ${SCRIPT_DIR}/minishift-destroy.sh
        exit 1;
    }
echo "Running Minishift Importer script... DONE!"
echo ""
