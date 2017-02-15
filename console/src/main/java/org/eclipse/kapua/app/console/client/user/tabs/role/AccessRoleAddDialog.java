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
package org.eclipse.kapua.app.console.client.user.tabs.role;

import org.eclipse.kapua.app.console.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessInfo;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessRoleCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.service.*;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AccessRoleAddDialog extends EntityAddEditDialog {

    private final static ConsoleUserMessages MSGS = GWT.create(ConsoleUserMessages.class);

    private final static GwtRoleServiceAsync gwtRoleService = GWT.create(GwtRoleService.class);
    private final static GwtAccessRoleServiceAsync gwtAccessRoleService = GWT.create(GwtAccessRoleService.class);
    private final static GwtAccessInfoServiceAsync gwtAccessInfoService = GWT.create(GwtAccessInfoService.class);
    
    private ComboBox<GwtRole> rolesCombo;
    private String accessInfoId;
    
    public AccessRoleAddDialog(GwtSession currentSession, String userId) {
        super(currentSession);
        gwtAccessInfoService.findByUserIdOrCreate(currentSession.getSelectedAccount().getId(), userId, new AsyncCallback<GwtAccessInfo>() {
            
            @Override
            public void onSuccess(GwtAccessInfo result) {
                accessInfoId = result.getId();
                m_submitButton.enable();
            }
            
            @Override
            public void onFailure(Throwable caught) {
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogAddError(MSGS.dialogAddRoleErrorAccessInfo(caught.getLocalizedMessage()));
                
                hide();
            }
        });
        
        DialogUtils.resizeDialog(this, 400, 400);
    }

    @Override
    public void submit() {
        GwtAccessRoleCreator gwtAccessRoleCreator = new GwtAccessRoleCreator();

        gwtAccessRoleCreator.setScopeId(currentSession.getSelectedAccount().getId());
        
        gwtAccessRoleCreator.setAccessInfoId(accessInfoId);
        gwtAccessRoleCreator.setRoleId(rolesCombo.getValue().getId());
        
        gwtAccessRoleService.create(xsrfToken, gwtAccessRoleCreator, new AsyncCallback<GwtAccessRole>() {

            @Override
            public void onSuccess(GwtAccessRole arg0) {
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                unmask();
                
                m_submitButton.enable();
                m_cancelButton.enable();
                m_status.hide();
                
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogAddError(MSGS.dialogAddRoleError(cause.getLocalizedMessage()));
                
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

    @Override
    public void createBody() {
        FormPanel roleFormPanel = new FormPanel(FORM_LABEL_WIDTH);

        RpcProxy<ListLoadResult<GwtRole>> roleUserProxy = new RpcProxy<ListLoadResult<GwtRole>>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtRole>> callback)
            {
                gwtRoleService.findAll(currentSession.getSelectedAccount().getId(),
                                       callback);
            }
        };

        BaseListLoader<ListLoadResult<GwtRole>> roleLoader = new BaseListLoader<ListLoadResult<GwtRole>>(roleUserProxy);
        ListStore<GwtRole> roleStore = new ListStore<GwtRole>(roleLoader);
        //
        // Role
        rolesCombo = new ComboBox<GwtRole>();
        rolesCombo.setEditable(false);
        rolesCombo.setTypeAhead(false);
        rolesCombo.setAllowBlank(false);
        rolesCombo.setFieldLabel(MSGS.dialogAddRoleComboName());
        rolesCombo.setTriggerAction(TriggerAction.ALL);
        rolesCombo.setStore(roleStore);
        rolesCombo.setDisplayField("name");
        rolesCombo.setValueField("id");
        
        roleFormPanel.add(rolesCombo);
        
        //
        // Add form panel to body
        m_bodyPanel.add(roleFormPanel);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        m_submitButton.disable();
    }
}
