/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua;

import java.io.Serializable;

/**
 * {@link KapuaErrorCode} definition.
 *
 * @since 1.0.0
 */
public interface KapuaErrorCode extends Serializable {

    /**
     * Gets the error code name.
     * <p>
     * The name can be used to search the exception message in the {@link java.util.ResourceBundle}.
     *
     * @return The error code name.
     * @since 1.0.0
     */
    String name();
}
