/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.domain;

import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.model.query.KapuaListResult;

/**
 * {@link Domain} list result definition.
 * 
 * @since 1.0.0
 */
@XmlRootElement(name = "domains")
public interface DomainListResult extends KapuaListResult<Domain> {

}
