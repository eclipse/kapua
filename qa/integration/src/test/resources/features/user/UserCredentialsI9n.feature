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
@user
@userCredentials
@integration

Feature: Feature file for testing Password user credential
  This feature file provides test scenarios for user password credential.

  Scenario: Create a valid user with valid password credential
    Creating a new user "kapua-a" in kapua-sys account with valid password credential.
    After that trying to login as "kapua-a" user.
    No exception should be thrown.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Then I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    Then I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then No exception was thrown

  Scenario: Create a user with too short password credential
    Create a new user "kapua-a" in kapua-sys account with too short password credential.
    An exception should be thrown.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Then I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I expect the exception "KapuaIllegalArgumentException" with the text "*"
    Then I add credentials
      | name    | password    | enabled |
      | kapua-a | Password12@ | true    |
    And An exception was thrown
    Then I add credentials
      | name    | password     | enabled |
      | kapua-a | Password12@3 | true    |
    And I logout
    Then I login as user with name "kapua-a" and password "Password12@3"
    And No exception was thrown

  Scenario: Create a user with password credential that does not contain a special character
    Create a new user "kapua-a" in kapua-sys account with password credential that does not contain a special character.
    An exception should be thrown.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Then I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I expect the exception "KapuaIllegalArgumentException" with the text "*"
    Then I add credentials
      | name    | password            | enabled |
      | kapua-a | ToManySecrets123   | true    |
    And An exception was thrown
    Then I add credentials
      | name    | password            | enabled |
      | kapua-a | ToManySecrets123#   | true    |
    And I logout
    Then I login as user with name "kapua-a" and password "ToManySecrets123#"
    And No exception was thrown

####   This scenario is currently failing. It will be uncommented when issue #(2842) is resolved.

