export default class DeleteDevicesModalCtrl {
  constructor(private $modalInstance: angular.ui.bootstrap.IModalServiceInstance,
              private $http: angular.IHttpService,
              private id: any) { }
  ok() {
    this.$http.delete(`api/devices/${this.id}`).then((deleteResult) => {
      this.$modalInstance.close("ok");
    });
  }
  cancel() {
    this.$modalInstance.dismiss("cancel");
  }
}
