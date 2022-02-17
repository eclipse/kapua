/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.api.client.ui.widget;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;

public class KapuaTextArea extends TextArea {
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
                    KapuaTextArea.this.mimicBlur(ce, ce.getTarget());
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
                KapuaTextArea.this.getFocusEl().blur();
            }
        });
        this.mimicing = false;
        this.focusEventPreview.remove();
        super.onBlur(ce);
    }
}
