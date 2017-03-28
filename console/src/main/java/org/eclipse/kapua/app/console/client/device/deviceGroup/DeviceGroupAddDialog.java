/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.client.device.deviceGroup;

import java.util.List;

import org.eclipse.kapua.app.console.client.messages.ConsoleDeviceGroupMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceGroup;
import org.eclipse.kapua.app.console.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceGroupService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceGroupServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.shared.service.GwtGroupServiceAsync;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeviceGroupAddDialog extends EntityAddEditDialog{
    
    private final static GwtDeviceGroupServiceAsync service = GWT.create(GwtDeviceGroupService.class);
    private final static GwtGroupServiceAsync groupService = GWT.create(GwtGroupService.class);
    private final static ConsoleDeviceGroupMessages MSGS = GWT.create(ConsoleDeviceGroupMessages.class);
    protected ComboBox<GwtGroup> groupCombo;
    protected GwtDevice selected_device;

    public DeviceGroupAddDialog(GwtSession currentSession, GwtDevice gwtDevice) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 400);
        
       selected_device = gwtDevice;    
    }
    
  

    @Override
    public void createBody() {
      
        FormPanel formPanel = new FormPanel(FORM_LABEL_WIDTH);
        formPanel.setHeaderVisible(false);
        groupCombo = new ComboBox<GwtGroup>();
        groupCombo.setStore(new ListStore<GwtGroup>());
        groupCombo.setFieldLabel(MSGS.dialogAddFieldName());
        groupCombo.setForceSelection(true);
        groupCombo.setTypeAhead(true);
        groupCombo.setAllowBlank(false);
        groupCombo.setDisplayField("groupName");
        groupCombo.setValueField("id");
        groupService.findAll(currentSession.getSelectedAccount().getId(), new AsyncCallback<List<GwtGroup>>() {

            @Override
            public void onFailure(Throwable arg0) {
                FailureHandler.handle(arg0);
                
            }

            @Override
            public void onSuccess(List<GwtGroup> arg0) {
               groupCombo.getStore().removeAll();
               groupCombo.getStore().add(arg0);
            }
        });
        
        formPanel.add(groupCombo);
        
        m_bodyPanel.add(formPanel);
       
        
    }

    @Override
    public void submit() {
        GwtDeviceGroupCreator gwtDeviceGroupCreator = new GwtDeviceGroupCreator();
        gwtDeviceGroupCreator.setScopeId(currentSession.getSelectedAccount().getId());
        gwtDeviceGroupCreator.setDevId(selected_device.getId());
        gwtDeviceGroupCreator.setGroupId(groupCombo.getValue().getId());
        service.create(gwtDeviceGroupCreator, new AsyncCallback<GwtDeviceGroup>() {

            @Override
            public void onFailure(Throwable arg0) {
                 unmask();
                
                m_submitButton.enable();
                m_cancelButton.enable();
                m_status.hide();
                
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogAddError(arg0.getLocalizedMessage());
                
                hide();
                
            }

            @Override
            public void onSuccess(GwtDeviceGroup arg0) {
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogAddConfirmation();
                hide();
                
            }
        });
        
        
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogAddHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogAddInfo();
    }
  
}
