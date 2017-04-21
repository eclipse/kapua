Feature: Device lifecycle scenarios

Scenario: Starting and stopping the simulator should create a device entry and properly set its status
  This starts and stops a simulator instance and checks if the connection state
  is recorded properly.

  Given The account name is kapua-sys and the client ID is sim-1

  When I start the simulator connecting to: tcp://kapua-broker:kapua-password@localhost:1883
  And I wait 5 seconds
  Then Device sim-1 for account kapua-sys is registered
  And Device sim-1 for account kapua-sys should report simulator device information
  
  When I fetch the bundle states
  Then The bundle org.eclipse.kura.api with version 2.1.0 is present and ACTIVE
  And  The bundle org.eclipse.kura.unresolved with version 2.1.2 is present and INSTALLED
  And  The bundle org.eclipse.kura.unstarted with version 2.1.1 is present and RESOLVED
  
  When I stop the bundle org.eclipse.kura.api with version 2.1.0
  And I fetch the bundle states
  Then The bundle org.eclipse.kura.api with version 2.1.0 is present and RESOLVED
  
  When I start the bundle org.eclipse.kura.api with version 2.1.0
  And I fetch the bundle states
  Then The bundle org.eclipse.kura.api with version 2.1.0 is present and ACTIVE
  
  When I stop the simulator
  And I wait 5 seconds
  Then Device sim-1 for account kapua-sys is not registered
