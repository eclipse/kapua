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
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.model.configuration;

/**
 * Kura password
 *
 * @since 1.0
 *
 */
public class KuraPassword {

    private String password;

    /**
     * Constructor
     *
     * @param password
     */
    public KuraPassword(String password) {
        super();
        this.password = password;
    }

    /**
     * Get the password
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
