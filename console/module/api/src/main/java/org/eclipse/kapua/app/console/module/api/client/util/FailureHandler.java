/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client.util;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.StatusCodeException;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;

import java.util.List;

/**
 * Handles GwtExceptions from RCP calls.
 */
public class FailureHandler {

    private FailureHandler() {
    }

    private static final ConsoleMessages CMSGS = GWT.create(ConsoleMessages.class);
    private static final ValidationMessages MSGS = GWT.create(ValidationMessages.class);

    public static void handle(Throwable caught) {
        if (caught instanceof GwtKapuaException) {

            GwtKapuaException gee = (GwtKapuaException) caught;
            GwtKapuaErrorCode code = gee.getCode();
            switch (code) {

            case UNAUTHENTICATED:
                ConsoleInfo.display(CMSGS.loggedOut(), caught.getLocalizedMessage());
                Timer timer = new Timer() {

                    @Override
                    public void run() {
                        Window.Location.reload();
                    }
                };
                timer.schedule(5000);
                break;

            default:
                ConsoleInfo.display(CMSGS.error(), caught.getLocalizedMessage());
                Log.error("RPC Error", caught);
                break;
            }
        } else if (caught instanceof StatusCodeException &&
                ((StatusCodeException) caught).getStatusCode() == 0) {

            // the current operation was interrupted as the user started a new one
            // or navigated away from the page.
            // we can ignore this error and do nothing.
        } else {

            ConsoleInfo.display(CMSGS.error(), caught.getLocalizedMessage());
            caught.printStackTrace();
        }
    }

    public static boolean handleFormException(FormPanel form, Throwable caught) {

        boolean isWarning = false;
        if (caught instanceof GwtKapuaException) {

            List<Field<?>> fields = form.getFields();
            GwtKapuaException gee = (GwtKapuaException) caught;
            GwtKapuaErrorCode code = gee.getCode();
            switch (code) {

            case INVALID_XSRF_TOKEN:
                ConsoleInfo.display(CMSGS.error(), CMSGS.securityInvalidXSRFToken());
                Window.Location.reload();
                break;

            case UNAUTHENTICATED:
                ConsoleInfo.display(CMSGS.loggedOut(), caught.getLocalizedMessage());
                Window.Location.reload();
                break;

            case DUPLICATE_NAME:
                boolean fieldFound = false;
                String duplicateFieldName = gee.getArguments()[0];
                for (Field<?> field : fields) {
                    if (duplicateFieldName.equals(field.getName())) {
                        TextField<String> textField = (TextField<String>) field;
                        textField.markInvalid(MSGS.duplicateValue());
                        fieldFound = true;
                        break;
                    }
                }
                if (!fieldFound) {
                    ConsoleInfo.display(CMSGS.error(), caught.getLocalizedMessage());
                }
                break;

            case ENTITY_UNIQUENESS:
                String errorFields = gee.getArguments()[0];
                ConsoleInfo.display(CMSGS.error(), caught.getLocalizedMessage() + errorFields);
                break;

            case ILLEGAL_NULL_ARGUMENT:
                String invalidFieldName = gee.getArguments()[0];
                for (Field<?> field : fields) {
                    if (invalidFieldName.equals(field.getName())) {
                        TextField<String> textField = (TextField<String>) field;
                        textField.markInvalid(MSGS.invalidNullValue());
                        break;
                    }
                }
                break;

            case ILLEGAL_ARGUMENT:
                String invalidFieldName1 = gee.getArguments()[0];
                for (Field<?> field : fields) {
                    if (invalidFieldName1.equals(field.getName())) {
                        TextField<String> textField = (TextField<String>) field;
                        textField.markInvalid(gee.getCause().getMessage());
                        break;
                    }
                }
                break;

            case CANNOT_REMOVE_LAST_ADMIN:
                String adminFieldName = gee.getArguments()[0];
                for (Field<?> field : fields) {
                    if (adminFieldName.equals(field.getName())) {
                        CheckBoxGroup adminCheckBoxGroup = (CheckBoxGroup) field;
                        adminCheckBoxGroup.markInvalid(MSGS.lastAdministrator());
                        break;
                    }
                }
                break;

            case INVALID_RULE_QUERY:
                for (Field<?> field : fields) {
                    if ("query".equals(field.getName())) {
                        TextArea statement = (TextArea) field;
                        statement.markInvalid(caught.getLocalizedMessage());
                        break;
                    }
                }
                break;

            case WARNING:
                isWarning = true;
                ConsoleInfo.display(CMSGS.warning(), caught.getLocalizedMessage());
                break;

            default:
                ConsoleInfo.display(CMSGS.error(), caught.getLocalizedMessage());
                caught.printStackTrace();
                break;
            }
        } else {

            ConsoleInfo.display(CMSGS.error(), caught.getLocalizedMessage());
            caught.printStackTrace();
        }

        return isWarning;
    }
}
