###############################################################################
# Copyright (c) 2020 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@sso
@integration

Feature: Sso Login using Keycloak Provider

  Scenario: Configure environment for SSO with the Keycloak provider
    Given Set environment variables for keycloak provider

  Scenario: Start Keycloak

    Given Pull image "jboss/keycloak:8.0.1"
    When Create network
    Then Start Keycloak container with name "keycloak"
    Then I wait 45 seconds
    And Initialize shiro ini file

  Scenario: Create external user using the the SimpleRegistrationProcessor
  Create a user using keycloak
  Login using jwt credentials with SimpleRegistrationProcessor
  Check if user exists

    Then Get access token for user with username "test-user" and password "TestPassword123#"
    And Create a jwt credential using the access token
    Given Configure the SSO service
    And Login using a jwt credential
    Then I logout
    Given I login as user with name "kapua-sys" and password "kapua-password"
    When I search for user with name "test-user"
    Then I find user
      | name      | email           | status  | userType | externalId                           |
      | test-user | test-user@co.co | ENABLED | EXTERNAL | 5698d2ff-d26e-45bf-92df-c3e88da58dad |
    And I logout

  Scenario: Create external user using the the UserService
  Login as kapua-sys, create user with externalId
  Login as external user using jwt credentials
  Check if user exists

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    Then Account
      | name      |
      | test-user |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    And A generic user
      | name      | email           | userType | externalId                           |
      | test-user | test-user@co.co | EXTERNAL | 5698d2ff-d26e-45bf-92df-c3e88da58dad |
    And I logout
    And Get access token for user with username "test-user" and password "TestPassword123#"
    And Create a jwt credential using the access token
    And Login using a jwt credential
    Then I logout
    And I login as user with name "kapua-sys" and password "kapua-password"
    Then I search for user with name "test-user"
    And I find user
      | name      | email           | userType | externalId                           |
      | test-user | test-user@co.co | EXTERNAL | 5698d2ff-d26e-45bf-92df-c3e88da58dad |

  Scenario: Using normal login using Keycloak credentials
  Create user using keycloak and login using SimpleRegistrationProcessor
  Login as external user using normal login method
  Exception should be thrown

    Then Get access token for user with username "test-user" and password "TestPassword123#"
    And Create a jwt credential using the access token
    Given Configure the SSO service
    And Login using a jwt credential
    Then I logout
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "test-user" and password "TestPassword123#"
    Then An exception was thrown

  Scenario: Login with an existing internal user using the SSO login
  Login as kapua-sys, create a regular user without externalId
  Try to login as a keycloak user with same username as previously created user
  Exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    Then Account
      | name      |
      | test-user |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When A generic user
      | name      | displayName | email           | phoneNumber     | status  | userType |
      | test-user | Test User   | test-user@co.co | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name      | password          | enabled |
      | test-user | ToManySecrets123# | true    |
    Then I logout
    Given I expect the exception "NullPointerException" with the text "*"
    Then Get access token for user with username "test-user" and password "ToManySecrets123#"
    Then An exception was thrown

  Scenario: Add user with fake externalId
  Login as kapua-sys, create an external user with made up externalId
  Try to login as created user using sso
  Exception should be thrown

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    Then Account
      | name      |
      | test-user |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    And A generic user
      | name      | email           | userType | externalId                           |
      | test-user | test-user@co.co | EXTERNAL | 1118d3dd-f11w-11bf-11de-c3e88da58abc |
    And I logout
    And Get access token for user with username "test-user" and password "TestPassword123#"
    And Create a jwt credential using the access token
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When Login using a jwt credential
    Then An exception was thrown

  Scenario: Logging out and logging back in with SSO
  Login as external user using SimpleRegistrationProcessor, logout
  Login again, no exception should be thrown

    Then Get access token for user with username "test-user" and password "TestPassword123#"
    And Create a jwt credential using the access token
    Given Configure the SSO service
    And Login using a jwt credential
    Then I logout
    And Login using a jwt credential
    Then No exception was thrown

  Scenario: Login using normal login after logging out with SSO
  Login as external user using SimpleRegistrationProcessor, logout
  Login again, this time with normal login method, exception should be thrown

    Then Get access token for user with username "test-user" and password "TestPassword123#"
    And Create a jwt credential using the access token
    Given Configure the SSO service
    And Login using a jwt credential
    Then I logout
    Given I expect the exception "KapuaAuthenticationException" with the text "*"
    When I login as user with name "test-user" and password "TestPassword123#"
    Then An exception was thrown

  Scenario: Stop Keycloak

    Then Stop container with name "keycloak"
    And Remove container with name "keycloak"
    Then Remove network
