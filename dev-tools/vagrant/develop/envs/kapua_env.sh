#!/bin/sh
#*******************************************************************************
# Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Eurotech - initial API and implementation
#*******************************************************************************
export ACTIVEMQ_OPTS="-Dcommons.db.schema.update=true -Dcertificate.jwt.private.key=file:///home/vagrant/key.pk8 -Dcertificate.jwt.certificate=file:///home/vagrant/cert.pem"
export CATALINA_OPTS="-Dcommons.db.schema.update=true -Dcertificate.jwt.private.key=file:///home/vagrant/key.pk8 -Dcertificate.jwt.certificate=file:///home/vagrant/cert.pem"
export JETTY_OPTS="-Dcommons.db.schema.update=true -Dcertificate.jwt.private.key=file:///home/vagrant/key.pk8 -Dcertificate.jwt.certificate=file:///home/vagrant/cert.pem -Djetty.base=/var/lib/jetty"
