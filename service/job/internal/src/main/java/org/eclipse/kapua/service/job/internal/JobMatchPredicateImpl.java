/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job.internal;

import org.eclipse.kapua.commons.model.query.predicate.AbstractMatchPredicate;
import org.eclipse.kapua.service.job.JobAttributes;
import org.eclipse.kapua.service.job.JobMatchPredicate;

import java.util.Arrays;

public class JobMatchPredicateImpl<T> extends AbstractMatchPredicate<T> implements JobMatchPredicate<T> {

    /**
     * Constructor.
     *
     * @param matchTerm
     * @since 2.0.0
     */
    public JobMatchPredicateImpl(T matchTerm) {
        this.attributeNames = Arrays.asList(
                JobAttributes.NAME,
                JobAttributes.DESCRIPTION
        );
        this.matchTerm = matchTerm;
    }

}