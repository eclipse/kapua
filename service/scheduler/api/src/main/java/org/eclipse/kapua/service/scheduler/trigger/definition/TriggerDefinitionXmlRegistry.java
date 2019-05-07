/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.trigger.definition;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link TriggerDefinition} xml factory class
 *
 * @since 1.1.0
 */
@XmlRegistry
public class TriggerDefinitionXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final TriggerDefinitionFactory TRIGGER_DEFINITION_FACTORY = LOCATOR.getFactory(TriggerDefinitionFactory.class);

    public TriggerDefinition newTriggerDefinition() {
        return TRIGGER_DEFINITION_FACTORY.newEntity(null);
    }

    public TriggerDefinitionCreator newTriggerDefinitionCreator() {
        return TRIGGER_DEFINITION_FACTORY.newCreator(null);
    }

    public TriggerDefinitionListResult newListResult() {
        return TRIGGER_DEFINITION_FACTORY.newListResult();
    }

    public TriggerDefinitionQuery newQuery() {
        return TRIGGER_DEFINITION_FACTORY.newQuery(null);
    }
}
