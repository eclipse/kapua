#!/usr/bin/env bash

###############################################################################
# Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
#
###############################################################################

if [ -z "${DOCKER_ACCOUNT}" ]; then
  DOCKER_ACCOUNT='kapua'
fi

docker build -t ${DOCKER_ACCOUNT}/h2:0.2.0-SNAPSHOT .
docker push ${DOCKER_ACCOUNT}/h2:0.2.0-SNAPSHOT