###############################################################################
# Copyright (c) 2016, 2017 Red Hat Inc and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
###############################################################################

#!/usr/bin/env bash

set -e

# Configuration

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. $SCRIPT_DIR/openshift-common.sh

# Create Kapua project
$OC login
$OC new-project "$OPENSHIFT_PROJECT_NAME" --description="Open source IoT Platform" --display-name="Eclipse Kapua"