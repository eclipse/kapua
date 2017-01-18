/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.subject;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class SubjectXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final SubjectFactory factory = locator.getFactory(SubjectFactory.class);

    /**
     * Creates a new {@link Subject} instance
     * 
     * @return
     * 
     * @since 1.0.0
     */
    public Subject newSubject() {
        return factory.newSubject(null, null);
    }
}
