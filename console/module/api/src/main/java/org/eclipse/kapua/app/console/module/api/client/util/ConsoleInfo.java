/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.InfoConfig;

public class ConsoleInfo extends Info {
    private static final int CHAR_STEP = 20;
    private static final int STEP_HEIGHT = 18;

    public static void display(String title, String text) {
        display(title, text, 5000);
    }

    public static void display(String title, String text, int delay) {
        display(title, text, delay, true);
    }

    public static void display(String title, String text, int delay,
                               boolean keepOnMouseOver) {
        //
        // XSS escaping
        if (text != null) {
            text = KapuaSafeHtmlUtils.htmlEscape(text);
        }

        int infoHeight = 25 + STEP_HEIGHT * (1 + ((text != null ? text.length() : 0) / CHAR_STEP)) + STEP_HEIGHT / 3;

        InfoConfig config = new InfoConfig(title, text);
        config.height = infoHeight;

        if (delay > 0) {
            config.display = delay;
        }

        Params params = new Params();
        params.set("keepOnMouseOver", keepOnMouseOver);
        config.params = params;

        display(config);
    }
}
