###############################################################################
# Copyright (c) 2011, 2016 Red Hat and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
###############################################################################

FROM java:8

MAINTAINER Henryk Konsek <hekonsek@gmail.com>

ENV DATA_DIR /opt/h2-data

RUN curl http://www.h2database.com/h2-2016-10-31.zip -o h2-2016-10-31.zip
RUN unzip h2-2016-10-31.zip -d /opt/
RUN rm h2-2016-10-31.zip
RUN mkdir -p ${DATA_DIR}

VOLUME ${DATA_DIR}

EXPOSE 8181 1521

CMD java -cp /opt/h2/bin/h2*.jar org.h2.tools.Server \
  -web -webAllowOthers -webPort 8181 -tcp -tcpAllowOthers -tcpPort 1521 \
  -baseDir ${DATA_DIR}