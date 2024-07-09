/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.commons.model.query.predicate.AbstractMatchPredicate;
import org.eclipse.kapua.service.authorization.role.RoleAttributes;
import org.eclipse.kapua.service.authorization.role.RoleMatchPredicate;
import java.util.Arrays;

public class RoleMatchPredicateImpl<T> extends AbstractMatchPredicate<T> implements RoleMatchPredicate<T> {

    /**
     * Constructor.
     *
     * @param matchTerm
     * @since 2.1.0
     */
    public RoleMatchPredicateImpl(T matchTerm) {
        this.attributeNames = Arrays.asList(
                RoleAttributes.DESCRIPTION,
                RoleAttributes.NAME
        );
        this.matchTerm = matchTerm;
    }
}
