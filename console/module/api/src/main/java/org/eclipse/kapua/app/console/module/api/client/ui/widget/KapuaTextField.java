/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
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

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;

public class KapuaTextField<T> extends TextField<T> {
    protected BaseEventPreview focusEventPreview;
    protected boolean mimicing;

    @Override
    public void setMaxLength(int maxLength) {
        super.setMaxLength(maxLength);
        if (rendered) {
            getInputEl().setElementAttribute("maxLength", maxLength);
        }
    }

    @Override
    protected void onFocus(ComponentEvent ce) {
        super.onFocus(ce);
        if (!this.mimicing) {
            this.mimicing = true;
            this.focusEventPreview.add();
        }
    }

    @Override
    protected void onRender(Element target, int index) {
        this.focusEventPreview = new BaseEventPreview() {
            protected boolean onAutoHide(PreviewEvent ce) {
                if (ce.getEventTypeInt() == 4) {
                    KapuaTextField.this.mimicBlur(ce, ce.getTarget());
                }

                return false;
            }
        };
        super.onRender(target, index);
        getInputEl().setElementAttribute("maxLength", getMaxLength());
    }

    protected void mimicBlur(PreviewEvent e, Element target) {
        if (!this.el().dom.isOrHasChild(target)) {
            this.triggerBlur((ComponentEvent)null);
        }

    }

    protected void triggerBlur(ComponentEvent ce) {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                KapuaTextField.this.getFocusEl().blur();
            }
        });
        this.mimicing = false;
        this.focusEventPreview.remove();
        super.onBlur(ce);
    }

}
