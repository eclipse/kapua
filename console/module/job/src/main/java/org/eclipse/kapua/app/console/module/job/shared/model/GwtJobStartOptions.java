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
package org.eclipse.kapua.app.console.module.job.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GwtJobStartOptions implements Serializable {

    private List<String> targetIdSublist = new ArrayList<String>();
    private boolean resetStepIndex;
    private Integer fromStepIndex;

    public List<String> getTargetIdSublist() {
        return targetIdSublist;
    }

    public void setTargetIdSublist(List<String> targetIdSublist) {
        this.targetIdSublist = targetIdSublist;
    }

    public boolean getResetStepIndex() {
        return resetStepIndex;
    }

    public void setResetStepIndex(boolean resetStepIndex) {
        this.resetStepIndex = resetStepIndex;
    }

    public Integer getFromStepIndex() {
        return fromStepIndex;
    }

    public void setFromStepIndex(Integer fromStepIndex) {
        this.fromStepIndex = fromStepIndex;
    }
}
