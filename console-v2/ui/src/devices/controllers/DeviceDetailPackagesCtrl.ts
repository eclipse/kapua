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
export default class DeviceDetailPackagesCtrl {
  private deviceId: string;
  private packages: DevicePackage[];

  constructor(private $stateParams: angular.ui.IStateParamsService,
    private $scope: any,
    private $http: angular.IHttpService,
    private devicesService: IDevicesService) {
    $scope.deviceId = $stateParams["id"];

    this.getPackages($scope.deviceId);

    let uninstallPackage = function (action, item) {
      alert("Ininstalling: " + item.name);
      // devicesService.uninstallPackage($scope.deviceId, item);
    }

    $scope.actionButtons = [
      {
        name: 'Uninstall',
        class: 'btn-primary',
        title: 'Uninstall package',
        actionFn: uninstallPackage
      }
    ];
  }

  private config = {
    selectedItems: false,
    useExpandingRows: true,
    showSelectBox: false
  };

  installPackage(packageItem: any): void {
    alert("Installing item: " + packageItem.name);
    // this.devicesService.uninstallPackage(this.$scope.deviceId, packageItem);
  }
  deletePackage(packageItem: any): void {
    alert("Deleting item: " + packageItem.name);
  }

  getPackages(deviceID: string): void {
    this.devicesService.getPackagesByDeviceId(deviceID).then((responseData: ng.IHttpPromiseCallbackArg<DevicePackages>) => {
      this.$scope.packages = responseData.data.devicePackage;
      this.$scope.packages.forEach(devPackage => {
        if (!devPackage.bundleInfos.bundleInfo.length)
          devPackage.disableRowExpansion = true;
      });
    });
  }
}