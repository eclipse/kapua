Feature: Device lifecycle scenarios

Scenario: Starting and stopping the simulator should create a device entry and properly set its status
  This starts and stops a simulator instance and checks if the connection state
  is recorded properly.

  Given The account name is kapua-sys and the client ID is sim-1
    And The broker URI is tcp://kapua-broker:kapua-password@localhost:1883
  
  When I login as user with name "kapua-sys" and password "kapua-password"

  When I start the simulator
  And I wait 5 seconds
  Then Device sim-1 for account kapua-sys is registered
  And The device should report simulator device information
  And I expect the device to report the applications
    | DEPLOY-V2 |
    | CMD-V1 |
  
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

Scenario: Installing a package
  Given The account name is kapua-sys and the client ID is sim-1
    And The broker URI is tcp://kapua-broker:kapua-password@localhost:1883
  
  When I login as user with name "kapua-sys" and password "kapua-password"
   And I start the simulator
   And I wait 5 seconds
  Then Device sim-1 for account kapua-sys is registered
  
  When I fetch the package states
  Then There must be no installed packages
  
  When I start to download package "foo.bar" with version 1.2.3 from http://127.0.0.1/foo.dp
   And I wait 2 seconds for the download to start
  Then The download is in status IN_PROGRESS
  
  When I wait 10 seconds more for the download to complete
  Then The download is in status COMPLETED
  
  When I fetch the package states
  Then Package "foo.bar" with version 1.2.3 is installed and has 10 mock bundles
  
  
