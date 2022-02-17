/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystore;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystoreCertificate;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystoreCsr;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystoreItem;
import org.eclipse.kapua.app.console.module.device.shared.model.management.keystore.GwtDeviceKeystoreKeypair;

import java.util.List;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("deviceManagementKeystore")
public interface GwtDeviceKeystoreManagementService extends RemoteService {

    List<GwtDeviceKeystore> getKeystores(String scopeIdString, String deviceIdString) throws GwtKapuaException;

    List<GwtDeviceKeystoreItem> getKeystoreItems(String scopeIdString, String deviceIdString) throws GwtKapuaException;

    List<GwtDeviceKeystoreItem> getKeystoreItems(String scopeIdString, String deviceIdString, String keystoreId) throws GwtKapuaException;

    GwtDeviceKeystoreItem getKeystoreItem(String scopeIdString, String deviceIdString, String keystoreId, String alias) throws GwtKapuaException;

    void createKeystoreCertificateRaw(GwtXSRFToken xsrfToken, String scopeIdString, String deviceIdString, GwtDeviceKeystoreCertificate gwtKeystoreCertificate) throws GwtKapuaException;

    void createKeystoreCertificateInfo(GwtXSRFToken xsrfToken, String scopeIdString, String deviceIdString, String keystoreId, String alias, String certificateInfoIdString) throws GwtKapuaException;

    void createKeystoreKeypair(GwtXSRFToken xsrfToken, String scopeIdString, String deviceIdString, GwtDeviceKeystoreKeypair gwtKeystoreKeypair) throws GwtKapuaException;

    GwtDeviceKeystoreCertificate createKeystoreCsr(GwtXSRFToken xsrfToken, String scopeIdString, String deviceIdString, GwtDeviceKeystoreCsr gwtDeviceKeystoreCsr) throws GwtKapuaException;

    void deleteKeystoreItem(GwtXSRFToken xsrfToken, String scopeIdString, String deviceIdString, String keystoreId, String alias) throws GwtKapuaException;
}
