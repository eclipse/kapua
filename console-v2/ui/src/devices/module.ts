import DevicesListCtrl from "./controllers/DevicesListCtrl";

import "./assets/styles/devices.scss";

angular.module("app.devices", [])
    .config(["$stateProvider",
        ($stateProvider: angular.ui.IStateProvider,
            $authProvider) => {
            $stateProvider.state("devices", {
                url: "/devices",
                views: {
                    mainView: {
                        template: require("./views/devices.html"),
                        controller: "DevicesListCtrl as vm"
                    }
                }
            });
        }])
    .controller("DevicesListCtrl", [DevicesListCtrl]);