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
 *******************************************************************************/
package org.eclipse.kapua.qa.common;

import cucumber.api.java.After;
import cucumber.api.java.Before;

/**
 * Priorities for the {@link Before} and {@link After} hook annotations
 */
public final class HookPriorities {

    private HookPriorities() {
    }

    /*
     * Fire up the database first and shut it down last
     */
    public static final int DATABASE = 1_000;
    public static final int DATASTORE = 2_000;
    public static final int DEFAULT = 10_000;

}
