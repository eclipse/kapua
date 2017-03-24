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
package org.eclipse.kapua.service.datastore.model.query;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.service.datastore.ClientInfoXmlRegistry;
import org.eclipse.kapua.service.datastore.model.ClientInfo;

/**
 * Client information schema query definition
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "query")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = ClientInfoXmlRegistry.class, factoryMethod = "newQuery")
public interface ClientInfoQuery extends StorableQuery<ClientInfo> {

}
