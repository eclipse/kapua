###############################################################################
# Copyright (c) 2017 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
Feature: Job service CRUD tests
    The Job service is responsible for executing scheduled actions on various targets.

Scenario: Regular job creation

    Given A regular job creator with the name "TestJob"
    When I create a new job entity from the existing creator
