/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.connector;


public enum MessageContent {

    /**
     * Data messages (to be processed by processors)
     */
    DATA,
    /**
     * system messages (skipped by processors)
     */
    SYSTEM,
    /**
     * system empty messages (skipped by processors)
     */
    SYSTEM_EMPTY

}
