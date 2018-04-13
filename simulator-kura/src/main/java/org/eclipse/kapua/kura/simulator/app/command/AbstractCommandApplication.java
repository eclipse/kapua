/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app.command;

import org.eclipse.kapua.kura.simulator.app.AbstractDefaultApplication;
import org.eclipse.kapua.kura.simulator.app.Request;
import org.eclipse.kapua.kura.simulator.payload.Metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractCommandApplication extends AbstractDefaultApplication {

    public static class Result {

        private final String standardOutput;
        private final String standardError;
        private final int returnCode;
        private final boolean timeout;

        public Result(final String standardOutput, final String standardError, final int returnCode,
                final boolean timeout) {
            Objects.requireNonNull(standardOutput);
            Objects.requireNonNull(standardError);

            this.standardOutput = standardOutput;
            this.standardError = standardError;
            this.returnCode = returnCode;
            this.timeout = timeout;
        }

        public String getStandardOutput() {
            return standardOutput;
        }

        public String getStandardError() {
            return standardError;
        }

        public int getReturnCode() {
            return returnCode;
        }

        public boolean isTimeout() {
            return timeout;
        }
    }

    public abstract Result executeCommand(String command);

    public AbstractCommandApplication() {
        super("CMD-V1");
    }

    @Override
    protected void processRequest(final Request request) {
        if (!"EXEC/command".equals(request.getMessage().getTopic().render(0, 2))) {
            request.replyNotFound();
            return;
        }

        final String command = Metrics.getAsString(request.getMetrics(), "command.command");

        final Result resultValue = executeCommand(command);
        if (resultValue == null) {
            throw new IllegalStateException("Failed to execute command");
        }

        final Map<String, Object> result = new HashMap<>();
        result.put("command.stdout", resultValue.getStandardOutput());
        result.put("command.stderr", resultValue.getStandardError());
        result.put("command.exit.code", resultValue.getReturnCode());
        result.put("command.timedout", resultValue.isTimeout());
        request.replySuccess().send(result);
    }

}
