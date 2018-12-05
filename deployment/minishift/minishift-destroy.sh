#!/usr/bin/env bash
###############################################################################
# Copyright (c) 2018 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
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