package org.eclipse.kapua.service.device.management.packages.message.internal;

import java.net.URI;

import org.eclipse.kapua.message.internal.KapuaPayloadImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.request.KapuaRequestPayload;

public class PackageRequestPayload extends KapuaPayloadImpl implements KapuaRequestPayload
{
    public KapuaId getOperationId()
    {
        return (KapuaId) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID.getValue());
    }

    public void setOperationId(KapuaId operationId)
    {
        if (operationId != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID.getValue(), operationId);
        }
    }

    public Boolean isReboot()
    {
        return (Boolean) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT.getValue());
    }

    public void setReboot(Boolean reboot)
    {
        if (reboot != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT.getValue(), reboot);
        }
    }

    public Integer getRebootDelay()
    {
        return (Integer) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT_DELAY.getValue());
    }

    public void setRebootDelay(Integer rebootDelay)
    {
        if (rebootDelay != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_REBOOT_DELAY.getValue(), rebootDelay);
        }
    }

    //
    // Download
    //
    public URI getPackageDownloadURI()
    {
        return (URI) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_URI.getValue());
    }

    public void setPackageDownloadURI(URI uri)
    {
        if (uri != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_URI.getValue(), uri);
        }
    }

    public String getPackageDownloadName()
    {
        return (String) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_NAME.getValue());
    }

    public void setPackageDownloadName(String packageName)
    {
        if (packageName != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_NAME.getValue(), packageName);
        }
    }

    public String getPackageDownloadVersion()
    {
        return (String) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_VERSION.getValue());
    }

    public void setPackageDownloadVersion(String packageVersion)
    {
        if (packageVersion != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_VERSION.getValue(), packageVersion);
        }
    }

    public Boolean isPackageDownloadnstall()
    {
        return (Boolean) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL.getValue());
    }

    public void setPackageDownloadnstall(Boolean packageDownloadnstall)
    {
        if (packageDownloadnstall != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PACKAGE_INSTALL.getValue(), packageDownloadnstall);
        }
    }

    //
    // Install
    //
    public String getPackageInstallName()
    {
        return (String) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME.getValue());
    }

    public void setPackageInstallName(String packageName)
    {
        if (packageName != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_NAME.getValue(), packageName);
        }
    }

    public String getPackageInstallVersion()
    {
        return (String) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION.getValue());
    }

    public void setPackageInstallVersion(String packageVersion)
    {
        if (packageVersion != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_INSTALL_PACKAGE_VERSION.getValue(), packageVersion);
        }
    }

    //
    // Uninstall
    //
    public String getPackageUninstallName()
    {
        return (String) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME.getValue());
    }

    public void setPackageUninstallName(String packageName)
    {
        if (packageName != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_NAME.getValue(), packageName);
        }
    }

    public String getPackageUninstallVersion()
    {
        return (String) getProperties().get(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION.getValue());
    }

    public void setPackageUninstallVersion(String packageVersion)
    {
        if (packageVersion != null) {
            getProperties().put(PackageAppProperties.APP_PROPERTY_PACKAGE_UNINSTALL_PACKAGE_VERSION.getValue(), packageVersion);
        }
    }

    //
    // Utility methods
    //
    public boolean isDownloadRequest()
    {
        return getPackageDownloadName() != null;
    }

    public boolean isInstallRequest()
    {
        return getPackageInstallName() != null;
    }

    public boolean isUninstallRequest()
    {
        return getPackageUninstallName() != null;
    }
}
