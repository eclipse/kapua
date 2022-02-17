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
package org.eclipse.kapua.service.device.management.packages.message.internal;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponsePayloadImpl;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;
import org.eclipse.kapua.service.device.management.packages.DevicePackageFactory;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadStatus;
import org.eclipse.kapua.service.device.management.packages.model.install.DevicePackageInstallOperation;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOperation;

import javax.validation.constraints.NotNull;

/**
 * {@link DevicePackages} {@link KapuaResponsePayload} implementation.
 *
 * @since 1.0.0
 */
public class PackageResponsePayload extends KapuaResponsePayloadImpl implements KapuaResponsePayload {

    private static final long serialVersionUID = -2100712552502696907L;

    private static final String CHAR_ENCODING = DeviceManagementSetting.getInstance().getString(DeviceManagementSettingKey.CHAR_ENCODING);

    private static final DevicePackageFactory DEVICE_PACKAGE_FACTORY = KapuaLocator.getInstance().getFactory(DevicePackageFactory.class);

    /**
     * Set the package download operation identifier
     *
     * @param operationId
     * @since 1.0.0
     */
    public void setPackageDownloadOperationId(KapuaId operationId) {
        if (operationId != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID.getValue(), operationId);
        }
    }

    /**
     * Get the package download operation identifier
     *
     * @return
     * @since 1.0.0
     */
    public KapuaId getPackageDownloadOperationId() {
        return (KapuaId) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID.getValue());
    }

    /**
     * Set the package download operation status
     *
     * @param status
     * @since 1.0.0
     */
    public void setPackageDownloadOperationStatus(DevicePackageDownloadStatus status) {
        if (status != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_STATUS.getValue(), status);
        }
    }

    /**
     * Get the package download operation status
     *
     * @return
     * @since 1.0.0
     */
    public DevicePackageDownloadStatus getPackageDownloadOperationStatus() {
        return (DevicePackageDownloadStatus) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_STATUS.getValue());
    }

    /**
     * Get the package download size
     *
     * @param size
     * @since 1.0.0
     */
    public void setPackageDownloadOperationSize(Integer size) {
        if (size != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_SIZE.getValue(), size);
        }
    }

    /**
     * Set the package download size
     *
     * @return
     * @since 1.0.0
     */
    public Integer getPackageDownloadOperationSize() {
        return (Integer) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_SIZE.getValue());
    }

    /**
     * Set the package download progress
     *
     * @param progress
     * @since 1.0.0
     */
    public void setPackageDownloadOperationProgress(Integer progress) {
        if (progress != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PROGRESS.getValue(), progress);
        }
    }

    /**
     * Get the package download progress
     *
     * @return
     * @since 1.0.0
     */
    public Integer getPackageDownloadOperationProgress() {
        return (Integer) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PROGRESS.getValue());
    }

    /**
     * Gets the {@link DevicePackages} from the {@link #getBody()}.
     *
     * @return The {@link DevicePackages} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DevicePackages getDevicePackages() throws Exception {
        if (!hasBody()) {
            return DEVICE_PACKAGE_FACTORY.newDeviceDeploymentPackages();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DevicePackages.class);
    }

    /**
     * Sets the {@link DevicePackages} in the {@link #getBody()}.
     *
     * @param devicePackages The {@link DevicePackages} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setDeploymentPackages(@NotNull DevicePackages devicePackages) throws Exception {
        String bodyString = XmlUtil.marshal(devicePackages);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    /**
     * Gets the {@link DevicePackageInstallOperation} from the {@link #getBody()}.
     *
     * @return The {@link DevicePackageInstallOperation} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DevicePackageInstallOperation getDevicePackageInstallOperation() throws Exception {
        if (!hasBody()) {
            return DEVICE_PACKAGE_FACTORY.newPackageInstallOperation();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DevicePackageInstallOperation.class);
    }

    /**
     * Sets the {@link DevicePackageInstallOperation} in the {@link #getBody()}.
     *
     * @param devicePackageInstallOperation The {@link DevicePackages} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setDevicePackageInstallOperations(@NotNull DevicePackageInstallOperation devicePackageInstallOperation) throws Exception {
        String bodyString = XmlUtil.marshal(devicePackageInstallOperation);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    /**
     * Gets the {@link DevicePackageUninstallOperation} from the {@link #getBody()}.
     *
     * @return The {@link DevicePackageUninstallOperation} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DevicePackageUninstallOperation getDevicePackageUninstallOperation() throws Exception {
        if (!hasBody()) {
            return DEVICE_PACKAGE_FACTORY.newPackageUninstallOperation();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DevicePackageUninstallOperation.class);
    }

    /**
     * Sets the {@link DevicePackageUninstallOperation} in the {@link #getBody()}.
     *
     * @param devicePackageUninstallOperation The {@link DevicePackages} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setDevicePackageUninstallOperations(@NotNull DevicePackageUninstallOperation devicePackageUninstallOperation) throws Exception {
        String bodyString = XmlUtil.marshal(devicePackageUninstallOperation);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }
}
