###############################################################################
# Copyright (c) 2018 Eurotech and/or its affiliates and others
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

FROM @docker.base.image@

ENV JAVA_HOME=/usr/lib/jvm/jre-openjdk

RUN yum install -y java-1.8.0-openjdk && \
    yum install -y curl && \
    yum install -y openssl && \
    mkdir -p /opt/jolokia && \
    curl -s @jolokia.agent.url@ -o /opt/jolokia/jolokia-jvm-agent.jar

EXPOSE 8778
