/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util.log;

import org.eclipse.kapua.commons.liquibase.KapuaLiquibaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple configuration printer.
 * <p>
 * This can be used when starting a component (i.e. {@link KapuaLiquibaseClient}) and print its configuration on a given {@link Logger}
 * to print something like this:
 *
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
 *
 * @since 1.3.0
 */
public class ConfigurationPrinter {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationPrinter.class);

    private Logger parentLogger;
    private String title;
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
        if (getParentLogger() == null) {
            LOG.warn("External Logger not provided! Using the Configuration Printer's one!");
            LOG.warn("To fix this please use .withLogger(org.slf4j.Logger) providing your own!");
            withLogger(LOG);
        }

        // Title
        String titleLog = "=================== ".concat(title).concat(" ===================");
        getParentLogger().info(titleLog);

        // Parameters
        for (Configuration configuration : getConfigurations()) {
            getParentLogger().info("|  ".concat(configuration.toString()));
        }

        // End Line - Same length of Title
        String footerLog = new String(new char[titleLog.length()]).replace('\0', '=');
        getParentLogger().info(footerLog);
    }

    /**
     * Creates a {@code new} {@link ConfigurationPrinter}.
     *
     * @return a {@code new} {@link ConfigurationPrinter}.
     * @since 1.3.0
     */
    public static ConfigurationPrinter create() {
        return new ConfigurationPrinter();
    }

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
}

