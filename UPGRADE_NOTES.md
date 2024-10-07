# Upgrade Notes v 2.1.0-SNAPSHOT

Below are described most important changes, features, additions and bugfixes that needs attention while performing the upgrade from the previous version.

This report is only partial for Eclipse Kapua release 2.1.0-SNAPSHOT, since we started to maintain it mid-release development.

## DB Changes

#### New column in MFA Option table

In PR [#4115](https://github.com/eclipse/kapua/pull/4115) a new `has_trust_me` column has been added to the `atht_mfa_option`.

New column defaults to `null` and doesn't need any migration. The code itself will populate the column accordingly on first update.
For large `atht_mfa_option` tables it might take sometime to be added.