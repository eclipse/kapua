###############################################################################
# Copyright (c) 2017 Red Hat Inc and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Red Hat Inc - initial API and implementation
###############################################################################
Feature: Tests with simulator

Scenario:
  When I start the simulator named sim-1 for account kapua-sys connecting to: tcp://kapua-broker:kapua-password@localhost:1883
  And I wait 5 seconds
  Then Device sim-1 for account kapua-sys is registered
  
  When I stop the simulator named sim-1
  And I wait 5 seconds
  Then Device sim-1 for account kapua-sys is not registered
