/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.populators;

import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Set;

public class DataPopulatorRunner implements KapuaService {

    private final Set<DataPopulator> populators;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    public DataPopulatorRunner(Set<DataPopulator> populators) {
        this.populators = populators;
    }

    public void runPopulators() {
        for (final DataPopulator populator : populators) {
            try {
                populator.populate();
            } catch (Exception ex) {
                logger.error("Failed running populator {}:", populator.getClass().getName(), ex);
            }
        }
    }
}
