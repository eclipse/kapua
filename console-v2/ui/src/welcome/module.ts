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
import WelcomeCtrl from "./controllers/WelcomeCtrl";

angular.module("app.welcome", ["ui.router"])
    .config(["$stateProvider", ($stateProvider: angular.ui.IStateProvider) => {
        $stateProvider.state("kapua.welcome", {
            views: {
                "kapuaView@kapua": {
                    template: require("./views/welcome.html"),
                    controller: "WelcomeCtrl as vm"
                }
            },
            url: "/welcome"
        });
    }])
    .controller("WelcomeCtrl", WelcomeCtrl);
