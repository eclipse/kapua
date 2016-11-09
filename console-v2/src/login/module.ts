import "./assets/styles/login.scss";

angular.module("app.login", [])
    .config(["$stateProvider", ($stateProvider: angular.ui.IStateProvider) => {
        $stateProvider.state("login", {
            url: "/login",
            views: {
                mainView: {
                    template: require("./views/login.html")
                }
            }
        });
    }]);