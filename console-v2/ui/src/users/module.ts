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
import UsersListCtrl from "./controllers/UsersListCtrl";

import "./assets/styles/users.scss";

angular.module("app.users", [])
    .config(["$stateProvider",
        ($stateProvider: angular.ui.IStateProvider,
            $authProvider) => {
            $stateProvider.state("kapua.users", {
                url: "/users",
                views: {
                    "kapuaView@kapua": {
                        template: require("./views/users-list.html"),
                        controller: "UsersListCtrl as vm"
                    }
                }
            });
        }])
    .controller("UsersListCtrl", ["$http", "localStorageService", UsersListCtrl]);