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

FROM @docker.account@/java-base

COPY maven /

USER 0

RUN useradd -u 1000 -g 0 -d '/var/opt/jetty' -s '/sbin/nologin' jetty && \
    mkdir -p /opt/jetty /var/opt/jetty/lib/ext /var/opt/jetty/start.d /var/opt/jetty/tls && \
    curl -Ls @jetty.url@ -o /tmp/jetty.tar.gz && \
    tar --strip=1 -xzf /tmp/jetty.tar.gz -C /opt/jetty && \
    rm -f /tmp/jetty.tar.gz && \
    cd /var/opt/jetty && \
    java -jar /opt/jetty/start.jar --approve-all-licenses --create-startd --add-to-start=http,https,jsp,jstl,websocket,deploy,logging-logback,jmx,ssl,stats && \
    chown -R 1000:0 /opt/jetty /var/opt/jetty && \
    chmod -R g=u /opt/jetty /var/opt/jetty

WORKDIR /var/opt/jetty

EXPOSE 8080
EXPOSE 8443

USER 1000

ENTRYPOINT /var/opt/jetty/run-jetty
