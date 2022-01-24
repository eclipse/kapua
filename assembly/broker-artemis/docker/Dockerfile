###############################################################################
# Copyright (c) 2019, 2021 Eurotech and/or its affiliates and others
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

RUN useradd -u 1002 -g 0 -d '/opt/artemis' -s '/sbin/nologin' artemis && \
    chown -R artemis:root /opt/artemis && \
    chmod -R g=u /opt/artemis && \
    chmod -R +x /opt/artemis/bin

ENV BROKER_HOST=message-broker
ENV BROKER_ID=broker-id

EXPOSE 1883 1893 8883

USER 1002

ENTRYPOINT /opt/artemis/run-broker