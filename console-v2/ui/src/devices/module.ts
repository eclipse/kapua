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

import DevicesListCtrl from "./controllers/DevicesListCtrl";
import DeleteDevicesModalCtrl from "./controllers/DeleteDevicesModalCtrl";
import DeviceDetailCtrl from "./controllers/DeviceDetailCtrl";

import "./assets/styles/devices.scss";

angular.module("app.devices", [])
    .config(["$stateProvider",
        ($stateProvider: angular.ui.IStateProvider,
            $authProvider) => {
            $stateProvider
                .state("kapua.devices", {
                    url: "/devices",
                    views: {
                        "kapuaView@kapua": {
                            template: require("./views/devices-list.html"),
                            controller: "DevicesListCtrl as vm"
                        }
                    }
                })
                .state("kapua.devices.detail", {
                    url: "/:id",
                    views: {
                        "kapuaView@kapua": {
                            template: require("./views/device-detail.html"),
                            controller: "DeviceDetailCtrl as vm"
                        }
                    }
                });
        }])
    .controller("DevicesListCtrl", ["$http", "$modal", DevicesListCtrl])
    .controller("DeleteDevicesModalCtrl", ["$modalInstance", "$http", "id", DeleteDevicesModalCtrl])
    .controller("DeviceDetailCtrl", ["$stateParams", DeviceDetailCtrl]);