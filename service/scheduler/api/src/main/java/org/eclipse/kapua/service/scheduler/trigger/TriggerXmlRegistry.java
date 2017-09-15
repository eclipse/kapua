/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.trigger;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * Trigger xml factory class
 * 
 * @since 1.0
 *
 */
@XmlRegistry
public class TriggerXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final TriggerFactory factory = locator.getFactory(TriggerFactory.class);

    /**
     * Creates a new schedule instance
     * 
     * @return
     */
    public Trigger newTrigger() {
        return factory.newEntity(null);
    }

    /**
     * Creates a new schedule creator instance
     * 
     * @return
     */
    public TriggerCreator newTriggerCreator() {
        return factory.newCreator(null);
    }

    /**
     * Creates a new schedule list result instance
     * 
     * @return
     */
    public TriggerListResult newTriggerListResult() {
        return factory.newListResult();
    }

    public TriggerQuery newQuery() {
        return factory.newQuery(null);
    }
}
