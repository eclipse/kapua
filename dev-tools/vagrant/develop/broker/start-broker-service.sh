#!/usr/bin/env bash
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

# Kapua jars and activemq.xml need to be added before starting the activemq instance...
bin/activemq start -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=* xbean:conf/activemq.xml
