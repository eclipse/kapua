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

    private final TriggerDefinitionFactory triggerDefinitionFactory = KapuaLocator.getInstance().getFactory(TriggerDefinitionFactory.class);

    public TriggerDefinition newEntity() {
        return triggerDefinitionFactory.newEntity(null);
    }

    public TriggerDefinitionCreator newCreator() {
        return triggerDefinitionFactory.newCreator(null);
    }

    public TriggerDefinitionListResult newListResult() {
        return triggerDefinitionFactory.newListResult();
    }

    public TriggerDefinitionQuery newQuery() {
        return triggerDefinitionFactory.newQuery(null);
    }
}
