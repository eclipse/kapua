/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client.ui.widget;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.ActionDialog;

public class KapuaDateField extends DateField {

    private ActionDialog dialog;
    private Boolean enabledDateFieldEvents = false;

    public KapuaDateField() {
        super();
    }

    public KapuaDateField(ActionDialog actionDialog) {
        super();
        this.dialog = actionDialog;
        this.enabledDateFieldEvents = true;

    }

    @Override
    public void setMaxLength(int maxLength) {
        super.setMaxLength(maxLength);
        if (rendered) {
            getInputEl().setElementAttribute("maxLength", maxLength);
        }
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        getInputEl().setElementAttribute("maxLength", getMaxLength());

        Listener<BaseEvent> dateListener = new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                setDateFieldState();
            }
        };

        final Listener<BaseEvent> pasteEventListener = new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                if (be.getType() == Events.OnPaste) {
                    final Timer timer = new Timer() {

                        @Override
                        public void run() {
                            setDateFieldState();
                        }
                    };
                    timer.schedule(100);
                }
            }
        };

        KeyNav<ComponentEvent> keyNav = new KeyNav<ComponentEvent>(KapuaDateField.this) {
            @Override
            public void onKeyPress(ComponentEvent ce) {
                if (ce.getKeyCode() == KeyCodes.KEY_TAB || ce.getKeyCode() == KeyCodes.KEY_ENTER) {
                    setDateFieldState();
                }
            }
        };

        if (enabledDateFieldEvents) {
            KapuaDateField.this.addListener(Events.KeyUp, dateListener);
            KapuaDateField.this.addListener(Events.OnMouseUp, dateListener);
            KapuaDateField.this.addListener(Events.OnPaste, pasteEventListener);
            sinkEvents(Event.ONPASTE);
        }
    }

    private void setDateFieldState() {
        if (dialog != null) {
            if (!KapuaDateField.this.getRawValue().isEmpty() && KapuaDateField.this.getOriginalValue() == null) {
                dialog.setDateValueNotNull(true);
            } else {
                dialog.setDateValueNotNull(false);
            }
        }
    }
}
