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
export default class DeviceDetailCtrl {
  private device: Device;
  private devicePackages: DevicePackages;
  private deviceEvents: DeviceEvents;
  private deviceBundles: DeviceBundles;
  private deviceConfigurations: DeviceConfigurations;
  private deviceCommands: DeviceCommands;
  private packagesStatus = {
    "title": "Installed Packages",
    "count": 0,
    "href": null,
    "iconClass": "fa fa-cube",
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
  private eventsStatus = {
    "title": "Events",
    "count": 1,
    "href": null,
    "iconClass": "fa fa-history",
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
  private bundlesStatus = {
    "title": "Bundles",
    "count": 2,
    "href": null,
    "iconClass": "fa fa-cubes",
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
  private configurationsStatus = {
    "title": "Configurations",
    "count": 3,
    "href": null,
    "iconClass": "fa fa-cogs",
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
  private commandsStatus = {
    "title": "Commands",
    "count": 4,
    "href": null,
    "iconClass": "fa fa-terminal",
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
    this.eventsStatus.href = `devices/${$stateParams["id"]}/events`;
    this.bundlesStatus.href = `devices/${$stateParams["id"]}/bundles`;
    this.configurationsStatus.href = `devices/${$stateParams["id"]}/configurations`; 
    this.commandsStatus.href = `devices/${$stateParams["id"]}/commands`; 
    // $http.get<Device>(`api/devices/${$stateParams["id"]}`).then((deviceResult) => {
    //   this.device = deviceResult.data;
    //   console.log(this.device);
    // });
    // $http.get<DevicePackages>(`api/devices/${$stateParams["id"]}/packages`).then((packagesResult) => {
    //   this.devicePackages = packagesResult.data;
    //   this.packagesStatus.count = packagesResult.data.devicePackage.length;
    // });
    // $http.get<DeviceEvents>(`api/devices/${$stateParams["id"]}/events`).then((eventsResult) => {
    //   this.deviceEvents = eventsResult.data;
    //   this.eventsStatus.count = eventsResult.data.deviceEvent.length;
    // });
    // $http.get<DeviceBundles>(`api/devices/${$stateParams["id"]}/bundles`).then((bundlesResult) => {
    //   this.deviceBundles = bundlesResult.data;
    //   this.bundlesStatus.count = bundlesResult.data.deviceBundle.length;
    // });
    // $http.get<DeviceConfigurations>(`api/devices/${$stateParams["id"]}/configurations`).then((configurationsResult) => {
    //   this.deviceConfigurations = configurationsResult.data;
    //   this.configurationsStatus.count = configurationsResult.data.deviceConfiguration.length;
    // });
    // $http.get<DeviceCommands>(`api/devices/${$stateParams["id"]}/commands`).then((commandsResult) => {
    //   this.deviceCommands = commandsResult.data;
    //   this.commandsStatus.count = commandsResult.data.deviceCommand.length;
    // });
  }
}