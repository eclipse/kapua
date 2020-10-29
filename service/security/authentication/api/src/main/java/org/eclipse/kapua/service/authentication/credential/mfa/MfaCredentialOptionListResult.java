/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.mfa;

import org.eclipse.kapua.model.query.KapuaListResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * MfaCredentialOption list result definition.
 */
@XmlRootElement(name = "mfaCredentialOptionListResult")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = MfaCredentialOptionXmlRegistry.class, factoryMethod = "newMfaCredentialOptionListResult")
public interface MfaCredentialOptionListResult extends KapuaListResult<MfaCredentialOption> {

}
