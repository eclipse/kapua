###############################################################################
# Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
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

# Generate X509 certificate and private key
RUN mkdir -p /etc/opt/kapua && \
    openssl req -x509 -newkey rsa:4096 -keyout /etc/opt/kapua/key.pem -out /etc/opt/kapua/cert.pem -days 365 -nodes -subj '/O=Eclipse Kapua/C=XX' && \
    openssl pkcs8 -topk8 -in /etc/opt/kapua/key.pem -out /etc/opt/kapua/key.pk8 -nocrypt && \
    rm /etc/opt/kapua/key.pem

EXPOSE 8778
