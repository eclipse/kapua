###############################################################################
# Copyright (c) 2017, 2020 Red Hat Inc and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Red Hat Inc - initial API and implementation
#     Eurotech
###############################################################################

FROM jboss/keycloak:7.0.0

COPY run-keycloak /opt/jboss/run-keycloak

USER 0

RUN chown -R jboss:0 /opt/jboss && \
    chmod -R g=u /opt/jboss

USER jboss

VOLUME /opt/jboss/keycloak/standalone/data

ENTRYPOINT ["/opt/jboss/run-keycloak"]
