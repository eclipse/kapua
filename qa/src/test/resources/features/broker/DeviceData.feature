Feature: Device data scenarios

Scenario: Connect to the system and publish some data

  Given The account name is kapua-sys and the client ID is sim-1
    And The broker URI is tcp://kapua-broker:kapua-password@localhost:1883
    And I have a mock data application named "my-app-1"
  
  When I login as user with name "kapua-sys" and password "kapua-password"
  And I start the simulator 
  
  Then Device sim-1 for account kapua-sys is registered after 5 seconds
   And I expect the device to report the applications
    | DEPLOY-V2 |
    | CMD-V1 |
    | my-app-1 |
  
  Given I publish for the application "my-app-1"
  
  When I publish on the opic "my-topic-1/data" timestamped now
    | key | type | value |
    | foo.string | STRING | bar |
  And  I publish on the opic "my-topic-2/data" timestamped now
    | key | type | value |
    | foo.int32 | INT32 | 123 |
  And  I publish on the opic "my-topic-3/data" timestamped now
    | key | type | value |
    | foo.int64 | INT64 | 456 |
  And  I publish on the opic "my-topic-4/data" timestamped now
    | key | type | value |
    | foo.double | DOUBLE | 42.42 |
  And  I publish on the opic "my-topic-5/data" timestamped now
    | key | type | value |
    | foo.boolean| BOOLEAN | true |
  
  And  I wait 5 seconds for the broker to process its data
  And  I refresh all indices
  
  Then I expect the number of messages for this device to be 5
  And  I expect the latest captured message on channel "my-app-1/my-topic-5/data" to have the metrics
    | key | type | value |
    | foo.boolean | BOOLEAN | true |
  
  When I stop the simulator
  Then Device sim-1 for account kapua-sys is not registered after 5 seconds
