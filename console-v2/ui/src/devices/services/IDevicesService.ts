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
interface IDevicesService {
    getDeviceById(deviceID: string): ng.IHttpPromise<Device>;
    getBundlesByDeviceId(deviceID: string): ng.IHttpPromise<DeviceBundles>;
    getPackagesByDeviceId(deviceID: string): ng.IHttpPromise<DevicePackages>;
    startDeviceBundle(deviceID: string, bundleID: number): ng.IHttpPromise<DeviceBundles>;
    stopDeviceBundle(deviceID: string, bundleID: number): ng.IHttpPromise<DeviceBundles>;
}