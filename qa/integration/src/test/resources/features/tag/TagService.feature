###############################################################################
# Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
#
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Eurotech
###############################################################################
@tag
@integration

Feature: Tag Service
  Tag Service is responsible for CRUD operations on Tags. This service is currently
  used to attach tags to Devices, but could be used to tag eny kapua entity, like
  User for example.

  Scenario: Creating Unique Tag Without Description
  Login as kapua-sys, go to tags, try to create a tag with unique name without description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag123" without description
    And Tag with name "Tag123" is searched
    Then I find a tag with name "Tag123"
    And I logout

  Scenario: Creating Non-unique Tag Without Description
  Login as kapua-sys, go to tags, try to create a tag with non-unique name without description.
  Kapua should return Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag123" without description
    Given I expect the exception "KapuaDuplicateNameException" with the text "An entity with the same name Tag123 already exists."
    When I create tag with name "Tag123" without description
    Then An exception was thrown
    Then I logout

  Scenario: Creating Tag With Short Name Without Description
  Login as kapua-sys, go to tags, try to create a tag with short name without description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "abc" without description
    And Tag with name "abc" is searched
    Then I find a tag with name "abc"
    And I logout

  Scenario: Creating Tag With Too Short Name Without Description
  Login as kapua-sys, go to tags, try to create a tag with too short name without description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Then I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument tagCreator.name: Value less than allowed min length. Min length is 3."
    When I create tag with name "a" without description
    Then An exception was thrown
    And I logout

  Scenario: Creating Tag With Long Name Without Description
  Login as kapua-sys, go to tags, try to create a tag with long (255 characters) name without description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Y5gJ7o5XPkLBBFttelFa6tKTfF2G905xbQL7MTpoKcW8hDnXUORC0Rv0z6MJm1vKZPt6Wm6EB7RiJrP0D0hi28R2272J5inIlA7KiDxSKljwX4N7zW8RK7fwhUkwemA5qyF2DQ2DncXTUxsyAlXhh9qIJ43cPC7lSWyTNUFnMshYlLtB2ArnXPgLDQLooJlfdn6qbwTnNUOxML0OYrVoV1spfsZQEYsmFk9r53mfLajIfxDeHtoEShDxnHL4fgh" without description
    And Tag with name "Y5gJ7o5XPkLBBFttelFa6tKTfF2G905xbQL7MTpoKcW8hDnXUORC0Rv0z6MJm1vKZPt6Wm6EB7RiJrP0D0hi28R2272J5inIlA7KiDxSKljwX4N7zW8RK7fwhUkwemA5qyF2DQ2DncXTUxsyAlXhh9qIJ43cPC7lSWyTNUFnMshYlLtB2ArnXPgLDQLooJlfdn6qbwTnNUOxML0OYrVoV1spfsZQEYsmFk9r53mfLajIfxDeHtoEShDxnHL4fgh" is searched
    Then I find a tag with name "Y5gJ7o5XPkLBBFttelFa6tKTfF2G905xbQL7MTpoKcW8hDnXUORC0Rv0z6MJm1vKZPt6Wm6EB7RiJrP0D0hi28R2272J5inIlA7KiDxSKljwX4N7zW8RK7fwhUkwemA5qyF2DQ2DncXTUxsyAlXhh9qIJ43cPC7lSWyTNUFnMshYlLtB2ArnXPgLDQLooJlfdn6qbwTnNUOxML0OYrVoV1spfsZQEYsmFk9r53mfLajIfxDeHtoEShDxnHL4fgh"
    And I logout

  Scenario: Creating Tag With Too Long Name Without Description
  Login as kapua-sys, go to tags, try to create a tag with too long (256 characters) name without description.
  Kapua should return Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Then I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument tagCreator.name: Value over than allowed max length. Max length is 255."
    When I create tag with name "aY5gJ7o5XPkLBBFttelFa6tKTfF2G905xbQL7MTpoKcW8hDnXUORC0Rv0z6MJm1vKZPt6Wm6EB7RiJrP0D0hi28R2272J5inIlA7KiDxSKljwX4N7zW8RK7fwhUkwemA5qyF2DQ2DncXTUxsyAlXhh9qIJ43cPC7lSWyTNUFnMshYlLtB2ArnXPgLDQLooJlfdn6qbwTnNUOxML0OYrVoV1spfsZQEYsmFk9r53mfLajIfxDeHtoEShDxnHL4fgh" without description
    Then An exception was thrown
    And I logout

  Scenario: Creating Tag With Permitted Symbols In Name Without Description
  Login as kapua-sys, go to tags, try to create a tag name with permitted ('-' and '_') symbols without description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag-12_3" without description
    And Tag with name "Tag-12_3" is searched
    Then I find a tag with name "Tag-12_3"
    And No exception was thrown
    And I logout

  Scenario: Creating Tag With Invalid Symbols In Name Without Description
  Login as kapua-sys, go to tags, try to create a tag name with invalid symbols without description.
  Kapua should return Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "*"
    When I try to create tags with that include invalid symbols in name
    Then An exception was thrown
    And I logout

  Scenario: Creating Tag Without a Name And Without Description
  Login as kapua-sys, go to tags, try to create a tag name without a name.
  Kapua should return an error.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Then I expect the exception "Exception"
    When I create tag with name "" without description
    Then An exception was thrown
    And I logout

  Scenario: Creating Tag With Numbers In Name Without Description
  Login as kapua-sys, go to tags, try to create a tag that contains numbers.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "12345" without description
    And Tag with name "12345" is searched
    Then I find a tag with name "12345"
    And I logout

  Scenario: Creating Unique Tag With Unique Description
  Login as kapua-sys, go to tags, try to create a unique tag with unique description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag123" and description "Description-@12#$456"
    And Tag with name "Tag123" is searched
    Then I find a tag with name "Tag123"
    And I logout

  Scenario: Creating Unique Tag With Non-unique Description
  Login as kapua-sys, go to tags, try to create two unique tags with same descriptions.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag123" and description "Description1"
    And I create tag with name "Tag456" and description "Description1"
    And Tag with name "Tag123" is searched
    Then I find a tag with name "Tag123"
    When Tag with name "Tag456" is searched
    And I find a tag with name "Tag456"
    And I logout

  Scenario: Creating Non-Unique Tag With Valid Description
  Login as kapua-sys, go to tags, try to create non-unique tag with valid Description.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag123" and description "Description1"
    Given I expect the exception "KapuaDuplicateNameException"
    When I create tag with name "Tag123" and description "Description1"
    Then An exception was thrown
    And I logout

  Scenario: Creating Tag With Short Name With Valid Description
  Login as kapua-sys, go to tags, try to create tag with short name with valid Description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag" and description "Valid description 123"
    And Tag with name "Tag" is searched
    Then I find a tag with name "Tag"
    And I logout

  Scenario: Creating Tag With Too Short Name With Valid Description
  Login as kapua-sys, go to tags, try to create tag with too short name with valid description.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I expect the exception "Exception"
    When I create tag with name "t" and description "Valid description 123"
    Then An exception was thrown
    And I logout

  Scenario: Creating Tag With Long Name With Valid Description
  Login as kapua-sys, go to tags, try to create tag with long name with valid description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "oAB7rpR552NlNY0TV8G4h7pikTASljfgRzc50ZSXBX5finW69LHoExMG3gyYpOeboQ01plWuF74qrYT2fvgtjmpLVn7UkbAVWvok7kDodu3rJGqaHIIBIxdAm1FhoWM0sc9ROSeEyv0RV1WVH2Fey4eVFf5aqG3T6hSwUNpJFblaZvfLoh3f9aBPNibEsVFSmqvJwdH3Vi1q8NHfv3hlTUxZidLCphUSTGaB8Yecp7mJJXVM1OwXCpiOcyGc5Uj" and description "Valid description 123"
    And Tag with name "oAB7rpR552NlNY0TV8G4h7pikTASljfgRzc50ZSXBX5finW69LHoExMG3gyYpOeboQ01plWuF74qrYT2fvgtjmpLVn7UkbAVWvok7kDodu3rJGqaHIIBIxdAm1FhoWM0sc9ROSeEyv0RV1WVH2Fey4eVFf5aqG3T6hSwUNpJFblaZvfLoh3f9aBPNibEsVFSmqvJwdH3Vi1q8NHfv3hlTUxZidLCphUSTGaB8Yecp7mJJXVM1OwXCpiOcyGc5Uj" is searched
    Then I find a tag with name "oAB7rpR552NlNY0TV8G4h7pikTASljfgRzc50ZSXBX5finW69LHoExMG3gyYpOeboQ01plWuF74qrYT2fvgtjmpLVn7UkbAVWvok7kDodu3rJGqaHIIBIxdAm1FhoWM0sc9ROSeEyv0RV1WVH2Fey4eVFf5aqG3T6hSwUNpJFblaZvfLoh3f9aBPNibEsVFSmqvJwdH3Vi1q8NHfv3hlTUxZidLCphUSTGaB8Yecp7mJJXVM1OwXCpiOcyGc5Uj"
    And I logout

  Scenario: Creating Tag With Too Long Name With Valid Description
  Login as kapua-sys, go to tags, try to create tag with too long name with valid description.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument tagCreator.name: Value over than allowed max length. Max length is 255."
    When I create tag with name "aoAB7rpR552NlNY0TV8G4h7pikTASljfgRzc50ZSXBX5finW69LHoExMG3gyYpOeboQ01plWuF74qrYT2fvgtjmpLVn7UkbAVWvok7kDodu3rJGqaHIIBIxdAm1FhoWM0sc9ROSeEyv0RV1WVH2Fey4eVFf5aqG3T6hSwUNpJFblaZvfLoh3f9aBPNibEsVFSmqvJwdH3Vi1q8NHfv3hlTUxZidLCphUSTGaB8Yecp7mJJXVM1OwXCpiOcyGc5Uj" and description "Valid description 123"
    Then An exception was thrown
    And I logout

  Scenario: Creating Tag With Permitted Symbols In Name With Valid Description
  Login as kapua-sys, go to tags, try to create tag with permitted symbols in name with valid description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag_12-3" and description "Valid description 123"
    And Tag with name "Tag_12-3" is searched
    Then I find a tag with name "Tag_12-3"
    And I logout

  Scenario: Creating Tag With Invalid Symbols In Name With Valid Description
  Login as kapua-sys, go to tags, try to create a tag with invalid symbols with valid description.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I expect the exception "Exception"
    When I create tag with name "Tag@123" and description "Valid description 123"
    Then An exception was thrown
    And I logout

  Scenario: Creating Tag Without a Name And With Valid Description
  Login as kapua-sys, go to tags, try to create a tag without a name with valid description.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    Given I expect the exception "KapuaIllegalNullArgumentException"
    When I create tag with name "" and description "Valid description 123"
    Then An exception was thrown
    And I logout

  Scenario: Creating Tag With Numbers In Name With Valid Description
  Login as kapua-sys, go to tags, try to create a tag with numbers in name with valid description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag123" and description "Valid-description@33-2"
    And Tag with name "Tag123" is searched
    Then I find a tag with name "Tag123"
    And I logout

  Scenario: Creating Unique Tag With Short Description
  Login as kapua-sys, go to tags, try to create a tag with short description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag123" and description "a"
    And Tag with name "Tag123" is searched
    Then I find a tag with name "Tag123"
    And I logout

  Scenario: Creating Unique Tag With Long Description
  Login as kapua-sys, go to tags, try to create a tag with long description.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag123" and description "oAB7rpR552NlNY0TV8G4h7pikTASljfgRzc50ZSXBX5finW69LHoExMG3gyYpOeboQ01plWuF74qrYT2fvgtjmpLVn7UkbAVWvok7kDodu3rJGqaHIIBIxdAm1FhoWM0sc9ROSeEyv0RV1WVH2Fey4eVFf5aqG3T6hSwUNpJFblaZvfLoh3f9aBPNibEsVFSmqvJwdH3Vi1q8NHfv3hlTUxZidLCphUSTGaB8Yecp7mJJXVM1OwXCpiOcyGc5Uj"
    And Tag with name "Tag123" is searched
    Then I find a tag with name "Tag123"
    And I logout

  Scenario: Changing Tag's Name To Unique One
  Login as kapua-sys, go to tags, try to edit tag's name into a unique one.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag1" without description
    And Name of tag "Tag1" is changed into "Tag2"
    And Tag with name "Tag2" is searched
    Then I find a tag with name "Tag2"
    When Tag with name "Tag1" is searched
    Then Tag with name "Tag1" is not found
    And I logout

  Scenario: Changing Tag's Name To Non-Unique One
  Login as kapua-sys, go to tags, try to edit tag's name to non-unique one.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag1" without description
    And I create tag with name "Tag2" without description
    Given I expect the exception "KapuaDuplicateNameException" with the text "An entity with the same name Tag1 already exists."
    When Name of tag "Tag2" is changed into "Tag1"
    Then An exception was thrown
    Then I logout

  Scenario: Changing Tag's Name To Short One Without Description
  Login as kapua-sys, go to tags, try to edit tag's name to short one.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag1" without description
    And Name of tag "Tag1" is changed into "abc"
    And Tag with name "abc" is searched
    Then I find a tag with name "abc"
    And Tag with name "Tag1" is searched
    Then Tag with name "Tag1" is not found
    And I logout

  Scenario: Changing Tag's Name To a Too Short One Without Description
  Login as kapua-sys, go to tags, try to edit tag's name to a too short one.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag1" without description
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument tag.name: Value less than allowed min length. Min length is 3."
    And Tag name is changed into name "a"
    Then An exception was thrown
    Then I logout

  Scenario: Changing Tag's Name To a Long One Without Description
  Login as kapua-sys, go to tags, try to edit tag's name to a long (255 characters) one.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag1" without description
    And Name of tag "Tag1" is changed into "YXZb6s4L1f6Xk9J23S5RcNcH4Befzk4fg1fDi0PkIbCGtaIN50lWeklthY7Ngo06ss6lmUcqaHiChWjXYdqlcn1UMyqCHcuP4eG0qc9h7a9FLlnXgiFvcAQfvki8iwTPVEdEpBzOWZoWEssb9v966k0tSeQye4yxFC2FyR2SlNZTW06D0krB6zjKa8k5t1BJ2HbJwj5cp8Gsabjyk8lEtlMBeDLqCJv3ik3MZhySD1UvXMkWpOPZoixik8tCBHX"
    And Tag with name "YXZb6s4L1f6Xk9J23S5RcNcH4Befzk4fg1fDi0PkIbCGtaIN50lWeklthY7Ngo06ss6lmUcqaHiChWjXYdqlcn1UMyqCHcuP4eG0qc9h7a9FLlnXgiFvcAQfvki8iwTPVEdEpBzOWZoWEssb9v966k0tSeQye4yxFC2FyR2SlNZTW06D0krB6zjKa8k5t1BJ2HbJwj5cp8Gsabjyk8lEtlMBeDLqCJv3ik3MZhySD1UvXMkWpOPZoixik8tCBHX" is searched
    Then I find a tag with name "YXZb6s4L1f6Xk9J23S5RcNcH4Befzk4fg1fDi0PkIbCGtaIN50lWeklthY7Ngo06ss6lmUcqaHiChWjXYdqlcn1UMyqCHcuP4eG0qc9h7a9FLlnXgiFvcAQfvki8iwTPVEdEpBzOWZoWEssb9v966k0tSeQye4yxFC2FyR2SlNZTW06D0krB6zjKa8k5t1BJ2HbJwj5cp8Gsabjyk8lEtlMBeDLqCJv3ik3MZhySD1UvXMkWpOPZoixik8tCBHX"
    And Tag with name "Tag1" is searched
    Then Tag with name "Tag1" is not found
    And I logout

  Scenario: Changing Tag's Name To a Too Long One Without Description
  Login as kapua-sys, go to tags, try to edit tag's name to a too long (256 characters) one.
  Kapua should throw Exception.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag1" without description
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument tag.name: Value over than allowed max length. Max length is 255."
    When Name of tag "Tag1" is changed into "aYXZb6s4L1f6Xk9J23S5RcNcH4Befzk4fg1fDi0PkIbCGtaIN50lWeklthY7Ngo06ss6lmUcqaHiChWjXYdqlcn1UMyqCHcuP4eG0qc9h7a9FLlnXgiFvcAQfvki8iwTPVEdEpBzOWZoWEssb9v966k0tSeQye4yxFC2FyR2SlNZTW06D0krB6zjKa8k5t1BJ2HbJwj5cp8Gsabjyk8lEtlMBeDLqCJv3ik3MZhySD1UvXMkWpOPZoixik8tCBHX"
    Then An exception was thrown
    Then I logout

  Scenario: Changing Tag's Name To Contain Permitted Symbols In Name Without Description
  Login as kapua-sys, go to tags, try to edit tag's name to a name with permitted symbols.
  Kapua should not return any errors.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And I configure the tag service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    When I create tag with name "Tag1" without description
    And Tag name is changed into name "T-a-g_1"
    And Tag with name "T-a-g_1" is searched
    Then I find a tag with name "T-a-g_1"
    And I logout