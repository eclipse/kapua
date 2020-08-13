/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.user.internal;

import java.util.Arrays;

import org.eclipse.kapua.commons.model.query.predicate.AbstractMatchPredicate;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.service.user.UserAttributes;
import org.eclipse.kapua.service.user.UserMatchPredicate;

public class UserMatchPredicateImpl<T> extends AbstractMatchPredicate<T> implements UserMatchPredicate<T> {

    /**
     * Constructor.
     *
     * @param matchTerm
     * @since 1.3.0
     */
    public UserMatchPredicateImpl(T matchTerm) {
        this.attributeNames = Arrays.asList(
                KapuaNamedEntityAttributes.NAME,
                UserAttributes.EMAIL,
                UserAttributes.PHONE_NUMBER,
                UserAttributes.DISPLAY_NAME,
                UserAttributes.EXTERNAL_ID,
                KapuaNamedEntityAttributes.DESCRIPTION
        );
        this.matchTerm = matchTerm;
    }

}
