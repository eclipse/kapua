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
package org.eclipse.kapua.service.device.management.keystore;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.info.CertificateInfo;
import org.eclipse.kapua.service.certificate.info.CertificateInfoService;
import org.eclipse.kapua.service.device.management.DeviceManagementService;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystore;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSR;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSRInfo;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCertificate;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItem;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItemQuery;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItems;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreKeypair;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystores;
import org.eclipse.kapua.service.device.registry.Device;

/**
 * {@link DeviceKeystore} {@link DeviceManagementService} definition.
 *
 * @see DeviceManagementService
 * @since 1.5.0
 */
public interface DeviceKeystoreManagementService extends DeviceManagementService {

    /**
     * Gets the {@link DeviceKeystores} present on the {@link Device}.
     *
     * @param scopeId  The {@link Device#getScopeId()}.
     * @param deviceId The {@link Device#getId()}.
     * @param timeout  The timeout waiting for the device response.
     * @return The {@link DeviceKeystores} retrieved from the {@link Device}.
     * @throws KapuaException
     * @since 1.5.0
     */
    DeviceKeystores getKeystores(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException;

    /**
     * Gets the {@link DeviceKeystoreItems} present on the {@link Device}.
     *
     * @param scopeId  The {@link Device#getScopeId()}.
     * @param deviceId The {@link Device#getId()}.
     * @param timeout  The timeout waiting for the device response.
     * @return The {@link DeviceKeystoreItems} retrieved from the {@link Device}.
     * @throws KapuaException
     * @since 1.5.0
     */
    DeviceKeystoreItems getKeystoreItems(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException;

    /**
     * Gets the {@link DeviceKeystoreItems} present on the {@link Device}.
     *
     * @param scopeId   The {@link Device#getScopeId()}.
     * @param deviceId  The {@link Device#getId()}.
     * @param itemQuery The {@link DeviceKeystoreItemQuery} to filter results.
     * @param timeout   The timeout waiting for the device response.
     * @return The {@link DeviceKeystoreItems} retrieved from the {@link Device}.
     * @throws KapuaException
     * @since 1.5.0
     */
    DeviceKeystoreItems getKeystoreItems(KapuaId scopeId, KapuaId deviceId, DeviceKeystoreItemQuery itemQuery, Long timeout) throws KapuaException;

    /**
     * Gets a {@link DeviceKeystoreItem} present on the {@link Device}.
     *
     * @param scopeId    The {@link Device#getScopeId()}.
     * @param deviceId   The {@link Device#getId()}.
     * @param keystoreId The {@link DeviceKeystore#getId()}
     * @param alias      The {@link DeviceKeystoreItem#getAlias()}
     * @param timeout    The timeout waiting for the device response.
     * @return The {@link DeviceKeystoreItem} retrieved from the {@link Device}.
     * @throws KapuaException
     */
    DeviceKeystoreItem getKeystoreItem(KapuaId scopeId, KapuaId deviceId, String keystoreId, String alias, Long timeout) throws KapuaException;

    /**
     * Creates a {@link CertificateInfo} as {@link DeviceKeystoreCertificate} into the {@link Device}.
     * <p>
     * It uses the {@link CertificateInfoService} to get the {@link CertificateInfo} to send.
     *
     * @param scopeId       The {@link Device#getScopeId()}.
     * @param deviceId      The {@link Device#getId()}.
     * @param keystoreId    The {@link DeviceKeystore#getId()}.
     * @param alias         The {@link DeviceKeystoreItem#getAlias()}.
     * @param certificateId The {@link CertificateInfo#getId()} to send.
     * @param timeout       The timeout waiting for the device response.
     * @throws KapuaException
     */
    void createKeystoreCertificate(KapuaId scopeId, KapuaId deviceId, String keystoreId, String alias, KapuaId certificateId, Long timeout) throws KapuaException;

    /**
     * Creates a {@link DeviceKeystoreCertificate} into the {@link Device}
     *
     * @param scopeId             The {@link Device#getScopeId()}.
     * @param deviceId            The {@link Device#getId()}.
     * @param keystoreCertificate The {@link DeviceKeystoreCertificate} to create.
     * @param timeout             The timeout waiting for the device response.
     * @throws KapuaException
     */
    void createKeystoreCertificate(KapuaId scopeId, KapuaId deviceId, DeviceKeystoreCertificate keystoreCertificate, Long timeout) throws KapuaException;

    /**
     * Creates a {@link DeviceKeystoreKeypair} into the {@link Device}
     *
     * @param scopeId         The {@link Device#getScopeId()}.
     * @param deviceId        The {@link Device#getId()}.
     * @param keystoreKeypair The {@link DeviceKeystoreKeypair} to create.
     * @param timeout         The timeout waiting for the device response.
     * @throws KapuaException
     */
    void createKeystoreKeypair(KapuaId scopeId, KapuaId deviceId, DeviceKeystoreKeypair keystoreKeypair, Long timeout) throws KapuaException;

    /**
     * Sends a {@link DeviceKeystoreCSRInfo} into the {@link Device}
     *
     * @param scopeId         The {@link Device#getScopeId()}.
     * @param deviceId        The {@link Device#getId()}.
     * @param keystoreCSRInfo The {@link DeviceKeystoreCSRInfo} to send.
     * @param timeout         The timeout waiting for the device response.
     * @return The {@link DeviceKeystoreCSR} returned from the {@link Device}
     * @throws KapuaException
     */
    DeviceKeystoreCSR createKeystoreCSR(KapuaId scopeId, KapuaId deviceId, DeviceKeystoreCSRInfo keystoreCSRInfo, Long timeout) throws KapuaException;

    /**
     * Deletes a {@link DeviceKeystoreItem} present on the {@link Device}.
     *
     * @param scopeId    The {@link Device#getScopeId()}.
     * @param deviceId   The {@link Device#getId()}.
     * @param keystoreId The {@link DeviceKeystoreItem#getKeystoreId()}
     * @param alias      The {@link DeviceKeystoreItem#getAlias()}
     * @param timeout    The timeout waiting for the device response.
     * @throws KapuaException
     */
    void deleteKeystoreItem(KapuaId scopeId, KapuaId deviceId, String keystoreId, String alias, Long timeout) throws KapuaException;

}
