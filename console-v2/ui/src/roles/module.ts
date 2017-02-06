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
import RolesListCtrl from "./controllers/RolesListCtrl";

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
            });
        }])
    .controller("RolesListCtrl", ["$http", "localStorageService", RolesListCtrl]);