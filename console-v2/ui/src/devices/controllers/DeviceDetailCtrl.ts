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
  private packagesStatus = {
   "title": "Installed Packages",
   "count": 0,
   "href": "#",
   "iconClass": "fa fa-shield",
   "notifications": [
     {
       "iconClass":"pficon pficon-error-circle-o",
       "count": 4,
       "href": "#"
     },
     {
       "iconClass":"pficon pficon-warning-triangle-o",
       "count": 1
     }
   ]
 };

  constructor(private $stateParams: angular.ui.IStateParamsService,
              private $http: angular.IHttpService) {
    $http.get<Device>(`api/devices/${$stateParams["id"]}`).then((deviceResult) => {
      this.device = deviceResult.data;
      console.log(this.device);
    });
    $http.get<DevicePackages>(`api/devices/${$stateParams["id"]}/packages`).then((packagesResult) => {
      this.devicePackages = packagesResult.data;
      this.packagesStatus.count = packagesResult.data.devicePackage.length;
    });
  }
}