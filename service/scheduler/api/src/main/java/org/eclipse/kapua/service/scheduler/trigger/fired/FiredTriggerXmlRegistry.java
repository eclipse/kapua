/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.trigger.fired;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link FiredTrigger} xml factory class.
 *
 * @since 1.5.0
 */
@XmlRegistry
public class FiredTriggerXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final FiredTriggerFactory FIRED_TRIGGER_FACTORY = LOCATOR.getFactory(FiredTriggerFactory.class);

    public FiredTrigger newEntity() {
        return FIRED_TRIGGER_FACTORY.newEntity(null);
    }

    public FiredTriggerCreator newCreator() {
        return FIRED_TRIGGER_FACTORY.newCreator(null);
    }

    public FiredTriggerListResult newListResult() {
        return FIRED_TRIGGER_FACTORY.newListResult();
    }

    public FiredTriggerQuery newQuery() {
        return FIRED_TRIGGER_FACTORY.newQuery(null);
    }
}
