export default class LayoutCtrl {
    navigationItems = [
        {
            title: "Welcome",
            iconClass: "fa fa-dashboard",
            href: "#/welcome"
        }
    ];

    constructor() { }

    private getLogoImage() {
        return require("../../login/assets/img/logo-white.svg");
    }
}