/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link Job} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class JobXmlRegistry {

    private final JobFactory jobFactory = KapuaLocator.getInstance().getFactory(JobFactory.class);

    /**
     * Creates a new job instance
     *
     * @return
     */
    public Job newJob() {
        return jobFactory.newEntity(null);
    }

    /**
     * Creates a new job creator instance
     *
     * @return
     */
    public JobCreator newJobCreator() {
        return jobFactory.newCreator(null);
    }

    /**
     * Creates a new job list result instance
     *
     * @return
     */
    public JobListResult newJobListResult() {
        return jobFactory.newListResult();
    }

    public JobQuery newQuery() {
        return jobFactory.newQuery(null);
    }
}
