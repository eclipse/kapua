###############################################################################
# Copyright (c) 2016, 2017 Red Hat Inc and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
###############################################################################

FROM fedora:25

MAINTAINER Henryk Konsek <hekonsek@gmail.com>

RUN dnf -y install maven git-core

# Download Maven project
RUN git clone -b develop https://github.com/eclipse/kapua.git

# Use Liquibase changelog from current build, not downloaded version
ADD liquibase.sql /kapua/dev-tools/src/main/database/

# Cache Maven dependencies
RUN mvn -Dmaven.repo.local=/m2-repo -B -f/kapua/dev-tools/pom.xml clean install liquibase:help \
  && chmod a+rw -R /m2-repo /kapua

# Prepare final command
ENTRYPOINT mvn -B -Dmaven.repo.local=/m2-repo -Pliquibase -fkapua/dev-tools/pom.xml
