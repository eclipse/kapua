interface ILoginData {
    username: string;
    password: string;
}

export default class LoginCtrl {
    private loginData: ILoginData;

    constructor(private $rootScope: angular.IRootScopeService,
        private $http: angular.IHttpService,
        private $state: angular.ui.IStateService,
        private localStorageService: angular.local.storage.ILocalStorageService,
        private $auth) {
        $rootScope.$on("$stateChangeSuccess", (event, toState: angular.ui.IState, toParams, fromState: angular.ui.IState, fromParams) => {
            if (toState.name.indexOf("kapua.") === 0) {
                angular.element("html").addClass("layout-pf layout-pf-fixed");
            } else if (toState.name === "login") {
                angular.element("html").removeClass("layout-pf layout-pf-fixed");
            }
        });
    }

    private doLogin(loginData: ILoginData) {
        this.$http.post("http://localhost:8080/api/v1/authentication", loginData)
            .then((responseData: angular.IHttpPromiseCallbackArg<any>) => {
                this.$http.defaults.headers.common["X-Access-Token"] = responseData.data.tokenId;
                this.localStorageService.set("accessToken", responseData.data.tokenId);
                this.$state.go("kapua.welcome");
            },
            (responseData: angular.IHttpPromiseCallbackArg<any>) => {
                console.log("fail!");
            });
    }

    private doSSOLogin() {
        this.$auth.authenticate("oauth2");
    }
}