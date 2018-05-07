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
package org.eclipse.kapua.service;

import org.eclipse.kapua.model.domain.Domain;

/**
 * {@link KapuaService} extension to make a {@link KapuaEntityService} able to return its entity {@link Domain}
 *
 * @since 1.0
 */
public interface KapuaDomainService<D extends Domain> extends KapuaService {

    D getServiceDomain();
}
