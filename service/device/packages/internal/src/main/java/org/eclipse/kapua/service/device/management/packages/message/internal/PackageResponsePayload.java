package org.eclipse.kapua.service.device.management.packages.message.internal;

import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponsePayloadImpl;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadStatus;

public class PackageResponsePayload extends KapuaResponsePayloadImpl implements KapuaPayload {

    public void setPackageDownloadOperationId(KapuaId operationId) {
        if (operationId != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID.getValue(), operationId);
        }
    }

    public KapuaId getPackageDownloadOperationId() {
        return (KapuaId) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID.getValue());
    }

    public void setPackageDownloadOperationStatus(DevicePackageDownloadStatus status) {
        if (status != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_STATUS.getValue(), status);
        }
    }

    public DevicePackageDownloadStatus getPackageDownloadOperationStatus() {
        return (DevicePackageDownloadStatus) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_STATUS.getValue());
    }

    public void setPackageDownloadOperationSize(Integer size) {
        if (size != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_SIZE.getValue(), size);
        }
    }

    public Integer getPackageDownloadOperationSize() {
        return (Integer) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_SIZE.getValue());
    }

    public void setPackageDownloadOperationProgress(Integer progress) {
        if (progress != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PROGRESS.getValue(), progress);
        }
    }

    public Integer getPackageDownloadOperationProgress() {
        return (Integer) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PROGRESS.getValue());
    }
}
