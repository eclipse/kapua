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

FROM registry.access.redhat.com/ubi9 AS ubi-micro-build
RUN mkdir -p /mnt/rootfs && \
    dnf install --installroot /mnt/rootfs curl coreutils --releasever 9 --setopt install_weak_deps=false --nodocs -y && \
    dnf --installroot /mnt/rootfs clean all


FROM keycloak/keycloak:21.1

COPY --from=ubi-micro-build /mnt/rootfs /

COPY ../entrypoint/run-keycloak /opt/run-keycloak

USER 0

VOLUME /opt/keycloak/keycloak/standalone/data

ENTRYPOINT ["/opt/run-keycloak"]
