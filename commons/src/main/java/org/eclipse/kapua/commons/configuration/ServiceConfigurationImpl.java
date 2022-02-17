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
package org.eclipse.kapua.commons.configuration;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.config.ServiceComponentConfiguration;
import org.eclipse.kapua.service.config.ServiceConfiguration;

/**
 * Service configuration entity implementation.
 *
 * @since 1.0
 */
public class ServiceConfigurationImpl implements ServiceConfiguration {

    private static final long serialVersionUID = -2167999497954676423L;

    private List<ServiceComponentConfiguration> configurations;

    public ServiceConfigurationImpl() {
        configurations = new ArrayList<>();
    }

    @Override
    public List<ServiceComponentConfiguration> getComponentConfigurations() {
        return configurations;
    }

}
