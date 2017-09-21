# Working with Kapua database

In current implementation of Kapua we hold device data in SQL database (it can MariaDB or H2 SQL Server). In order to keep SQL schema updated,
we decided to use [Liquibase](https://www.liquibase.org/).

## Liquibase in Kapua

Kapua REST APIs, Admin Console and Broker can use, upon their startup, the embedded Liquibase client which ensures that all database update scripts have been collected and
applied to your database. The coordinates of database URL connection are obtained from regular Kapua settings i.e. the following
system properties:

- `commons.db.username`
- `commons.db.password`
- `commons.db.schema`

Please note that the scripts execution is disabled by default. To enable it, set the system property `commons.db.schema.update` to true when running any of the applications.

Liquibase clients looks up for xml scripts located in the classpath matching `liquibase/(*-master.pre.xml|*-master.xml|*-master.post.xml)` pattern. 

Such files will be collected all together from the plugin, then sorted in three distinct sets: all the `*-master.pre.xml` files first, then the `*-master.xml` files and finally `*-master.post.xml` files. Every set will be sorted by path name; then the three sets will then be executed in the same same order (the `master.pre` first, then the `master` set and finally the `master.post` set.)

Every master file must contain at least one reference (via the `<include>` Liquibase XML tag) to a version-specific changelog file, usually contained in a folder named after the service version. Such changelog will, in turn, contain references to the actual Liquibase XML files that gets executed by the Liquibase client.

### Adding new XML script to the project

If you would like a new Liquibase script to your service, just add new `*.xml` file to `src/main/resources/liquibase/_service-version_` directory of your Maven project. Just keep in mind that 
the name of your file should be unique, so the best way is to at least reference the name of your service module (for example `device-new-creation-script.xml` for device management). Also, after you create the XML, be sure to add a reference to it in the main changelog XML for the version. If such file doesn't exist yet, because it's releated to a new version of the service, create it and add a reference to it in an appropriate master XML file.

More details regarding Liquibase XML file syntax, can be found on [Liquibase web page](http://www.liquibase.org/documentation/xml_format.html).
