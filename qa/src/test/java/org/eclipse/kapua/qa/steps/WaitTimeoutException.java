/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.steps;

import java.time.Duration;

public class WaitTimeoutException extends AssertionError {

    private static final long serialVersionUID = 1L;

    public WaitTimeoutException(final Duration period) {
        super(makeMessage("Waiting for condition", period));
    }

    public WaitTimeoutException(final String action, final Duration period) {
        super(makeMessage(action, period));
    }

    private static String makeMessage(final String action, final Duration period) {
        return String.format("%s expired after %s ms", action, period.abs().toMillis());
    }

}
