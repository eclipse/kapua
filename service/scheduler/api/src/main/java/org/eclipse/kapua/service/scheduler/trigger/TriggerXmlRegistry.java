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

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link Trigger} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class TriggerXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final TriggerFactory TRIGGER_FACTORY = LOCATOR.getFactory(TriggerFactory.class);

    /**
     * Creates a new schedule instance
     *
     * @return
     */
    public Trigger newTrigger() {
        return TRIGGER_FACTORY.newEntity(null);
    }

    /**
     * Creates a new schedule creator instance
     *
     * @return
     */
    public TriggerCreator newTriggerCreator() {
        return TRIGGER_FACTORY.newCreator(null);
    }

    /**
     * Creates a new schedule list result instance
     *
     * @return
     */
    public TriggerListResult newTriggerListResult() {
        return TRIGGER_FACTORY.newListResult();
    }

    public TriggerQuery newQuery() {
        return TRIGGER_FACTORY.newQuery(null);
    }
}
