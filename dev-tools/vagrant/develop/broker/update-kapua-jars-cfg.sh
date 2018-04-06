#!/usr/bin/env bash
#*******************************************************************************
# Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
#*******************************************************************************

BROKER_ASSEMBLY_DIR="/kapua/assembly/broker";
BROKER_PLUGIN_DEPENDENCY_DIR="/kapua/broker/plugin/target/dependency";
BROKER_INSTALLATION_DIR="/usr/local/activemq";

echo "Cleanup the symbolic links to Kapua jars..."
for name in $(find lib/extra -type l);
    do
        echo "    Remove symbolic link from ./${name}";
        rm ./${name};
    done;
echo "Cleanup the symbolic links to Kapua jars... DONE!"

echo "Create the symbolic links to Kapua jars..."

echo "    Copy dependencies for broker-core..."
for name in $(ls  ${BROKER_PLUGIN_DEPENDENCY_DIR} | grep -Ev 'qa|jaxb-|activemq-|kapua-');
    do
        echo "        Create symbolic link from ./lib/extra/${name}  ${BROKER_PLUGIN_DEPENDENCY_DIR}/${name}";
        ln -s  ${BROKER_PLUGIN_DEPENDENCY_DIR}/${name} ./lib/extra/${name};
    done;
echo "    Copy dependencies for broker-core... DONE!"

echo '    Copy Kapua modules...'
for name in $(find /kapua -name 'kapua-*.jar' | grep target | grep -Ev 'qa|bin|test|console|WEB-INF|dependency|mysql|assembly|dev-tool|job|scheduler|broker-core|broker-client|datastore-int|datastore-client|transport|translator|simulator');
    do
        jar_name=$(echo - ${name} - ${name} | awk -F"/" '{print $NF}');
        echo "        Create symbolic link from ./lib/extra/${jar_name} ${name}";
        ln -s ${name} ./lib/extra/${jar_name};
    done;
echo '    Copy Kapua modules... DONE'

echo "Create the symbolic links to Kapua jars... Done!"

echo "Remove old config links..."
rm ${BROKER_INSTALLATION_DIR}/conf/activemq.xml
rm ${BROKER_INSTALLATION_DIR}/conf/camel.xml
rm ${BROKER_INSTALLATION_DIR}/conf/camel-routes.xml
rm ${BROKER_INSTALLATION_DIR}/conf/locator.xml
rm ${BROKER_INSTALLATION_DIR}/conf/logback.xml
echo "Remove old config links... DONE!"

echo "Create new config links..."
ln -s ${BROKER_ASSEMBLY_DIR}/configurations/activemq.xml      ${BROKER_INSTALLATION_DIR}/conf/activemq.xml
ln -s ${BROKER_ASSEMBLY_DIR}/configurations/camel.xml         ${BROKER_INSTALLATION_DIR}/conf/camel.xml
ln -s ${BROKER_ASSEMBLY_DIR}/configurations/camel-routes.xml  ${BROKER_INSTALLATION_DIR}/conf/camel-routes.xml
ln -s ${BROKER_ASSEMBLY_DIR}/configurations/locator.xml       ${BROKER_INSTALLATION_DIR}/conf/locator.xml
ln -s ${BROKER_ASSEMBLY_DIR}/configurations/logback.xml       ${BROKER_INSTALLATION_DIR}/conf/logback.xml
echo "Create new config links... DONE!"