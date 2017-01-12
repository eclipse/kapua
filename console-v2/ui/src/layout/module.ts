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
import LayoutCtrl from "./controllers/LayoutCtrl";
import ExternalModulesList from "./services/externalModulesList.service";

import "./assets/styles/layout.scss";

angular.module("app.layout", ["ui.router"])
    .config(["$stateProvider", ($stateProvider: angular.ui.IStateProvider) => {
        $stateProvider.state("kapua", {
            abstract: true,
            views: {
                mainView: {
                    template: require("./views/layout.html"),
                    controller: "LayoutCtrl as vm"
                }
            }
        });
    }])
    .controller("LayoutCtrl", ["$http", "$state", "localStorageService", "$auth", "kapuaConfig", "$rootScope", "ExternalModulesList", LayoutCtrl])
    .service("ExternalModulesList", [ExternalModulesList]);
