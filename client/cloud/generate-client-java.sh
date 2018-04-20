#!/usr/bin/env bash

###############################################################################
# Copyright (c) 2016, 2017 Red Hat Inc and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
###############################################################################

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
API_ADDRESS=`oc get service | grep api | awk '{print $2}'`
docker run -it --net=host -v $SCRIPT_DIR/kapua-java:/local swaggerapi/swagger-codegen-cli generate  -i http://${API_ADDRESS}:8080/v1/swagger.json -l java -o /local --api-package=org.eclipse.kapua.client.java --artifact-id=kapua-client-java --group-id=org.eclipse.kapua
sudo chown $USER kapua-java -R