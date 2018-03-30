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
package org.eclipse.kapua.service.authorization.permission;

import org.eclipse.kapua.model.KapuaPredicates;

public interface PermissionPredicates extends KapuaPredicates {

    /**
     * Predicate for field {@link Permission#getDomain()}
     *
     * @since 1.0.0
     */
    String DOMAIN = "domain";

    /**
     * Predicate for field {@link Permission#getAction()}
     *
     * @since 1.0.0
     */
    String ACTION = "action";

    /**
     * Predicate for field {@link Permission#getTargetScopeId()}
     *
     * @since 1.0.0
     */
    String TARGET_SCOPE_ID = "targetScopeId";

    /**
     * Predicate for field {@link Permission#getGroupId()}
     *
     * @since 1.0.0
     */
    String GROUP_ID = "groupId";

    /**
     * Predicate for field {@link Permission#getForwardable()}
     *
     * @since 1.0.0
     */
    String FORWARDABLE = "forwardable";

}
