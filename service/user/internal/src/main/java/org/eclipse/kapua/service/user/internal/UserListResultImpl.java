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
package org.eclipse.kapua.service.user.internal;

import org.eclipse.kapua.commons.model.query.KapuaListResultImpl;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserListResult;

/**
 * User list result implementation.
 *
 * @since 1.0
 *
 */
public class UserListResultImpl extends KapuaListResultImpl<User> implements UserListResult {

    private static final long serialVersionUID = 2231053707705207563L;
}
