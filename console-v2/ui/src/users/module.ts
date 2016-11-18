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