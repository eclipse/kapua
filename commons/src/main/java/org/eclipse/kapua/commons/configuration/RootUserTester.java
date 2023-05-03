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
 *     Eurotech - initial implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;

/**
 * Tests whether a provided user id belongs to the Root User
 *
 * @since 2.0.0
 */
public interface RootUserTester extends KapuaService {

    /**
     * @param userId The user id to test for
     * @return true if the provided id belongs to a Root User, false otherwise
     * @throws KapuaException
     */
    boolean isRoot(KapuaId userId) throws KapuaException;
}
