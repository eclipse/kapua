export default class LayoutCtrl {
    navigationItems = [
        {
            title: "Welcome",
            iconClass: "fa fa-info",
            href: "#/welcome"
        }
    ];

    constructor() { }

    private getLogoImage() {
        return require("../assets/img/logo-white.svg");
    }
}