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

export OPENSHIFT_PROJECT_NAME="eclipse-kapua"

if which oc &>/dev/null; then
  echo Using "oc" from path
  export OC=oc
else
  export OPENSHIFT_DIR=/tmp/openshift
  export OC=${OPENSHIFT_DIR}/openshift-origin-server-v1.4.1+3f9807a-linux-64bit/oc
fi
