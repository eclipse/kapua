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
package org.eclipse.kapua.model.query.predicate;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class PredicateXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final PredicateFactory PREDICATE_FACTORY = LOCATOR.getFactory(PredicateFactory.class);

    public AttributePredicate newAttributePredicate() {
        return PREDICATE_FACTORY.attributePredicate(null, null);
    }

    public AndPredicate newAndPredicate() {
        return PREDICATE_FACTORY.andPredicate();
    }

}
