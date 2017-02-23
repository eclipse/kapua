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
  private items: DeviceViewModel[];
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

  private listConfig = {
    selectedItems: false,
    showSelectBox: false,
    useExpandingRows: true
  };

  constructor(private $stateParams: angular.ui.IStateParamsService,
    private $http: angular.IHttpService,
    private devicesService: IDevicesService,
    private deviceMapperService: IDeviceMapperService) {
    this.packagesStatus.href = `devices/${$stateParams["id"]}/packages`;
    this.eventsStatus.href = `devices/${$stateParams["id"]}/events`;
    this.bundlesStatus.href = `devices/${$stateParams["id"]}/bundles`;
    this.configurationsStatus.href = `devices/${$stateParams["id"]}/configurations`;
    this.commandsStatus.href = `devices/${$stateParams["id"]}/commands`;
    this.getDeviceById($stateParams["id"]);
  }

  getDeviceById(deviceID): void {
    this.devicesService.getDeviceById(deviceID).then((responseData: ng.IHttpPromiseCallbackArg<Device>) => {
      this.device = responseData.data;
      this.items = this.deviceMapperService.prepareViewItems(this.device);
    });
  }


}