/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authentication.shared.service;

import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtScratchCode;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("scratchCodes")
public interface GwtScratchCodeService extends RemoteService {

    List<GwtScratchCode> findByMfaCredentialOptionsId(String scopeIdStr, String mfaCredentialOptionsIdStr, boolean selfManagement)
            throws GwtKapuaException;

    List<GwtScratchCode> createScratchCodes(GwtXSRFToken xsrfToken, String scopeIdStr, String mfaCredentialOptionsIdStr, boolean selfManagement)
            throws GwtKapuaException;

}
