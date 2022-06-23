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

FROM @docker.account@/java-base

COPY maven /

ENV DATASTORE_ADDR es:9200
ENV DATASTORE_CLIENT org.eclipse.kapua.service.elasticsearch.client.rest.RestElasticsearchClientProvider

ENV SQL_DB_ADDR db
ENV SQL_DB_PORT 3306

ENV SERVICE_BROKER_ADDR failover:(amqp://events-broker:5672)?jms.sendTimeout=1000

ENV JOB_ENGINE_BASE_ADDR http://job-engine:8080/v1

USER 0

RUN useradd -u 1002 -g 0 -d '/var/opt/activemq' -s '/sbin/nologin' activemq && \
    chown -R activemq:root /var/opt/activemq /opt/activemq && \
    chmod -R g=u /var/opt/activemq /opt/activemq

ENV ACTIVEMQ_OPTS "-Dcommons.db.schema.update=true \
                   -Dcommons.db.connection.host=\${SQL_DB_ADDR} \
                   -Dcommons.db.connection.port=\${SQL_DB_PORT} \
                   -Ddatastore.elasticsearch.nodes=\${DATASTORE_ADDR} \
                   -Ddatastore.elasticsearch.provider=\${DATASTORE_CLIENT} \
                   -Dcommons.eventbus.url=\${SERVICE_BROKER_ADDR} \
                   -Dcertificate.jwt.private.key=file:///etc/opt/kapua/key.pk8 \
                   -Dcertificate.jwt.certificate=file:///etc/opt/kapua/cert.pem \
                   -Dbroker.ip=broker \
                   -Djob.engine.base.url=\${JOB_ENGINE_BASE_ADDR} \
                   -Djob.engine.client.auth.mode=trusted \
                   -Dcipher.key=\${CIPHER_KEY} \
                   -Dcrypto.secret.key=\${CRYPTO_SECRET_KEY} \
                   -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=\${AMQ_SERIALIZABLE_PACKAGES:-java.lang,java.math,java.util,org.apache.activemq,org.fusesource.hawtbuf,org.eclipse.kapua}"

EXPOSE 1883 1893 8883 61614 61615 8161

VOLUME /opt/activemq/data

USER 1002

ENTRYPOINT /var/opt/activemq/run-broker
