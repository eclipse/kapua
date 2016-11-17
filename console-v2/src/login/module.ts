import "satellizer";

import LoginCtrl from "./controllers/LoginCtrl";

import "./assets/styles/login.scss";

angular.module("app.login", ["satellizer"])
    .config(["$stateProvider", "$authProvider",
        ($stateProvider: angular.ui.IStateProvider,
            $authProvider) => {
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
                authorizationEndpoint: "http://localhost:9090/auth/realms/master/protocol/openid-connect/auth",
                responseType: "id_token token",
                nonce: "abcdefg",
                requiredUrlParams: ["nonce"]
            });
        }])
    .controller("LoginCtrl", ["$rootScope", "$http", "$state", "localStorageService", "$auth", LoginCtrl]);