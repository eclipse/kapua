#!/usr/bin/env bash
#*******************************************************************************
# Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Eurotech - initial API and implementation
#*******************************************************************************
echo 'remove old symbolic link to console web application'
rm /var/lib/jetty/webapps/admin.war

echo 'remove old symbolic link to api web application'
rm /var/lib/jetty/webapps/api.war

echo 'create symbolic link to console web application'
ln -s /kapua/console/web/target/admin.war /var/lib/jetty/webapps/admin.war

echo 'create symbolic link to api web application'
ln -s /kapua/rest-api/web/target/api.war /var/lib/jetty/webapps/api.war
