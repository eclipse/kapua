import "satellizer";

import LoginCtrl from "./controllers/LoginCtrl";

import "./assets/styles/login.scss";

angular.module("app.login", ["satellizer", "app.constants"])
    .config(["$stateProvider", "$authProvider", "kapuaConfig",
        ($stateProvider: angular.ui.IStateProvider,
         $authProvider,
         kapuaConfig) => {
            $stateProvider.state("login", {
                url: "/login",
                views: {
                    mainView: {
                        template: require("./views/login.html"),
                        controller: "LoginCtrl as vm"
                    }
                }
            });

            $authProvider.oauth2({
                name: "oauth2",
                clientId: "console",
                redirectUri: window.location.origin,
                authorizationEndpoint: kapuaConfig.oauth.authorizationEndpoint,
                url: window.location.origin + "/oauth/authenticate",
                responseType: ["code"],
                nonce: "abcdefg",
                requiredUrlParams: ["nonce"],
            });
        }])
    .controller("LoginCtrl", ["$rootScope", "$http", "$state", "localStorageService", "$auth", "kapuaConfig", LoginCtrl]);