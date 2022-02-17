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
package org.eclipse.kapua.service.datastore.model;

import org.eclipse.kapua.service.datastore.model.xml.DatastoreMessageXmlRegistry;
import org.eclipse.kapua.service.storable.model.StorableListResult;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Message information query result list definition.<br>
 * This object contains the list of the message objects retrieved by the search service.
 *
 * @since 1.0
 */
@XmlRootElement(name = "datastoreMessages")
@XmlType(factoryClass = DatastoreMessageXmlRegistry.class, factoryMethod = "newListResult")
public interface MessageListResult extends StorableListResult<DatastoreMessage> {

}
