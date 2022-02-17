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
# This script destroys the Minishift VM
#

echo "Deleting Minishift instance..."
echo "You can optionally skip the deletion of the Minishift VM if you want to reuse it..."
minishift delete
echo "Deleting Minishift instance... DONE!"
