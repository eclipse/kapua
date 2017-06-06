/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.steps;

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
