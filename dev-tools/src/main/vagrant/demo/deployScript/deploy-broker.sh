#*******************************************************************************
# Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
vagrant ssh -c "echo 'deploying the Kapua broker'
    cd /usr/local/kapua
    rm -rf apache-activemq*
    tar -xvzf /kapua/assembly/broker/target/kapua-broker*
    mv kapua-broker* apache-activemq-${ACTIVEMQ_VERSION}
    cd apache-activemq-${ACTIVEMQ_VERSION}
    sed -i -E 's/console(.*)ch.qos.logback.core.ConsoleAppender(.*)/file\1ch.qos.logback.core.FileAppender\2\n\t\t<file>data\/kapua.log<\/file>/' conf/logback.xml
    sed -i -E 's/<appender-ref ref=\"console\" \/>/<appender-ref ref=\"file\" \/>/' conf/logback.xml
    cd ..
    sudo chown -R vagrant:vagrant apache-activemq-${ACTIVEMQ_VERSION}"
