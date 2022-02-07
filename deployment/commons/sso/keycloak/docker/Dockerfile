###############################################################################
# Copyright (c) 2017, 2022 Red Hat Inc and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Red Hat Inc - initial API and implementation
#     Eurotech
###############################################################################

FROM jboss/keycloak:7.0.0

COPY ../entrypoint/run-keycloak /opt/jboss/run-keycloak

USER 0

RUN chown -R jboss:0 /opt/jboss && \
    chmod -R g=u /opt/jboss

USER jboss

VOLUME /opt/jboss/keycloak/standalone/data

ENTRYPOINT ["/opt/jboss/run-keycloak"]
