/*******************************************************************************
* Copyright (c) 2016, 2017 Eurotech and/or its affiliates                       
*                                                                               
* All rights reserved. This program and the accompanying materials              
* are made available under the terms of the Eclipse Public License v1.0         
* which accompanies this distribution, and is available at                      
* http://www.eclipse.org/legal/epl-v10.html                                     
*                                                                               
* Contributors:                                                                 
*     Eurotech - initial API and implementation                                 
*                                                                               
*******************************************************************************/
export default class DevicesService implements IDevicesService {

    constructor(private $http: ng.IHttpService) {
    }

    getDevices(): ng.IHttpPromise<ListResult<Device>> {
        return this.$http.get("api/_/devices?fetchAttributes=connection");
    }

    getDeviceById(deviceID: string): ng.IHttpPromise<Device> {
        return this.$http.get("/api/_/devices/" + deviceID);
    }

    getBundlesByDeviceId(deviceID: string): ng.IHttpPromise<DeviceBundles> {
        return this.$http.get("/api/_/devices/" + deviceID + "/bundles");
    };

    getPackagesByDeviceId(deviceID: string): ng.IHttpPromise<DevicePackages> {
        return this.$http.get("/api/_/devices/" + deviceID + "/packages");
    };

    getConfigsByDeviceId(deviceID: string): ng.IHttpPromise<DeviceConfigurations> {
        return this.$http.get("/api/_/devices/" + deviceID + "/configurations");
    };

    getSnapshotsByDeviceId(deviceID: string): ng.IHttpPromise<DeviceSnapshots> {
        return this.$http.get("/api/_/devices/" + deviceID + "/snapshots");
    };

    startDeviceBundle(deviceID: string, bundleID: number): ng.IHttpPromise<DeviceBundles> {
        return this.$http.post("/api/_/devices/" + deviceID + "/bundles/" + bundleID + "/_start", {});
    };

    stopDeviceBundle(deviceID: string, bundleID: number): ng.IHttpPromise<DeviceBundles> {
        return this.$http.post("/api/_/devices/" + deviceID + "/bundles/" + bundleID + "/_stop", {});
    };

    downloadPackage(deviceID: string, devicePackage: DevicePackage): any {
        let requestModel = {
            version: devicePackage.version,
            uri: "", // what is "uri"? 
            rebootDelay: 0,
            reboot: false,
            install: true,
            name: devicePackage.name
        };
        return this.$http.post("/api/_/devices/" + deviceID + "/packages/_download", requestModel);
    };

    uninstallPackage(deviceID: string, devicePackage: DevicePackage): any {
        let requestModel = {
            version: devicePackage.version,
            rebootDelay: 0,
            reboot: false,
            name: devicePackage.name
        };
        return this.$http.post("/api/_/devices/" + deviceID + "/packages/_uninstall", requestModel);
    }

    executeCommand(deviceID: string): any {
        let requestModel = {
            // TO BE defined            
        };
        return this.$http.post("/api/_/devices/" + deviceID + "/commands/_execute", requestModel);
    }

    applyConfig(config: DeviceConfiguration, deviceID: string): ng.IHttpPromise<DeviceConfigurations> {
        return this.$http.post("/api/_/devices/" + deviceID + "/configurations", config);
    }

    rollbackSnapshot(deviceID: string, snapshot: DeviceSnapshot): ng.IHttpPromise<DeviceSnapshots> {
        return this.$http.post("/api/_/devices/" + deviceID + "/snapshots/" + snapshot.id, snapshot);
    }

}