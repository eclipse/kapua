###############################################################################
# Copyright (c) 2011, 2016 Red Hat and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
###############################################################################

FROM java:8

MAINTAINER Henryk Konsek <hekonsek@gmail.com>

RUN apt-get update -qq
RUN apt-get install -qq maven

# Download Maven project
RUN apt-get install -qq git
RUN git clone https://github.com/eclipse/kapua.git
WORKDIR kapua/dev-tools
RUN git checkout develop

# Use Liquibase changelog from current build, not downloaded version
ADD liquibase.sql /root/kapua/dev-tools/src/main/database/

# Cache Maven dependencies
RUN mvn clean install liquibase:help
# Prepare final command
CMD ["/usr/share/maven/bin/mvn", "-Pliquibase"]