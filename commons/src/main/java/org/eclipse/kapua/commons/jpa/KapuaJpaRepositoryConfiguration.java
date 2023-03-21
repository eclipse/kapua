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
    public KapuaJpaRepositoryConfiguration(String escape, String like, String any, int maxInsertAllowedRetry) {
        this.escape = escape;
        this.like = like;
        this.any = any;
        this.maxInsertAllowedRetry = maxInsertAllowedRetry;
    }

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
