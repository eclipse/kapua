/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util.log;

import org.eclipse.kapua.commons.liquibase.KapuaLiquibaseClient;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple configuration printer.
 * <p>
 * This can be used when starting a component (i.e. {@link KapuaLiquibaseClient}) and print its configuration on a given {@link Logger}
 * to print something like this:
 * <pre>
 * 09:56:07.286 [main] INFO  o.e.k.c.l.KapuaLiquibaseClient - =================== KapuaLiquibaseClient Configuration ===================
 * 09:56:07.293 [main] INFO  o.e.k.c.l.KapuaLiquibaseClient - | Liquibase Version: 3.6.3
 * 09:56:07.293 [main] INFO  o.e.k.c.l.KapuaLiquibaseClient - | DB connection info
 * 09:56:07.293 [main] INFO  o.e.k.c.l.KapuaLiquibaseClient - |     JDBC URL: jdbc:h2:mem:kapua;MODE=MySQL;
 * 09:56:07.294 [main] INFO  o.e.k.c.l.KapuaLiquibaseClient - |     Username: kapua
 * 09:56:07.294 [main] INFO  o.e.k.c.l.KapuaLiquibaseClient - |     Password: ******
 * 09:56:07.294 [main] INFO  o.e.k.c.l.KapuaLiquibaseClient - |     Schema: N/A
 * 09:56:07.298 [main] INFO  o.e.k.c.l.KapuaLiquibaseClient - | Timestamp(3) fix info (eclipse/kapua#2889)
 * 09:56:07.301 [main] INFO  o.e.k.c.l.KapuaLiquibaseClient - |     Force timestamp fix: false
 * 09:56:07.301 [main] INFO  o.e.k.c.l.KapuaLiquibaseClient - |     Apply timestamp fix: true
 * 09:56:07.303 [main] INFO  o.e.k.c.l.KapuaLiquibaseClient - ==========================================================================
 * </pre>
 * <p>
 * Usage example:
 * <pre>
 * ConfigurationPrinter
 *                 .create()
 *                 .withLogger(LOG)
 *                 .withLogLevel(ConfigurationPrinter.LogLevel.INFO)
 *                 .withTitle("KapuaLiquibaseClient Configuration")
 *                 .addParameter("Liquibase Version", currentLiquibaseVersionString)
 *                 .addHeader("DB connection info")
 *                 .increaseIndentation()
 *                 .addParameter("JDBC URL", jdbcUrl)
 *                 .addParameter("Username", username)
 *                 .addParameter("Password", "******")
 *                 .addParameter("Schema", schema)
 *                 .decreaseIndentation()
 *                 .addHeader("Timestamp(3) fix info (eclipse/kapua#2889)")
 *                 .increaseIndentation()
 *                 .addParameter("Force timestamp fix", forceTimestampFix)
 *                 .addParameter("Apply timestamp fix", runTimestampsFix)
 *                 .printLog();
 * </pre>
 * <p>
 * Example are from {@link KapuaLiquibaseClient} but can be generalized if needed.
 *
 * @since 1.3.0
 */
public class ConfigurationPrinter {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationPrinter.class);

    private Logger parentLogger;
    private LogLevel logLevel;

    private String title;
    private TitleAlignment titleAlignment;

    private List<Configuration> configurations;
    private int currentIndentation;

    /**
     * Gets the parent {@link Logger} to be used to {@link #printLog()}
     *
     * @return The parent {@link Logger} to be used to {@link #printLog()}
     * @since 1.3.0
     */
    protected Logger getParentLogger() {
        return parentLogger;
    }

    /**
     * Sets the parent {@link Logger} to be used to {@link #printLog()}
     *
     * @param logger The parent {@link Logger} to be used to {@link #printLog()}
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    public ConfigurationPrinter withLogger(@NotNull Logger logger) {
        this.parentLogger = logger;
        return this;
    }

    /**
     * Gets the {@link LogLevel} to be used to {@link #printLog()}
     *
     * @return The {@link LogLevel} to be used to {@link #printLog()}
     * @since 1.3.0
     */
    protected LogLevel getLogLevel() {
        return logLevel;
    }

    /**
     * Sets the {@link LogLevel} to be used to {@link #printLog()}
     *
     * @param logLevel The {@link LogLevel} to use.
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    public ConfigurationPrinter withLogLevel(@NotNull LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    /**
     * Gets the title of the configuration.
     *
     * @return The title of the configuration.
     * @since 1.3.0
     */
    protected String getTitle() {
        return title;
    }

    /**
     * Sets the title of the configuration.
     * <p>
     * It will be printed as:
     * <pre>
     * =================== {title} ===================
     * </pre>
     * according to the {@link #getTitleAlignment()}
     *
     * @param title The title of the configuration.
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    public ConfigurationPrinter withTitle(@NotNull String title) {
        this.title = title;
        return this;
    }

    /**
     * Gets the {@link TitleAlignment}.
     *
     * @return The {@link TitleAlignment}.
     * @since 1.3.0
     */
    protected TitleAlignment getTitleAlignment() {
        if (titleAlignment == null) {
            titleAlignment = TitleAlignment.CENTER;
        }

        return titleAlignment;
    }

    /**
     * Sets the {@link TitleAlignment} to {@link #printLog()}.
     * <p>
     * {@link TitleAlignment#LEFT} will print as:
     * <pre>
     * = {title} =====================================
     * </pre>
     * <p>
     * {@link TitleAlignment#CENTER} will print as:
     * <pre>
     * =================== {title} ===================
     * </pre>
     * <p>
     * {@link TitleAlignment#RIGHT} will print as:
     * <pre>
     * ===================================== {title} =
     * </pre>
     * <p>
     * It defaults to {@link TitleAlignment#CENTER}.
     *
     * @param titleAlignment The {@link TitleAlignment} to use.
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    public ConfigurationPrinter withTitleAlignment(@Nullable TitleAlignment titleAlignment) {
        this.titleAlignment = titleAlignment;
        return this;
    }

    /**
     * Gets the {@link Configuration} to print.
     *
     * @return The {@link Configuration} to print.
     * @since 1.3.0
     */
    protected List<Configuration> getConfigurations() {
        if (configurations == null) {
            configurations = new ArrayList<>();
        }

        return configurations;
    }

    /**
     * Adds a {@link ConfigurationHeader}.
     * <p>
     * It will be printed as:
     * <pre>
     * |\t{name}
     * </pre>
     *
     * @param name The name of the {@link ConfigurationHeader}
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    public ConfigurationPrinter addHeader(@NotNull String name) {
        getConfigurations().add(new ConfigurationHeader(currentIndentation, name));
        return this;
    }

    /**
     * Adds a {@link ConfigurationParameter}.
     * <p>
     * It will be printed as:
     * <pre>
     * |\t{name}: {value}
     * </pre>
     *
     * @param name The name of the {@link ConfigurationParameter}
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    public ConfigurationPrinter addParameter(@NotNull String name, @Nullable Object value) {
        getConfigurations().add(new ConfigurationParameter(currentIndentation, name, value));
        return this;
    }

    /**
     * Shortcut method for:
     * <pre>
     *      addHeader(name);
     *      increaseIndentation();
     * </pre>
     *
     * @param name The name of the {@link ConfigurationHeader}
     * @return Itself, to chain method invocation.
     * @since 1.5.0
     */
    public ConfigurationPrinter openSection(@NotNull String name) {
        addHeader(name);
        increaseIndentation();
        return this;
    }

    /**
     * Same as {@link #decreaseIndentation()} but to be used in combination with {@link #openSection(String)}
     *
     * @return Itself, to chain method invocation.
     * @since 1.5.0
     */
    public ConfigurationPrinter closeSection() {
        decreaseIndentation();
        return this;
    }

    /**
     * Increases the indentation of the printed configuration.
     * <p>
     * It prints a {@code \t} for each indentation added.
     *
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    public ConfigurationPrinter increaseIndentation() {
        currentIndentation++;
        return this;
    }

    /**
     * Decreases the indentation of the printed configuration.
     * <p>
     * It cannot go negative, so check usage of this and {@link #increaseIndentation()}
     *
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    public ConfigurationPrinter decreaseIndentation() {
        if (currentIndentation > 0) {
            currentIndentation--;
        }

        return this;
    }

    /**
     * Prints the given {@link Configuration}s.
     * <p>
     * It uses the given {@link #getParentLogger()} or the {@link #LOG} if the former was not provided.
     * It is advised to always provide it using {@link #withLogger(Logger)}.
     *
     * @since 1.3.0
     */
    public void printLog() {
        //
        // Check Provided data
        if (getTitle() == null) {
            LOG.warn("Title was not provided. Using default 'Info'");
            LOG.warn("To fix this please use .withTitle(java.lang.String) providing your own!");
            withTitle("Info");
        }

        if (getParentLogger() == null) {
            LOG.warn("External Logger not provided! Using the Configuration Printer's one!");
            LOG.warn("To fix this please use .withLogger(org.slf4j.Logger) providing your own!");
            withLogger(LOG);
        }

        if (getLogLevel() == null) {
            LOG.warn("Log level was not provided! Defaulting to LogLevel.INFO");
            LOG.warn("To fix this please use .withLogLevel(org.eclipse.kapua.commons.util.log.ConfigurationPrinter.LogLevel) providing the desired level!");
            withLogLevel(LogLevel.INFO);
        }

        //
        // Title
        String alignedTitleFormat = buildAlignedTitleFormat();
        printLogLeveled(alignedTitleFormat, getTitle());

        //
        // Parameters
        for (Configuration configuration : getConfigurations()) {
            printLogLeveled("|  {}", configuration);
        }

        //
        // End Line - Same length of Title
        String footerLog = new String(new char[alignedTitleFormat.length()]).replace('\0', '=');
        printLogLeveled(footerLog);
    }

    /**
     * Produces the log formar for the {@link #getTitle()} according to {@link #getTitleAlignment()}
     *
     * @return The {@link #getTitle()} aligned format.
     * @since 1.3.0
     */
    private String buildAlignedTitleFormat() {
        switch (getTitleAlignment()) {
            case LEFT:
                return "= {} =====================================";
            case RIGHT:
                return "===================================== {} =";
            case CENTER:
            default:
                return "=================== {} ===================";
        }
    }

    /**
     * Prints the given log according to {@link #getLogLevel()}.
     *
     * @param log The log to print.
     * @since 1.3.0
     */
    private void printLogLeveled(String log) {
        printLogLeveled(log, null);
    }

    /**
     * Prints the given logFormat with the given argument according to {@link #getLogLevel()}.
     *
     * @param logFormat The log format to print.
     * @param arg       The argument to populate the format.
     * @since 1.3.0
     */
    private void printLogLeveled(String logFormat, Object arg) {
        switch (getLogLevel()) {
            case DEBUG:
                getParentLogger().debug(logFormat, arg);
                break;
            case ERROR:
                getParentLogger().error(logFormat, arg);
                break;
            case INFO:
                getParentLogger().info(logFormat, arg);
                break;
            case TRACE:
                getParentLogger().trace(logFormat, arg);
                break;
            case WARN:
                getParentLogger().warn(logFormat, arg);
                break;
        }
    }

    //
    // Creator
    //

    /**
     * Creates a {@code new} {@link ConfigurationPrinter}.
     *
     * @return a {@code new} {@link ConfigurationPrinter}.
     * @since 1.3.0
     */
    public static ConfigurationPrinter create() {
        return new ConfigurationPrinter();
    }


    //
    // Configuration Classes
    //

    /**
     * Base class of {@link Configuration}s which handles the {@link #indentation}.
     *
     * @since 1.3.0
     */
    private static class Configuration {

        final int indentation;

        public Configuration(int indentation) {
            this.indentation = indentation;
        }

        @Override
        public String toString() {
            return new String(new char[indentation]).replace('\0', '\t');
        }
    }

    /**
     * Header {@link Configuration} which handles the {@link #name}
     * <p>
     * Used to create section of {@link ConfigurationParameter}.
     *
     * @since 1.3.0
     */
    private static class ConfigurationHeader extends Configuration {

        final String name;

        public ConfigurationHeader(int indentation, String name) {
            super(indentation);

            this.name = name;
        }

        @Override
        public String toString() {
            return super.toString().concat(name);
        }
    }

    /**
     * Parameter {@link Configuration} which handles {@link #indentation}, {@link #name} and {@link #value}.
     * <p>
     * Used to create the list of parameters to print.
     *
     * @since 1.3.0
     */
    private static class ConfigurationParameter extends ConfigurationHeader {

        final Object value;

        public ConfigurationParameter(int indentation, @NotNull String name, @Nullable Object value) {
            super(indentation, name);

            this.value = value;
        }

        @Override
        public String toString() {
            return super.toString().concat(": ").concat(value != null ? value.toString() : "N/A");
        }
    }

    //
    // Log Level
    //

    /**
     * Identifies the log level to use on {@link #printLog()}.
     * <p>
     * Works the same as common {@link Logger} implementations.
     *
     * @since 1.3.0
     */
    public enum LogLevel {
        /**
         * {@link #printLog()} will use {@link Logger#debug(String)}
         *
         * @since 1.3.0
         */
        DEBUG,

        /**
         * {@link #printLog()} will use {@link Logger#error(String)}
         *
         * @since 1.3.0
         */
        ERROR,

        /**
         * {@link #printLog()} will use {@link Logger#info(String)}
         *
         * @since 1.3.0
         */
        INFO,

        /**
         * {@link #printLog()} will use {@link Logger#trace(String)}
         *
         * @since 1.3.0
         */
        TRACE,

        /**
         * {@link #printLog()} will use {@link Logger#warn(String)}
         *
         * @since 1.3.0
         */
        WARN
    }

    //
    // Title
    //

    /**
     * Sets the alignement for {@link #getTitle()}
     *
     * @since 1.3.0
     */
    public enum TitleAlignment {
        /**
         * {@link #printLog()} will print the title on the left.
         *
         * @since 1.3.0
         */
        LEFT,

        /**
         * {@link #printLog()} will print the title centered.
         *
         * @since 1.3.0
         */
        CENTER,

        /**
         * {@link #printLog()} will print the title on the right.
         *
         * @since 1.3.0
         */
        RIGHT
    }
}

