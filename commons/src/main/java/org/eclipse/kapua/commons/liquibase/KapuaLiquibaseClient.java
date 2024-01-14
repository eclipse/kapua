/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.commons.liquibase;

import com.google.common.base.Strings;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.kapua.commons.liquibase.settings.LiquibaseClientSettingKeys;
import org.eclipse.kapua.commons.liquibase.settings.LiquibaseClientSettings;
import org.eclipse.kapua.commons.util.SemanticVersion;
import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Client that execute {@link Liquibase} scripts.
 * <p>
 * It looks available scripts in {@code .xml} or {@code .sql} in the classpath.
 *
 * @since 1.0.0
 */
public class KapuaLiquibaseClient {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaLiquibaseClient.class);

    private static final SemanticVersion LIQUIBASE_TIMESTAMP_FIX_VERSION = new SemanticVersion("3.3.3"); // https://liquibase.jira.com/browse/CORE-1958

    private static final LiquibaseClientSettings LIQUIBASE_CLIENT_SETTINGS = LiquibaseClientSettings.getInstance();

    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final String schema;
    private final boolean runTimestampsFix;

    /**
     * Constructor.
     *
     * @param jdbcUrl  The JDBC connection string.
     * @param username The username to connect to to the database.
     * @param password The password to connect to to the database.
     * @since 1.0.0
     */
    public KapuaLiquibaseClient(String jdbcUrl, String username, String password) {
        this(jdbcUrl, username, password, (String) null);
    }

    /**
     * Constructor.
     *
     * @param jdbcUrl  The JDBC connection string.
     * @param username The username to connect to to the database.
     * @param password The password to connect to to the database.
     * @param schema   The {@link java.util.Optional} schema name.
     * @since 1.0.0
     * @deprecated Since 1.2.0. Removed usage of {@link Optional} as parameter.
     */
    @Deprecated
    public KapuaLiquibaseClient(String jdbcUrl, String username, String password, Optional<String> schema) {
        this(jdbcUrl, username, password, schema.orElse(null));
    }

    /**
     * Constructor.
     *
     * @param jdbcUrl  The JDBC connection string.
     * @param username The username to connect to to the database.
     * @param password The password to connect to to the database.
     * @param schema   The schema name.
     * @since 1.2.0
     */
    public KapuaLiquibaseClient(String jdbcUrl, String username, String password, String schema) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.schema = schema;

        // Check wether or not fix the timestamp based on Liquibase version
        boolean forceTimestampFix = LIQUIBASE_CLIENT_SETTINGS.getBoolean(LiquibaseClientSettingKeys.FORCE_TIMESTAMPS_FIX);
        String currentLiquibaseVersionString = LIQUIBASE_CLIENT_SETTINGS.getString(LiquibaseClientSettingKeys.LIQUIBASE_VERSION);
        SemanticVersion currentLiquibaseVersion = new SemanticVersion(currentLiquibaseVersionString);

        runTimestampsFix = (currentLiquibaseVersion.afterOrMatches(LIQUIBASE_TIMESTAMP_FIX_VERSION) || forceTimestampFix);

        // Print Configurations
        ConfigurationPrinter
                .create()
                .withLogger(LOG)
                .withLogLevel(ConfigurationPrinter.LogLevel.INFO)
                .withTitle("KapuaLiquibaseClient Configuration")
                .addParameter("Liquibase Version", currentLiquibaseVersionString)
                .openSection("DB connection info")
                .addParameter("JDBC URL", jdbcUrl)
                .addParameter("Username", username)
                .addParameter("Password", "******")
                .addParameter("Schema", schema)
                .closeSection()
                .openSection("Timestamp(3) fix info (eclipse/kapua#2889)")
                .addParameter("Force timestamp fix", forceTimestampFix)
                .addParameter("Apply timestamp fix", runTimestampsFix)
                .closeSection()
                .printLog();
    }

    /**
     * Starts the looking and execution of the Liquibase Scripts.
     *
     * @since 1.0.0
     */
    public void update() {
        try {
            LOG.info("Running Liquibase scripts...");
            if (Boolean.parseBoolean(System.getProperty("LIQUIBASE_ENABLED", "true")) || Boolean.parseBoolean(System.getenv("LIQUIBASE_ENABLED"))) {

                try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                    File changelogDir = loadChangelogs();

                    List<String> contexts = new ArrayList<>();
                    if (!runTimestampsFix) {
                        contexts.add("!fixTimestamps");
                    }

                    File[] releaseDirs = changelogDir.listFiles(file -> file.isDirectory() && file.getName().contains("."));

                    if (releaseDirs != null) {
                        Arrays.sort(releaseDirs);
                        //iterate over the release directories
                        for (File releaseDir : releaseDirs) {
                            executeChangelogsInDir(connection, schema, releaseDir, contexts);
                        }
                    } else { //case where there aren't release dirs at all (for example, tests, or when you have a module external to Kapua)
                        executeChangelogsInDir(connection, schema, changelogDir, contexts);
                    }

                }

                LOG.info("Running Liquibase scripts... DONE!");
            } else {
                LOG.info("Running Liquibase scripts... SKIPPED! Liquibase disabled by System property 'LIQUIBASE_ENABLED'...");
            }
        } catch (LiquibaseException | SQLException | IOException e) {
            LOG.error("Running Liquibase scripts... ERROR! Error: {}", e.getMessage(), e);
            throw new RuntimeException(e); // TODO: throw an appropriate exception!
        }
    }

    public void forceReleaseChangelogLock() {
        LOG.info("Trying to release changelog lock...");
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase((String) null, new FileSystemResourceAccessor(), database);
            liquibase.forceReleaseLocks();
        } catch (LiquibaseException | SQLException e) {
            LOG.error("Running release changelog lock... ERROR! Error: {}", e.getMessage(), e);
            throw new RuntimeException(e); // TODO: throw an appropriate exception!
        }
    }

    protected static synchronized File loadChangelogs() throws IOException {
        String tmpDirectory = SystemUtils.getJavaIoTmpDir().getAbsolutePath();

        File changelogTempDirectory = new File(tmpDirectory, "kapua-liquibase");

        if (changelogTempDirectory.exists()) {
            FileUtils.deleteDirectory(changelogTempDirectory);
        }

        boolean createdTmp = changelogTempDirectory.mkdirs();
        LOG.trace("{} Tmp dir: {}", createdTmp ? "Created" : "Using", changelogTempDirectory.getAbsolutePath());

        Reflections reflections = new Reflections("liquibase", new ResourcesScanner());
        Set<String> changeLogs = reflections.getResources(Pattern.compile(".*\\.xml|.*\\.sql"));
        for (String script : changeLogs) {
            URL scriptUrl = KapuaLiquibaseClient.class.getResource("/" + script);
            File changelogFile = new File(changelogTempDirectory, script.replaceFirst("liquibase/", ""));
            if (changelogFile.getParentFile() != null && !changelogFile.getParentFile().exists()) {
                boolean createdParent = changelogFile.getParentFile().mkdirs();
                LOG.trace("{} parent dir: {}", createdParent ? "Created" : "Using", changelogFile.getParentFile().getAbsolutePath());
            }
            try (FileOutputStream tmpStream = new FileOutputStream(changelogFile)) {
                IOUtils.write(IOUtils.toString(scriptUrl), tmpStream);
            }
            LOG.trace("Copied file: {}", changelogFile.getAbsolutePath());
        }

        return changelogTempDirectory;
    }

    protected static void executeChangelogsInDir(Connection connection, String schema, File changelogDir, List<String> contexts) throws LiquibaseException {
        List<File> changelogs = new ArrayList<>(Arrays.asList(changelogDir.listFiles((dir, name) -> name.startsWith("changelog"))));
        List<File> preChangelogs = changelogs.stream().filter(file -> file.getName().endsWith("pre.xml")).collect(Collectors.toList());
        List<File> postChangelogs = changelogs.stream().filter(file -> file.getName().endsWith("post.xml")).collect(Collectors.toList());
        changelogs.removeAll(preChangelogs);
        changelogs.removeAll(postChangelogs);

        LOG.info("\tEXECUTING LIQUIBASE SCRIPTS FOR VERSION: {}", changelogDir.getName());
        // Find and execute all master scripts
        LOG.info("Executing pre changelog files...");
        executeChangelogs(connection, schema, preChangelogs, contexts);
        LOG.info("Executing pre changelog files... DONE!");

        LOG.info("Executing changelog files...");
        executeChangelogs(connection, schema, changelogs, contexts);
        LOG.info("Executing changelog files... DONE!");

        LOG.info("Executing post changelog files...");
        executeChangelogs(connection, schema, postChangelogs, contexts);
        LOG.info("Executing post changelog files... DONE!");
    }

    protected static void executeChangelogs(Connection connection, String schema, List<File> changeLogs, List<String> contexts) throws LiquibaseException {
        LOG.info("\tLiquibase files found: {}", changeLogs.size());

        LOG.trace("\tSorting Liquibase files found.");
        changeLogs.sort(Comparator.comparing(File::getAbsolutePath));

        String ctx = contexts.isEmpty() ? null : String.join(",", contexts);
        for (File changelog : changeLogs) {
            LOG.info("\t\tExecuting liquibase script: {}...", changelog.getAbsolutePath());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            if (!Strings.isNullOrEmpty(schema)) {
                database.setDefaultSchemaName(schema);
            }
            Liquibase liquibase = new Liquibase(changelog.getAbsolutePath(), new FileSystemResourceAccessor(), database);
            liquibase.update(ctx);

            LOG.debug("\t\tExecuting liquibase script: {}... DONE!", changelog.getAbsolutePath());
        }
    }

}
