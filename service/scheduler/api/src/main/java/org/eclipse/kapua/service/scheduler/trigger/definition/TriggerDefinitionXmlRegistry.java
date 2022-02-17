/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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

    public TriggerDefinition newEntity() {
        return TRIGGER_DEFINITION_FACTORY.newEntity(null);
    }

    public TriggerDefinitionCreator newCreator() {
        return TRIGGER_DEFINITION_FACTORY.newCreator(null);
    }

    public TriggerDefinitionListResult newListResult() {
        return TRIGGER_DEFINITION_FACTORY.newListResult();
    }

    public TriggerDefinitionQuery newQuery() {
        return TRIGGER_DEFINITION_FACTORY.newQuery(null);
    }
}
