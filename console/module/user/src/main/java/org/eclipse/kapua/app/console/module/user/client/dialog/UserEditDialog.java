/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.user.client.dialog;

import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.InfoDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.InfoDialog.InfoDialogType;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.client.util.SplitTooltipStringUtil;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser.GwtUserStatus;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserServiceAsync;

public class UserEditDialog extends UserAddDialog {

    private GwtUser selectedUser;
    private boolean isChanged;

    private GwtUserServiceAsync gwtUserService = GWT.create(GwtUserService.class);
    public static final int MAX_LINE_LENGTH = 30;
    public static final int MAX_TOOLTIP_WIDTH = 300;

    private static final String ERROR = "Error";

    public UserEditDialog(GwtSession currentSession, GwtUser selectedUser) {
        super(currentSession);
        this.selectedUser = selectedUser;
        DialogUtils.resizeDialog(this, 400, 390);
    }

    @Override
    public void createBody() {
        super.createBody();

        loadUser();
    }

    private void loadUser() {
        maskDialog();
        gwtUserService.find(selectedUser.getScopeId(), selectedUser.getId(), new AsyncCallback<GwtUser>() {

            @Override
            public void onSuccess(GwtUser gwtUser) {
                unmaskDialog();
                populateEditDialog(gwtUser);
                selectedUser = gwtUser;
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                if (!isPermissionErrorMessage(cause)) {
                    exitMessage = USER_MSGS.dialogEditLoadFailed(cause.getLocalizedMessage());
                }
                unmaskDialog();
                hide();
            }
        });

    }

    @Override
    public void validateUser() {
        if (!email.isValid()) {
            ConsoleInfo.display(ERROR, email.getErrorMessage());
        } else if (!phoneNumber.isValid()) {
            ConsoleInfo.display(ERROR, phoneNumber.getErrorMessage());
        } else if (!expirationDate.isValid()) {
            ConsoleInfo.display(ERROR, KapuaSafeHtmlUtils.htmlUnescape(expirationDate.getErrorMessage()));
        }
    }

    @Override
    protected void preSubmit() {
        super.preSubmit();
        if (displayName.isDirty()) {
            isChanged = true;
        }
    }

