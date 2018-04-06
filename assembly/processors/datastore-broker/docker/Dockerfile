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

ENV DATASTORE_ADDR es
ENV DATASTORE_PORT 9200
ENV DATASTORE_CLIENT org.eclipse.kapua.service.datastore.client.rest.RestDatastoreClient

ENV SQL_DB_ADDR db
ENV SQL_DB_PORT 3306

ENV SERVICE_BROKER_ADDR failover:(amqp://events-broker:5682)?jms.sendTimeout=1000

ENV JAVA_OPTS -Dcommons.db.schema.update=true \
              -Dcommons.db.connection.host=${SQL_DB_ADDR} \
              -Dcommons.db.connection.port=${SQL_DB_PORT} \
              -Dbroker.host=${BROKER_ADDR} \
              -Dbroker.port=${BROKER_PORT} \
              -Dkapua.datastoreProcessor.messageSource.amqp.host=${BROKER_ADDR} \
              -Dkapua.datastoreProcessor.errorTarget.amqp.host=${BROKER_ADDR} \
              -Ddatastore.elasticsearch.nodes=${DATASTORE_ADDR} \
              -Ddatastore.elasticsearch.port=${DATASTORE_PORT} \
              -Ddatastore.client.class=${DATASTORE_CLIENT} \
              -Dcommons.eventbus.url="${SERVICE_BROKER_ADDR}"

EXPOSE 8080

ENTRYPOINT /run-datastore-broker
