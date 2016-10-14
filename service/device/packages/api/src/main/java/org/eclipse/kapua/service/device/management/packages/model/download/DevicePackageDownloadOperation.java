package org.eclipse.kapua.service.device.management.packages.model.download;

import org.eclipse.kapua.model.id.KapuaId;

public interface DevicePackageDownloadOperation {

    public KapuaId getId();

    public void setId(KapuaId id);

    public Integer getSize();

    public void setSize(Integer downloadSize);

    public Integer getProgress();

    public void setProgress(Integer downloadProgress);

    public DevicePackageDownloadStatus getStatus();

    public void setStatus(DevicePackageDownloadStatus status);
}
