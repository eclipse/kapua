/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.api.client.ui.dialog;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;

public class KapuaMessageBox extends MessageBox{

    public static final String QUESTION = "fa fa-question-circle fa-3x";
    private Dialog dialog;
    public static final String YESNO = Dialog.YESNO;

    public static MessageBox confirm(String title, String msg, Listener<MessageBoxEvent> callback) {
          KapuaMessageBox box = new KapuaMessageBox();
          box.setTitle(title);
          box.setMessage(msg);
          box.addCallback(callback);
          box.setButtons(YESNO);
          box.setIcon(QUESTION);
          box.show();

        return box;
      }

    @Override
    public void show() {
        dialog = getDialog();
        dialog.show();
    }
}
