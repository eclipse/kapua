/*******************************************************************************
* Copyright (c) 2016, 2017 Eurotech and/or its affiliates                       
*                                                                               
* All rights reserved. This program and the accompanying materials              
* are made available under the terms of the Eclipse Public License v1.0         
* which accompanies this distribution, and is available at                      
* http://www.eclipse.org/legal/epl-v10.html                                     
*                                                                               
* Contributors:                                                                 
*     Eurotech - initial API and implementation                                 
*                                                                               
*******************************************************************************/
export default class RoleDetailPermissionsCtrl {
  private roleId: string;
  private permissions: RolePermission[];

  constructor(private $stateParams: angular.ui.IStateParamsService,
    private $http: angular.IHttpService,
    private rolesService: IRolesService) {
    this.roleId = $stateParams["idRole"];
    this.getPermissonsByRole(this.roleId);
  }

  getPermissonsByRole(roleID: string): void {
    this.rolesService.getPermissionsByRole(roleID).then((permissionsResponse: ng.IHttpPromiseCallbackArg<ListResult<RolePermission>>) => {
      this.permissions = permissionsResponse.data.items.item;
    });
  }
}