###############################################################################
# Copyright (c) 2016, 2017 Red Hat Inc and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
###############################################################################

FROM openjdk:8u111-jre-alpine

MAINTAINER Henryk Konsek <hekonsek@gmail.com>

ENV DATA_DIR /opt/h2-data

RUN adduser -D -g "Eclipse Kapua" kapua && \
    apk update && apk add curl && \
    curl -s https://repo1.maven.org/maven2/org/jolokia/jolokia-jvm/1.3.4/jolokia-jvm-1.3.4-agent.jar -o /jolokia-jvm-agent.jar && \
    mkdir -p "${DATA_DIR}" && \
    chown kapua:kapua "${DATA_DIR}" && chmod a+rw "${DATA_DIR}"

USER kapua

WORKDIR /home/kapua
RUN curl -s http://www.h2database.com/h2-2016-10-31.zip -o h2-2016-10-31.zip && \
  unzip h2-2016-10-31.zip && \
  rm h2-2016-10-31.zip

VOLUME ${DATA_DIR}

EXPOSE 8181 3306 8778

CMD java -javaagent:/jolokia-jvm-agent.jar \
  -cp /home/kapua/h2/bin/h2*.jar org.h2.tools.Server \
  -web -webAllowOthers -webPort 8181 -tcp -tcpAllowOthers -tcpPort 3306 \
  -baseDir "${DATA_DIR}"