    @Override
    public void submit() {
        selectedUser.setUsername(username.getValue());
        selectedUser.setDisplayName(KapuaSafeHtmlUtils.htmlUnescape(displayName.getValue()));
        selectedUser.setEmail(email.getValue());
        selectedUser.setPhoneNumber(phoneNumber.getValue());
        selectedUser.setStatus(userStatus.getValue().getValue().toString());
        selectedUser.setExpirationDate(expirationDate.getValue());

        gwtUserService.update(xsrfToken, selectedUser, new AsyncCallback<GwtUser>() {

            @Override
            public void onSuccess(GwtUser gwtUser) {
                if (currentSession.getUserName().equals(gwtUser.getUsername()) && isChanged) {
                    InfoDialog infoDialog = new InfoDialog(InfoDialogType.INFO, USER_MSGS.dialogEditUserName());
                    infoDialog.show();
                }
                exitStatus = true;
                exitMessage = USER_MSGS.dialogEditConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
                if (!isPermissionErrorMessage(cause)) {
                    exitMessage = USER_MSGS.dialogEditError(cause.getLocalizedMessage());
                    FailureHandler.handleFormException(formPanel, cause);
                    if (cause instanceof GwtKapuaException) {
                        GwtKapuaException gwtCause = (GwtKapuaException) cause;
                        if (gwtCause.getCode().equals(GwtKapuaErrorCode.DUPLICATE_NAME)) {
                            username.markInvalid(gwtCause.getMessage());
                        } else if (gwtCause.getCode().equals(GwtKapuaErrorCode.ILLEGAL_ARGUMENT)) {
                            if (gwtCause.getArguments().length == 2 && gwtCause.getArguments()[0].equals("status") && gwtCause.getArguments()[1].equals("DISABLED")) {
                                userStatus.markInvalid(gwtCause.getMessage());
                            }
                        } else if (gwtCause.getCode().equals(GwtKapuaErrorCode.OPERATION_NOT_ALLOWED_ON_ADMIN_USER)) {
                            if (userStatus.getValue().getValue().equals(GwtUserStatus.DISABLED)) {
                                userStatus.markInvalid(USER_MSGS.dialogEditAdminUserStatusError());
                            } if (expirationDate.getValue() != null) {
                                expirationDate.markInvalid(USER_MSGS.dialogEditAdminExpirationDateError());
                            }
                        }
                    }
                }
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return USER_MSGS.dialogEditHeader(selectedUser.getUsername());
    }

    @Override
    public String getInfoMessage() {
        return USER_MSGS.dialogEditInfo();
    }

    private void populateEditDialog(GwtUser gwtUser) {
        infoFieldSet.remove(username);
        usernameLabel.setVisible(true);
        username.setVisible(false);
        ToolTipConfig toolTipConfig = new ToolTipConfig();
        toolTipConfig.setMaxWidth(MAX_TOOLTIP_WIDTH);
        String toolTipText = SplitTooltipStringUtil.splitTooltipString(gwtUser.getUsername(), MAX_LINE_LENGTH);
        toolTipConfig.setText(toolTipText);

        usernameLabel.setValue(gwtUser.getUsername());
        usernameLabel.setStyleAttribute("white-space", "nowrap");
        usernameLabel.setStyleAttribute("text-overflow", "ellipsis");
        usernameLabel.setStyleAttribute("overflow", "hidden");
        usernameLabel.setToolTip(toolTipConfig);

        infoFieldSet.remove(externalId);
        infoFieldSet.remove(userRadioGroup);

        if (currentSession.isSsoEnabled() && gwtUser.getUserTypeEnum() == GwtUser.GwtUserType.EXTERNAL) {
            externalIdLabel.setVisible(true);
            externalId.setVisible(false);
            ToolTipConfig externalIdToolTipConfig = new ToolTipConfig();
            externalIdToolTipConfig.setMaxWidth(MAX_TOOLTIP_WIDTH);
            String externalIdToolTipText = SplitTooltipStringUtil.splitTooltipString(gwtUser.getExternalId(), MAX_LINE_LENGTH);
            externalIdToolTipConfig.setText(externalIdToolTipText);

            externalIdLabel.setValue(gwtUser.getExternalId());
            externalIdLabel.setStyleAttribute("white-space", "nowrap");
            externalIdLabel.setStyleAttribute("text-overflow", "ellipsis");
            externalIdLabel.setStyleAttribute("overflow", "hidden");
            externalIdLabel.setToolTip(externalIdToolTipConfig);
        }

        //userRadioGroup.hide();

        if (gwtUser.getUserTypeEnum() == GwtUser.GwtUserType.INTERNAL) {
            if (password != null) {
                password.setVisible(false);
                password.setAllowBlank(true);
                password.setValidator(null);
                password.disable();
            }
            if (confirmPassword != null) {
                confirmPassword.setVisible(false);
                confirmPassword.setAllowBlank(true);
                confirmPassword.setValidator(null);
                confirmPassword.disable();
            }
            if (passwordTooltip != null) {
                passwordTooltip.hide();
                passwordTooltip.disable();
            }
        } else {
            password.hide();
            password.disable();
            confirmPassword.hide();
            confirmPassword.disable();
            passwordTooltip.hide();
            passwordTooltip.disable();
        }

        username.setValue(gwtUser.getUsername());
        if (currentSession.isSsoEnabled() && gwtUser.getUserTypeEnum()== GwtUser.GwtUserType.EXTERNAL) {
            externalId.setValue(gwtUser.getExternalId());
        }
        displayName.setValue(gwtUser.getUnescapedDisplayName());
        email.setValue(gwtUser.getEmail());
        phoneNumber.setValue(gwtUser.getPhoneNumber());
        userStatus.setSimpleValue(gwtUser.getStatusEnum());
        expirationDate.setValue(gwtUser.getExpirationDate());
        expirationDate.setMaxLength(10);
        formPanel.clearDirtyFields();
    }

}
