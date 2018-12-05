#!/bin/sh
#*******************************************************************************
# Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
#*******************************************************************************
export ACTIVEMQ_OPTS="-Dcommons.db.schema.update=true -Dcertificate.jwt.private.key=file:///home/vagrant/key.pk8 -Dcertificate.jwt.certificate=file:///home/vagrant/cert.pem"
export CATALINA_OPTS="-Dcommons.db.schema.update=true -Dcertificate.jwt.private.key=file:///home/vagrant/key.pk8 -Dcertificate.jwt.certificate=file:///home/vagrant/cert.pem"
export JETTY_OPTS="-Dcommons.db.schema.update=true -Dcertificate.jwt.private.key=file:///home/vagrant/key.pk8 -Dcertificate.jwt.certificate=file:///home/vagrant/cert.pem -Djetty.base=/var/lib/jetty"
