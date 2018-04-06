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

ENV BROKER_ADDR broker
ENV BROKER_PORT 1883

ENV SQL_DB_ADDR db
ENV SQL_DB_PORT 3306

ENV SERVICE_BROKER_ADDR failover:(amqp://events-broker:5682)?jms.sendTimeout=1000

ENV JAVA_OPTS -Dcommons.db.schema.update=true \
              -Dcommons.db.connection.host=${SQL_DB_ADDR} \
              -Dcommons.db.connection.port=${SQL_DB_PORT} \
              -Dbroker.host=${BROKER_ADDR} \
              -Dbroker.port=${BROKER_PORT} \
              -Dkapua.errorProcessor.messageSource.amqp.host=${BROKER_ADDR} \
              -Dcommons.eventbus.url="${SERVICE_BROKER_ADDR}"
EXPOSE 8080

ENTRYPOINT /run-error-broker
