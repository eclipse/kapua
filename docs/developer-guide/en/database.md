# Working with Kapua database

In current implementation of Kapua we hold device data in SQL database (it can MariaDB or H2 SQL Server). In order to keep SQL schema updated,
we decided to use [Liquibase](https://www.liquibase.org/).

## Liquibase in Kapua

Kapua broker on startup executes embedded Liquibase client which ensures that all database update scripts have been collected and
applied to your database. The coordinates of database URL connection are obtained from regular Kapua settings i.e. the following
system properties:

- `commons.db.username`
- `commons.db.password`
- `commons.db.schema`

Liquibase clients looks up for all scripts located in the classpath matching `liquibase/*.sql` pattern.

### Adding new SQL script to the project

If you would like a new SQL script to your service, just add new `*.sql` file to `src/main/resources/liquibase` directory of your Maven project. Just keep in mind that 
the name of your file should be unique, so the best way is to make it matching the name of your service module (for example `device.sql` for device management).

### Adding a new change set to existing SQL file

If you would like to add new change set to existing SQL file, append new `--changeset filename:number` line to your log file. The example:

```
--changeset device_event:2

CREATE TABLE my_new_table (
  scope_id             	    BIGINT(21) 	  UNSIGNED NOT NULL
  PRIMARY KEY (scope_id, id)
) CHARSET=utf8;
```

More details regarding Liquibase SQL file syntax, can be found on [Liquibase web page](http://www.liquibase.org/documentation/sql_format.html).