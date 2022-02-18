/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
 * Handles {@link Exception}s on the clinet side.
 *
 * @since 1.0.0
 */
public class FailureHandler {

    private FailureHandler() {
    }

    private static final ConsoleMessages CONSOLE_MSGS = GWT.create(ConsoleMessages.class);
    private static final ValidationMessages VALIDATION_MSGS = GWT.create(ValidationMessages.class);

    public static void handle(Throwable caught) {
        if (caught instanceof GwtKapuaException) {
            GwtKapuaException gee = (GwtKapuaException) caught;

            GwtKapuaErrorCode code = gee.getCode();
            if (code == GwtKapuaErrorCode.UNAUTHENTICATED) {
                ConsoleInfo.display(CONSOLE_MSGS.loggedOut(), caught.getLocalizedMessage());
                Timer timer = new Timer() {

                    @Override
                    public void run() {
                        Window.Location.reload();
                    }
                };
                timer.schedule(5000);
            } else {
                ConsoleInfo.display(CONSOLE_MSGS.error(), caught.getLocalizedMessage());
                Log.error("RPC Error", caught);
            }
        } else if (caught instanceof StatusCodeException &&
                ((StatusCodeException) caught).getStatusCode() == 0) {

            // the current operation was interrupted as the user started a new one
            // or navigated away from the page.
            // we can ignore this error and do nothing.
        } else {
            ConsoleInfo.display(CONSOLE_MSGS.error(), caught.getLocalizedMessage());
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
                    ConsoleInfo.display(CONSOLE_MSGS.error(), CONSOLE_MSGS.securityInvalidXSRFToken());
                    Window.Location.reload();
                    break;

                case UNAUTHENTICATED:
                    ConsoleInfo.display(CONSOLE_MSGS.loggedOut(), caught.getLocalizedMessage());
                    Window.Location.reload();
                    break;

                case DUPLICATE_NAME:
                    boolean fieldFound = false;
                    String duplicateFieldName = gee.getArguments()[0];
                    for (Field<?> field : fields) {
                        if (duplicateFieldName.equals(field.getName())) {
                            TextField<String> textField = (TextField<String>) field;
                            textField.markInvalid(VALIDATION_MSGS.duplicateValue());
                            fieldFound = true;
                            break;
                        }
                    }
                    if (!fieldFound) {
                        ConsoleInfo.display(CONSOLE_MSGS.error(), caught.getLocalizedMessage());
                    }
                    break;

                case ENTITY_UNIQUENESS:
                    String errorFields = gee.getArguments()[0];
                    ConsoleInfo.display(CONSOLE_MSGS.error(), caught.getLocalizedMessage() + errorFields);
                    break;

                case ILLEGAL_NULL_ARGUMENT:
                    String invalidFieldName = gee.getArguments()[0];
                    for (Field<?> field : fields) {
                        if (invalidFieldName.equals(field.getName())) {
                            TextField<String> textField = (TextField<String>) field;
                            textField.markInvalid(VALIDATION_MSGS.invalidNullValue());
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
                            adminCheckBoxGroup.markInvalid(VALIDATION_MSGS.lastAdministrator());
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

                case OPERATION_NOT_ALLOWED_ON_ADMIN_USER:
                    ConsoleInfo.display(CONSOLE_MSGS.error(), caught.getLocalizedMessage());
                    break;

                case WARNING:
                    isWarning = true;
                    ConsoleInfo.display(CONSOLE_MSGS.warning(), caught.getLocalizedMessage());
                    break;

                default:
                    ConsoleInfo.display(CONSOLE_MSGS.error(), caught.getLocalizedMessage());
                    caught.printStackTrace();
                    break;
            }
        } else {
            ConsoleInfo.display(CONSOLE_MSGS.error(), caught.getLocalizedMessage());
        }

        return isWarning;
    }
}
