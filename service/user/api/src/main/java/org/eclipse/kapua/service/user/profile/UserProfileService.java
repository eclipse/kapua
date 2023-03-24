/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.user.profile;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.KapuaService;

public interface UserProfileService extends KapuaService {

    void changeUserProfile(UserProfile userProfile) throws KapuaException;

    UserProfile getUserProfile() throws KapuaException;
}
