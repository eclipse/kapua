#!/usr/bin/env bash

###############################################################################
# Copyright (c) 2016, 2022 Red Hat Inc and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
###############################################################################

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
API_ADDRESS=`oc get service | grep api | awk '{print $2}'`
docker run -it --net=host -v $SCRIPT_DIR/kapua-java:/local swaggerapi/swagger-codegen-cli generate  -i http://${API_ADDRESS}:8080/v1/swagger.json -l java -o /local --api-package=org.eclipse.kapua.client.java --artifact-id=kapua-client-java --group-id=org.eclipse.kapua
sudo chown $USER kapua-java -R
