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
