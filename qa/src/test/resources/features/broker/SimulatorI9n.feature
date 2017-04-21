Feature: Tests with simulator

Scenario: Starting and stopping the simulator should create a device entry and properly set its status
  This starts and stops a simulator instance and checks if the connection state
  is recorded properly.

  When I start the simulator named sim-1 for account kapua-sys connecting to: tcp://kapua-broker:kapua-password@localhost:1883
  And I wait 5 seconds
  Then Device sim-1 for account kapua-sys is registered
  
  When I stop the simulator named sim-1
  And I wait 5 seconds
  Then Device sim-1 for account kapua-sys is not registered
