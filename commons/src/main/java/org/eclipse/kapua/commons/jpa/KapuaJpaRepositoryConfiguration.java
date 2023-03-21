/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.jpa;

public class KapuaJpaRepositoryConfiguration {
    /**
     * Customization constructor for this class. Allows to fine-tune RDBMS-specific configurations
     *
     * @param escape                Escape sequence used by the specific SQL dialect of the used RDBMS
     * @param like                  "like" operator, {@literal %} in most dialects
     * @param any                   "any character" operator,  {@literal _} in most dialects
     * @param maxInsertAllowedRetry Number of allowed retries in case of a key collision when persisting a new entity for the first time
     */
    public KapuaJpaRepositoryConfiguration(String escape, String like, String any, int maxInsertAllowedRetry) {
        this.escape = escape;
        this.like = like;
        this.any = any;
        this.maxInsertAllowedRetry = maxInsertAllowedRetry;
    }

    /**
     * Default constructor. The following values are used:
     *
     * <ul>
     * <li><b>escape sequence:</b> {@literal \\}</li>
     * <li><b>like operator:</b> {@literal %}</li>
     * <li><b>any character operator:</b> {@literal _}</li>
     * <li><b>max number of retries on inserts:</b> 3</li>
     * </ul>
     */
    public KapuaJpaRepositoryConfiguration() {
        this.escape = "\\";
        this.like = "%";
        this.any = "_";
        this.maxInsertAllowedRetry = 3;
    }

    public final String escape;
    public final String like;
    public final String any;
    public final int maxInsertAllowedRetry;
}
