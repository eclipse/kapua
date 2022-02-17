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
package org.eclipse.kapua.app.console.module.api.client.ui.widget;

import java.util.Arrays;

import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import org.eclipse.kapua.app.console.module.api.shared.model.Enum;

public class EnumComboBox<E extends Enum> extends SimpleComboBox<E> {

    public EnumComboBox() {
        super();

        setEditable(false);
        setTriggerAction(TriggerAction.ALL);
        setAllowBlank(false);
    }

    public void add(E[] values) {
        super.add(Arrays.asList(values));
    }
}
