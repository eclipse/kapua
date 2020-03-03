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
package org.eclipse.kapua.commons.model.query.predicate;

import java.util.List;

import org.eclipse.kapua.model.query.predicate.MatchPredicate;

/**
 * {@link MatchPredicate} implementation.
 *
 * @since 1.2.0
 */
public class AbstractMatchPredicate<T> implements MatchPredicate<T> {

    protected List<String> attributeNames;
    protected T matchTerm;

    @Override
    public List<String> getAttributeNames() {
        return attributeNames;
    }

    @Override
    public T getMatchTerm() {
        return matchTerm;
    }

}
