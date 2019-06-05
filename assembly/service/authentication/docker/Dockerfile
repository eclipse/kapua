###############################################################################
# Copyright (c) 2020 Eurotech and/or its affiliates and others
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

ENV BROKER_HOST=${BROKER_HOST:-broker}
ENV BROKER_PORT=${BROKER_PORT:-5672}
ENV BROKER_INTERNAL_USERNAME=${BROKER_INTERNAL_USERNAME:-internalUsername}
ENV BROKER_INTERNAL_PASSWORD=${BROKER_INTERNAL_PASSWORD:-internalPassword}

ENV SERVICE_BROKER_HOST=${SERVICE_BROKER_HOST:-events-broker}
ENV SERVICE_BROKER_PORT=${SERVICE_BROKER_PORT:-5672}
ENV SERVICE_BROKER_USERNAME=${SERVICE_BROKER_USERNAME:-kapua-sys}
ENV SERVICE_BROKER_PASSWORD=${SERVICE_BROKER_PASSWORD:-kapua-password}

ENV SQL_DB_ADDR db
ENV SQL_DB_PORT 3306

ENV SERVICE_BROKER_ADDR failover:(amqp://events-broker:5672)?jms.sendTimeout=1000

ENV JAVA_OPTS -Dcommons.db.schema.update=true \
              -Dcommons.db.connection.host=${SQL_DB_ADDR} \
              -Dcommons.db.connection.port=${SQL_DB_PORT} \
              -Dbroker.host=${BROKER_HOST} \
              -Dbroker.port=${BROKER_PORT} \
              -Dcommons.eventbus.url="${SERVICE_BROKER_ADDR}" \
              -Dcertificate.jwt.private.key=file:///etc/opt/kapua/key.pk8 \
              -Dcertificate.jwt.certificate=file:///etc/opt/kapua/cert.pem \
              -Dcommons.service_bus.host="${SERVICE_BROKER_HOST}" \
              -Dcommons.service_bus.port="${SERVICE_BROKER_PORT}" \
              -Dcommons.service_bus.username="${SERVICE_BROKER_USERNAME}" \
              -Dcommons.service_bus.password="${SERVICE_BROKER_PASSWORD}"

EXPOSE 8080

ENTRYPOINT /run-service-authentication
