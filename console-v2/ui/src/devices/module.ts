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
import DeviceDetailPackagesCtrl from "./controllers/DeviceDetailPackagesCtrl";
import DeviceDetailBundlesCtrl from "./controllers/DeviceDetailBundlesCtrl";
import DeviceDetailEventsCtrl from "./controllers/DeviceDetailEventsCtrl";
import DeviceDetailCommandsCtrl from "./controllers/DeviceDetailCommandsCtrl";
import DeviceDetailConfigurationsCtrl from "./controllers/DeviceDetailConfigurationsCtrl";


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
                            template: require("./views/device-details.html"),
                            controller: "DeviceDetailCtrl as vm"
                        }
                    }
                })
                .state("kapua.devices.detail.packages", {
                    url: "/packages",
                    views: {
                        "kapuaView@kapua": {
                            template: require("./views/device-details/packages.html"),
                            controller: "DeviceDetailPackagesCtrl as vm"
                        }
                    }
                })
                .state("kapua.devices.detail.bundles", {
                    url: "/bundles",
                    views: {
                        "kapuaView@kapua": {
                            template: require("./views/device-details/bundles.html"),
                            controller: "DeviceDetailBundlesCtrl as vm"
                        }
                    }
                })
                .state("kapua.devices.detail.events", {
                    url: "/events",
                    views: {
                        "kapuaView@kapua": {
                            template: require("./views/device-details/events.html"),
                            controller: "DeviceDetailEventsCtrl as vm"
                        }
                    }
                })
                .state("kapua.devices.detail.commands", {
                    url: "/commands",
                    views: {
                        "kapuaView@kapua": {
                            template: require("./views/device-details/commands.html"),
                            controller: "DeviceDetailCommandsCtrl as vm"
                        }
                    }
                })
                .state("kapua.devices.detail.configurations", {
                    url: "/configurations",
                    views: {
                        "kapuaView@kapua": {
                            template: require("./views/device-details/configurations.html"),
                            controller: "DeviceDetailConfigurationsCtrl as vm"
                        }
                    }
                });
        }])
    .controller("DevicesListCtrl", ["$http", "$modal", DevicesListCtrl])
    .controller("DeleteDevicesModalCtrl", ["$modalInstance", "$http", "id", DeleteDevicesModalCtrl])
    .controller("DeviceDetailCtrl", ["$stateParams", "$http", DeviceDetailCtrl])
    .controller("DeviceDetailPackagesCtrl", ["$stateParams", "$http", DeviceDetailPackagesCtrl])
    .controller("DeviceDetailBundlesCtrl", ["$stateParams", "$http", DeviceDetailBundlesCtrl])
    .controller("DeviceDetailEventsCtrl", ["$stateParams", "$http", DeviceDetailEventsCtrl])
    .controller("DeviceDetailCommandsCtrl", ["$stateParams", "$http", DeviceDetailCommandsCtrl])
    .controller("DeviceDetailConfigurationsCtrl", ["$stateParams", "$http", DeviceDetailConfigurationsCtrl]);