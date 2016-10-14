package org.eclipse.kapua.service.device.management.packages.message.internal;

import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaAppChannelImpl;
import org.eclipse.kapua.service.device.management.request.KapuaRequestChannel;

public class PackageRequestChannel extends KapuaAppChannelImpl implements KapuaRequestChannel
{
    private KapuaMethod     method;
    private PackageResource packageResource;

    @Override
    public KapuaMethod getMethod()
    {
        return method;
    }

    @Override
    public void setMethod(KapuaMethod method)
    {
        this.method = method;
    }

    public PackageResource getResource()
    {
        return packageResource;
    }

    public void setPackageResource(PackageResource packageResource)
    {
        this.packageResource = packageResource;
    }
}
