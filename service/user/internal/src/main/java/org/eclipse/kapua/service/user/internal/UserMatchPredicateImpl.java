/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.model.query.predicate.AbstractMatchPredicate;
import org.eclipse.kapua.service.user.UserAttributes;
import org.eclipse.kapua.service.user.UserMatchPredicate;

import java.util.Arrays;

public class UserMatchPredicateImpl<T> extends AbstractMatchPredicate<T> implements UserMatchPredicate<T> {

    /**
     * Constructor.
     *
     * @param matchTerm
     * @since 1.3.0
     */
    public UserMatchPredicateImpl(T matchTerm) {
        this.attributeNames = Arrays.asList(
                UserAttributes.NAME,
                UserAttributes.EMAIL,
                UserAttributes.PHONE_NUMBER,
                UserAttributes.DISPLAY_NAME,
                UserAttributes.EXTERNAL_ID,
                UserAttributes.DESCRIPTION
        );
        this.matchTerm = matchTerm;
    }

}
