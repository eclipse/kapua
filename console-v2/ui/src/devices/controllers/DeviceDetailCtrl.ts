export default class DeviceDetailCtrl {
  constructor(private $stateParams: angular.ui.IStateParamsService) {
    console.log($stateParams["id"]);
  }
}