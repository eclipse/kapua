/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

    public static String question = "fa fa-question-circle fa-3x";
    private Dialog dialog;
    public static final String YESNO = Dialog.YESNO;

    public static MessageBox confirm(String title, String msg, Listener<MessageBoxEvent> callback) { 
          KapuaMessageBox box = new KapuaMessageBox();
          box.setTitle(title);
          box.setMessage(msg);
          box.addCallback(callback);
          box.setButtons(YESNO);
          box.setIcon(question);
          box.show();

        return box;
      }

    @Override
    public Dialog getDialog() {
        dialog = super.getDialog();
        dialog.setIconStyle(question);
        return dialog;
    }

    @Override
    public void show() {
        dialog = getDialog();
        dialog.show();
    }
}
