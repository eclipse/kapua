/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app.command;

import java.util.Objects;
import java.util.function.Function;

public class SimpleCommandApplication extends AbstractCommandApplication {

    private final Function<String, String> handler;

    public SimpleCommandApplication(final Function<String, String> handler) {
        Objects.requireNonNull(handler);
        this.handler = handler;
    }

    @Override
    public Result executeCommand(final String command) {
        return new Result(this.handler.apply(command), "", 0, false);
    }

}
