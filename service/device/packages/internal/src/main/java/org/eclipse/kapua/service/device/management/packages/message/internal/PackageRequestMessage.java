package org.eclipse.kapua.service.device.management.packages.message.internal;

import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.device.management.request.KapuaRequestMessage;

/**
 * Package request message.
 * 
 * @since 1.0
 *
 */
public class PackageRequestMessage extends KapuaMessageImpl<PackageRequestChannel, PackageRequestPayload>
        implements KapuaRequestMessage<PackageRequestChannel, PackageRequestPayload> {

    @SuppressWarnings("unchecked")
    @Override
    public Class<PackageRequestMessage> getRequestClass() {
        return PackageRequestMessage.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<PackageResponseMessage> getResponseClass() {
        return PackageResponseMessage.class;
    }

}
