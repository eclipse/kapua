/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.domain.Domain;

/**
 * Available {@link Domain}s for {@code kapua-job-api} module.
 *
 * @since 1.0.0
 */
public class JobDomains {

    private JobDomains() {
    }

    /**
     * @see JobDomain
     * @since 1.0.0
     */
    public static final JobDomain JOB_DOMAIN = new JobDomain();
}
