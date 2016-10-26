package org.eclipse.kapua.service.device.management.snapshot;

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * Device bundle xml factory class
 * 
 * @since 1.0
 *
 */
@XmlRegistry
public class DeviceSnapshotXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceSnapshotFactory factory = locator.getFactory(DeviceSnapshotFactory.class);
    
    /**
     * Creates a new device snapshots list
     * 
     * @return
     */
    public DeviceSnapshots newDeviceSnapshots() {
        return factory.newDeviceSnapshots();
    }
    
    /**
     * Creates a new device snapshot
     * 
     * @return
     */
    public DeviceSnapshot newDeviceSnapshot() {
        return factory.newDeviceSnapshot();
    }
}
