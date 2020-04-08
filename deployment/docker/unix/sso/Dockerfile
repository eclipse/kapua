################################################################################
#    Copyright (c) 2020 Eurotech and/or its affiliates and others
#
#    All rights reserved. This program and the accompanying materials
#    are made available under the terms of the Eclipse Public License v1.0
#    which accompanies this distribution, and is available at
#    http://www.eclipse.org/legal/epl-v10.html
#
#    Contributors:
#        Eurotech
################################################################################
FROM kapua/kapua-console:latest

USER 0
COPY tls.crt /tmp/tls.crt
RUN keytool -noprompt  -importcert -file "/tmp/tls.crt" -alias "ssocert" -keystore "${JAVA_HOME}/lib/security/cacerts" -storepass "changeit"
USER 1000
