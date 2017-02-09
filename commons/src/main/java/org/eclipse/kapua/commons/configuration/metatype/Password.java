/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration.metatype;

/**
 * Password
 *
 * @since 1.0
 */
public class Password {

    private String password;

    /**
     * Constructor
     *
     * @param password
     */
    public Password(String password) {
        super();
        this.password = password;
    }

    /**
     * Get password
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return password;
    }
}
