/*******************************************************************************
* Copyright (c) 2011, 2016 Eurotech and/or its affiliates                       
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
export default class DeviceDetailCtrl {
  private device: Device;
  private devicePackages: DevicePackages;
  private deviceBundles: DeviceBundles;
  private deviceEvents: DeviceEvents;
  private deviceCommands: DeviceCommands;
  private deviceConfigurations: DeviceConfigurations;
  private packagesStatus = {
   "title": "Installed Packages",
   "count": 0,
   "href": null,
   "iconClass": "fa fa-cubes",
   "notifications": [
     {
       "iconClass": "pficon pficon-error-circle-o",
       "count": 4,
       "href": "#"
     },
     {
       "iconClass": "pficon pficon-warning-triangle-o",
       "count": 1
     }
   ]
 };
 private bundlesStatus = {
   "title": "Bundles",
   "count": 0,
   "href": null,
   "iconClass": "pficon pficon-builder-image",
   "notifications": [
     {
       "iconClass": "pficon pficon-error-circle-o",
       "count": 5,
       "href": "#"
     },
     {
       "iconClass": "pficon pficon-warning-triangle-o",
       "count": 2
     }
   ]
 };
 private eventsStatus = {
   "title": "Events",
   "count": 0,
   "href": null,
   "iconClass": "pficon pficon-history",
   "notifications": [
     {
       "iconClass": "pficon pficon-error-circle-o",
       "count": 6,
       "href": "#"
     },
     {
       "iconClass": "pficon pficon-warning-triangle-o",
       "count": 3
     }
   ]
 };
 private commandsStatus = {
   "title": "Commands",
   "count": 0,
   "href": null,
   "iconClass": "pficon pficon-screen",
   "notifications": [
     {
       "iconClass": "pficon pficon-error-circle-o",
       "count": 7,
       "href": "#"
     },
     {
       "iconClass": "pficon pficon-warning-triangle-o",
       "count": 4
     }
   ]
 };
 private configurationsStatus = {
   "title": "Configurations",
   "count": 0,
   "href": null,
   "iconClass": "glyphicon glyphicon-wrench",
   "notifications": [
     {
       "iconClass": "pficon pficon-error-circle-o",
       "count": 8,
       "href": "#"
     },
     {
       "iconClass": "pficon pficon-warning-triangle-o",
       "count": 5
     }
   ]
 };

  constructor(private $stateParams: angular.ui.IStateParamsService,
              private $http: angular.IHttpService) {
    this.packagesStatus.href = `devices/${$stateParams["id"]}/packages`;
    this.bundlesStatus.href = `devices/${$stateParams["id"]}/bundles`;
    this.eventsStatus.href = `devices/${$stateParams["id"]}/events`;
    this.commandsStatus.href = `devices/${$stateParams["id"]}/commands`;
    this.configurationsStatus.href = `devices/${$stateParams["id"]}/configurations`;
    $http.get<Device>(`api/devices/${$stateParams["id"]}`).then((deviceResult) => {
      this.device = deviceResult.data;
      console.log(this.device);
    });
    $http.get<DevicePackages>(`api/devices/${$stateParams["id"]}/packages`).then((packagesResult) => {
      this.devicePackages = packagesResult.data;
      this.packagesStatus.count = packagesResult.data.devicePackage.length;
    });
    $http.get<DeviceBundles>(`api/devices/${$stateParams["id"]}/bundles`).then((bundlesResult) => {
      this.deviceBundles = bundlesResult.data;
      this.bundlesStatus.count = bundlesResult.data.deviceBundle.length;
    });
    $http.get<DeviceEvents>(`api/devices/${$stateParams["id"]}/events`).then((eventsResult) => {
      this.deviceEvents = eventsResult.data;
      this.eventsStatus.count = eventsResult.data.size;
    });
     $http.get<DeviceCommands>(`api/devices/${$stateParams["id"]}/commands`).then((commandsResult) => {
      this.deviceCommands = commandsResult.data;
      this.commandsStatus.count = commandsResult.data.deviceCommand.length;
    });
     $http.get<DeviceConfigurations>(`api/devices/${$stateParams["id"]}/configurations`).then((configurationsResult) => {
      this.deviceConfigurations = configurationsResult.data;
      this.configurationsStatus.count = configurationsResult.data.deviceConfiguration.length;
    });
  }
}