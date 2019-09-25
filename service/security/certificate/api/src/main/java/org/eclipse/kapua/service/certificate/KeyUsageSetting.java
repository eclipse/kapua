/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.certificate;

/**
 * @since 1.0.0
 */

public interface KeyUsageSetting {

    /**
     * @since 1.0.0
     */
    KeyUsage getKeyUsage();

    /**
     * @since 1.0.0
     */
    void setKeyUsage(KeyUsage keyUsage);

    /**
     * @since 1.0.0
     */
    boolean getAllowed();

    /**
     * @since 1.0.0
     */
    void setAllowed(boolean allowed);

    /**
     * @since 1.0.0
     */
    Boolean getKapuaAllowed();

    /**
     * @since 1.0.0
     */
    void setKapuaAllowed(Boolean allowed);

}
