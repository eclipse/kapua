/*******************************************************************************
* Copyright (c) 2011, 2016 Eurotech and/or its affiliates                       
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
export default class IndexCtrl {
  private showHeader: boolean;
  private notifications = [];
  private closeNotification;

  constructor(private $rootScope: angular.IRootScopeService, private Notifications) {
    $rootScope.$on("$stateChangeSuccess", (
      event: angular.IAngularEvent,
      toState: angular.ui.IState,
      toParams,
      fromState: angular.ui.IState,
      fromParams
    ) => {
      this.showHeader = toState.name.indexOf("kapua.") === 0;
    });
    this.notifications = Notifications.data;

    // FIXME - To become a class method with PF4
    this.closeNotification = (data) => {
      this.Notifications.remove(data);
    };
  }

  closeNotification2(data) {
    console.log(this);
    this.Notifications.remove(data);
  }
}