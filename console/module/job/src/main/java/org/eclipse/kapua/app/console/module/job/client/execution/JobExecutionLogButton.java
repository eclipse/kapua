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
package org.eclipse.kapua.app.console.module.job.client.execution;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.Button;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;

public class JobExecutionLogButton extends Button {

    private static final ConsoleJobMessages MSGS_JOB = GWT.create(ConsoleJobMessages.class);

    public JobExecutionLogButton(SelectionListener<ButtonEvent> listener) {
        super(MSGS_JOB.jobExecutionLogButton(),
                new KapuaIcon(IconSet.STICKY_NOTE_O),
                listener);
    }
}
