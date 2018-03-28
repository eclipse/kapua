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
import "angular-patternfly";
import "angular-ui-router";
import "angular-local-storage";
import "angular-patternfly/node_modules/patternfly/node_modules/datatables/media/js/jquery.dataTables.js";

import "angular-patternfly/node_modules/patternfly/dist/css/patternfly.css";
import "angular-patternfly/node_modules/patternfly/dist/css/patternfly-additions.css";
import "angular-patternfly/dist/styles/angular-patternfly.css";

import "./index.scss";

import "./commons/module.ts";
import "./login/module.ts";
import "./layout/module.ts";
import "./welcome/module.ts";
import "./users/module.ts";
import "./devices/module.ts";
import "./roles/module.ts";

import IndexCtrl from "./IndexCtrl";

angular.module("app", [
    "ngAnimate",
    "ui.bootstrap",
    "ui.router",
    "patternfly",
    "patternfly.charts",
    "LocalStorageModule",
    "app.commons",
    "app.login",
    "app.layout",
    "app.welcome",
    "app.users",
    "app.devices",
    "app.roles"
])
    .config(["$locationProvider", "$urlRouterProvider", "$httpProvider", (
        $locationProvider: angular.ILocationProvider,
        $urlRouterProvider: angular.ui.IUrlRouterProvider,
        $httpProvider: angular.IHttpProvider
    ) => {
        $locationProvider.html5Mode({ enabled: true, requireBase: false });
        $urlRouterProvider.otherwise("/login");
        $httpProvider.interceptors.push(["$q", "Notifications",
            (
                $q: angular.IQService,
                Notifications
            ) => {
                return {
                    responseError: function (response: angular.IHttpPromiseCallbackArg<any>) {
                        Notifications.message(
                            "danger",
                            "Error",
                            response.data,
                            true
                        );
                        return $q.reject(response);
                    }
                };
            }]);
    }])
    .run(["$state", "$rootScope", "$auth", "Notifications",
        (
            $state: angular.ui.IStateService,
            $rootScope: angular.IRootScopeService,
            $auth,
            Notifications
        ) => {
            $rootScope.$on("$stateChangeStart", (
                event: angular.IAngularEvent,
                toState: angular.ui.IState,
                toParams, fromState: angular.ui.IState,
                fromParams
            ) => {
                if (toState.name.indexOf("kapua.") === 0 && !$auth.isAuthenticated()) {
                    event.preventDefault();
                    $state.go("login");
                }
            });
        }])
        .controller("IndexCtrl", ["$rootScope", "Notifications", IndexCtrl]);