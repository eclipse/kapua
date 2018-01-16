/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.configuration.internal;

import org.eclipse.kapua.commons.model.query.KapuaListResultImpl;
import org.eclipse.kapua.service.configuration.ServiceConfig;
import org.eclipse.kapua.service.configuration.ServiceConfigListResult;

/**
 * Service configuration result list reference implementation.
 * 
 * @since 1.0
 * 
 */
public class ServiceConfigListResultImpl extends KapuaListResultImpl<ServiceConfig> implements ServiceConfigListResult {

    private static final long serialVersionUID = -2550359084026132096L;
}
