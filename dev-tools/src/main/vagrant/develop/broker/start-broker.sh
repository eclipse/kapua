#*******************************************************************************
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
#*******************************************************************************
# Kapua jars and activemq.xml need to be added before starting the activemq instance...
cd /usr/local/artemis/apache-artemis-ARTEMIS_VERSION/kapua
./bin/artemis-service start
cd /usr/local/activemq/apache-activemq-ACTIVEMQ_VERSION
./update-kapua-jars-cfg.sh
export ACTIVEMQ_OPTS="${ACTIVEMQ_OPTS} -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=*"
bin/activemq start xbean:conf/activemq.xml
