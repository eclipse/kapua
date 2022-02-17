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
package org.eclipse.kapua.service.account;

import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.account.xml.AccountXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link Account} {@link KapuaListResult} definition.
 *
 * @see KapuaListResult
 * @since 1.0.0
 */
@XmlRootElement(name = "accountListResult")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = AccountXmlRegistry.class, factoryMethod = "newAccountListResult")
public interface AccountListResult extends KapuaListResult<Account> {

}