#  Scenario: Create a user with password credential with over 255 character
#  Creating new user "kapua-a" from kapua-sys parent account with password credential that contain over 255 character.
#  An exception should be thrown.
#
#    When I login as user with name "kapua-sys" and password "kapua-password"
#    Then I select account "kapua-sys"
#    And A generic user
#      | name    | displayName  | email             | phoneNumber     | status  | userType |
#      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
#    But I expect the exception "KapuaIllegalArgumentException" with the text "*"
#    Then I add credentials
#      | name    | password            | enabled |
#      | kapua-a | ToManySecrets123#ToManySecrets123#ToManySecrets123#ToManySecrets123#ToManySecrets123#ToManySecrets123#ToManySecrets123#ToManySecrets123#ToManySecrets123#ToManySecrets123#ToManySecrets123#ToManySecrets123#ToManySecrets123#ToManySecrets123#ToManySecrets123#T   | true    |
#    And An exception was thrown
#    And I logout

  Scenario: Create a user with password credential that does not contain upper case character
    Create a new user "kapua-a" from kapua-sys parent account with password credential that does not contain upper case character.
    An exception should be thrown.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Then I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I expect the exception "KapuaIllegalArgumentException" with the text "*"
    Then I add credentials
      | name    | password            | enabled |
      | kapua-a | tomanyecrets123#   | true    |
    And An exception was thrown
    Then I add credentials
      | name    | password            | enabled |
      | kapua-a | ToManySecrets123#   | true    |
    And I logout
    Then I login as user with name "kapua-a" and password "ToManySecrets123#"
    And No exception was thrown

  Scenario: Create a user with password credential that does not contain lower case character
    Create a new user "kapua-a" in kapua-sys parent account with password credential that does not contain lower case character.
    An exception should be thrown.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Then I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I expect the exception "KapuaIllegalArgumentException" with the text "*"
    Then I add credentials
      | name    | password            | enabled |
      | kapua-a | TOMANYSECRETS123#   | true    |
    And An exception was thrown
    Then I add credentials
      | name    | password            | enabled |
      | kapua-a | ToManySecrets123#   | true    |
    And I logout
    Then I login as user with name "kapua-a" and password "ToManySecrets123#"
    And No exception was thrown


  Scenario: Create a user with password credential that does not contain a number
    Create a new user "kapua-a" in kapua-sys parent account with password credential that does contain a number.
    An exception should be thrown.

    When I login as user with name "kapua-sys" and password "kapua-password"
    Then I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I expect the exception "KapuaIllegalArgumentException" with the text "*"
    Then I add credentials
      | name    | password         | enabled |
      | kapua-a | ToManySecrets#   | true    |
    And An exception was thrown
    Then I add credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets1#   | true    |
    And I logout
    Then I login as user with name "kapua-a" and password "ToManySecrets1#"
    And No exception was thrown

  Scenario: Create two users with the same password
    Create a new user "kapua-a" in kapua-sys account with valid a password (e.g "ToManySecrets123#"),
    and after that create a second user "kapua-b" with the same password.
    No exceptions should be thrown.

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-b | Kapua User b | kapua_b@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled |
      | kapua-b | ToManySecrets123# | true    |
    Then I logout
    And I login as user with name "kapua-a" and password "ToManySecrets123#"
    Then No exception was thrown
    And I logout
    And I login as user with name "kapua-b" and password "ToManySecrets123#"
    Then No exception was thrown

  Scenario: Creating user with an expiration date in the past for the password credential
    Create a new user "kapua-a" in kapua-sys account with valid a password (e.g "ToManySecrets123#")
    with expiration date in the past.
    An exception should be thrown when user would try to login

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | expirationDate |
      | kapua-a | ToManySecrets123# | yesterday      |
    Then I logout
    And I expect the exception "KapuaAuthenticationException" with the text "kapua-a"
    Then I login as user with name "kapua-a" and password "ToManySecrets123#"
    And An exception was thrown

  Scenario: Creating user with an expiration date in present for the password credential
    Create a new user "kapua-a" in kapua-sys account with valid a password (e.g "ToManySecrets123#")
    with expiration date in the present.
    An exception should be thrown when user would try to login

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled | expirationDate |
      | kapua-a | ToManySecrets123# | true    | today          |
    Then I logout
    And I expect the exception "KapuaAuthenticationException" with the text "kapua-a"
    Then I login as user with name "kapua-a" and password "ToManySecrets123#"
    And An exception was thrown

  Scenario: Creating user with an expiration date in future for the password credential
    Create a new user "kapua-a" in kapua-sys account with valid a password (e.g "ToManySecrets123#")
    with expiration date in the future.
    An exception should be thrown when user would try to login

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled | expirationDate |
      | kapua-a | ToManySecrets123# | true    | tomorrow       |
    Then I logout
    Then I login as user with name "kapua-a" and password "ToManySecrets123#"

  Scenario: Creating a user with valid password credential with its status set to DISABLED
    Create a new user "kapua-a" in kapua-sys account with valid a password (e.g "ToManySecrets123#")
    with its status e.g "DISABLED"
    An exception should be thrown when user would try to login

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | false   |
    Then I logout
    And I expect the exception "KapuaAuthenticationException" with the text "kapua-a"
    Then I login as user with name "kapua-a" and password "ToManySecrets123#"
    And An exception was thrown

  Scenario: Creating a user with valid password credential with expiration date in the past and status set to DISABLED
    Create a new user "kapua-a" in kapua-sys account with valid a password (e.g "ToManySecrets123#") in the past and
    with status e.g "DISABLED"
    An exception should be thrown when user would try to login

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled | expirationDate |
      | kapua-a | ToManySecrets123# | false   | yesterday      |
    Then I logout
    And I expect the exception "KapuaAuthenticationException" with the text "kapua-a"
    Then I login as user with name "kapua-a" and password "ToManySecrets123#"
    And An exception was thrown

  Scenario: Creating a user with valid password credential with expiration date in the present and status set to DISABLED
    Create a new user "kapua-a" in kapua-sys account with valid a password (e.g "ToManySecrets123#") in the present and
    with status e.g "DISABLED"
    An exception should be thrown when user would try to login

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled | expirationDate |
      | kapua-a | ToManySecrets123# | false   | today          |
    Then I logout
    And I expect the exception "KapuaAuthenticationException" with the text "kapua-a"
    Then I login as user with name "kapua-a" and password "ToManySecrets123#"
    And An exception was thrown

  Scenario: Creating a user with valid password credential with expiration date in the future and status set to DISABLED
    Create a new user "kapua-a" in kapua-sys account with valid a password (e.g "ToManySecrets123#") in the future and
    with status e.g "DISABLED"
    An exception should be thrown when user would try to login

    When I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User a | kapua_a@kapua.com | +386 31 321 123 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled | expirationDate |
      | kapua-a | ToManySecrets123# | false   | tomorrow       |
    Then I logout
    And I expect the exception "KapuaAuthenticationException" with the text "kapua-a"
    Then I login as user with name "kapua-a" and password "ToManySecrets123#"
    And An exception was thrown