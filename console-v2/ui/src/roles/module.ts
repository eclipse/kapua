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
import RoleListCtrl from "./controllers/RolesListCtrl";
import RoleDetailCtrl from "./controllers/RoleDetailCtrl";
import RoleDetailDescriptionCtrl from "./controllers/RoleDetailDescriptionCtrl";
import RoleDetailPermissionsCtrl from "./controllers/RoleDetailPermissionsCtrl";
import RoleDetailSubjectsCtrl from "./controllers/RoleDetailSubjectsCtrl";

import "./assets/styles/roles.scss";

angular.module("app.roles", [])
    .config(["$stateProvider",
        ($stateProvider: angular.ui.IStateProvider,
            $authProvider) => {
            $stateProvider.state("kapua.roles", {
                url: "/roles",
                views: {
                    "kapuaView@kapua": {
                        template: require("./views/roles-list.html"),
                        controller: "RolesListCtrl as vm"
                    }
                }
            })
            .state("kapua.roles.detail", {
                    url: "/:idRole",
                    views: {
                        "kapuaView@kapua": {
                            template: require("./views/roles-details.html"),
                            controller: "RoleDetailCtrl as vm"
                        }
                    }
                })
                .state("kapua.roles.detail.description", {
                    url: "/description",
                    views: {
                        "kapuaView@kapua": {
                            template: require("./views/role-details/description.html"),
                            controller: "RoleDetailDescriptionCtrl as vm"
                        }
                    }
                })
                .state("kapua.roles.detail.permissions", {
                    url: "/permissions",
                    views: {
                        "kapuaView@kapua": {
                            template: require("./views/role-details/permissions.html"),
                            controller: "RoleDetailPermissionsCtrl as vm"
                        }
                    }
                })
                .state("kapua.roles.detail.subjects", {
                    url: "/subjects",
                    views: {
                        "kapuaView@kapua": {
                            template: require("./views/role-details/subjects.html"),
                            controller: "RoleDetailSubjectsCtrl as vm"
                        }
                    }
                });
        }])
    .controller("RolesListCtrl", ["$http", "localStorageService", RoleListCtrl])
    .controller("RoleDetailCtrl", ["$stateParams", "$http", RoleDetailCtrl])
    .controller("RoleDetailDescriptionCtrl", ["$stateParams", "$http", RoleDetailDescriptionCtrl])
    .controller("RoleDetailPermissionsCtrl", ["$stateParams", "$http", RoleDetailPermissionsCtrl])
    .controller("RoleDetailSubjectsCtrl", ["$stateParams", "$http", RoleDetailSubjectsCtrl]);