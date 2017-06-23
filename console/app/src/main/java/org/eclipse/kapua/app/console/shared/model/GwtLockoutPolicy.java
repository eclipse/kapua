/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;

public class GwtLockoutPolicy implements Serializable {

    private static final long serialVersionUID = -3006807358731690418L;

    private Boolean enabled;
    private Integer maxFailures;
    private Integer resetAfter;
    private Integer lockDuration;

    public GwtLockoutPolicy() {}

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getMaxFailures() {
        return maxFailures;
    }

    public void setMaxFailures(Integer maxFailures) {
        this.maxFailures = maxFailures;
    }

    public Integer getResetAfter() {
        return resetAfter;
    }

    public void setResetAfter(Integer resetAfter) {
        this.resetAfter = resetAfter;
    }

    public Integer getLockDuration() {
        return lockDuration;
    }

    public void setLockDuration(Integer lockDuration) {
        this.lockDuration = lockDuration;
    }
}
