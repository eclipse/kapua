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
package org.eclipse.kapua.service.tag.internal;

import org.eclipse.kapua.commons.model.query.predicate.AbstractMatchPredicate;
import org.eclipse.kapua.service.tag.TagAttributes;
import org.eclipse.kapua.service.tag.TagMatchPredicate;
import java.util.Arrays;

public class TagMatchPredicateImpl<T> extends AbstractMatchPredicate<T> implements TagMatchPredicate<T> {

/**
 * Constructor.
 *
 * @param matchTerm
 * @since 2.1.0
 */
    public TagMatchPredicateImpl(T matchTerm) {
        this.attributeNames = Arrays.asList(
                TagAttributes.DESCRIPTION,
                TagAttributes.NAME
        );
        this.matchTerm = matchTerm;
    }
}
