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
