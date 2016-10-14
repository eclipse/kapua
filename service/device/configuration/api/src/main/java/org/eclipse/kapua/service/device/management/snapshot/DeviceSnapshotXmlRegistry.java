package org.eclipse.kapua.service.device.management.snapshot;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

@XmlRegistry
public class DeviceSnapshotXmlRegistry {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceSnapshotFactory factory = locator.getFactory(DeviceSnapshotFactory.class);
    
    public DeviceSnapshots newDeviceSnapshots() {
        return factory.newDeviceSnapshots();
    }
    
    public DeviceSnapshot newDeviceSnapshot() {
        return factory.newDeviceSnapshot();
    }
}
