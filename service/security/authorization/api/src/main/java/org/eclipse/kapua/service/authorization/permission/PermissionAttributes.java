/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.permission;

public class PermissionAttributes {

    protected PermissionAttributes() { }

    /**
     * Predicate for field {@link Permission#getDomain()}
     *
     * @since 1.0.0
     */
    public static final String DOMAIN = "domain";

    /**
     * Predicate for field {@link Permission#getAction()}
     *
     * @since 1.0.0
     */
    public static final String ACTION = "action";

    /**
     * Predicate for field {@link Permission#getTargetScopeId()}
     *
     * @since 1.0.0
     */
    public static final String TARGET_SCOPE_ID = "targetScopeId";

    /**
     * Predicate for field {@link Permission#getGroupId()}
     *
     * @since 1.0.0
     */
    public static final String GROUP_ID = "groupId";

    /**
     * Predicate for field {@link Permission#getForwardable()}
     *
     * @since 1.0.0
     */
    public static final String FORWARDABLE = "forwardable";

}
