/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.message.device.lifecycle;

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link KapuaLifecycleMessage}s {@link KapuaObjectFactory} definition.
 *
 * @since 1.1.0
 */
public interface KapuaLifecycleMessageFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link KapuaAppsMessage}
     *
     * @return the new {@link KapuaAppsMessage}
     * @since 1.1.0
     */
    KapuaAppsMessage newKapuaAppsMessage();

    /**
     * Creates a new {@link KapuaAppsChannel}
     *
     * @return the new {@link KapuaAppsChannel}
     * @since 1.1.0
     */
    KapuaAppsChannel newKapuaAppsChannel();

    /**
     * Creates a new {@link KapuaAppsPayload}.
     *
     * @return the new {@link KapuaAppsPayload}.
     * @since 1.1.0
     */
    KapuaAppsPayload newKapuaAppsPayload();

    /**
     * Creates a new {@link KapuaBirthMessage}
     *
     * @return the new {@link KapuaBirthMessage}
     * @since 1.1.0
     */
    KapuaBirthMessage newKapuaBirthMessage();

    /**
     * Creates a new {@link KapuaBirthChannel}
     *
     * @return the new {@link KapuaBirthChannel}
     * @since 1.1.0
     */
    KapuaBirthChannel newKapuaBirthChannel();

    /**
     * Creates a new {@link KapuaBirthPayload}.
     *
     * @return the new {@link KapuaBirthPayload}.
     * @since 1.1.0
     */
    KapuaBirthPayload newKapuaBirthPayload();

    /**
     * Creates a new {@link KapuaDisconnectMessage}
     *
     * @return the new {@link KapuaDisconnectMessage}
     * @since 1.1.0
     */
    KapuaDisconnectMessage newKapuaDisconnectMessage();

    /**
     * Creates a new {@link KapuaDisconnectChannel}
     *
     * @return the new {@link KapuaDisconnectChannel}
     * @since 1.1.0
     */
    KapuaDisconnectChannel newKapuaDisconnectChannel();

    /**
     * Creates a new {@link KapuaDisconnectPayload}.
     *
     * @return the new {@link KapuaDisconnectPayload}.
     * @since 1.1.0
     */
    KapuaDisconnectPayload newKapuaDisconnectPayload();

    /**
     * Creates a new {@link KapuaMissingMessage}
     *
     * @return the new {@link KapuaMissingMessage}
     * @since 1.1.0
     */
    KapuaMissingMessage newKapuaMissingMessage();

    /**
     * Creates a new {@link KapuaMissingChannel}
     *
     * @return the new {@link KapuaMissingChannel}
     * @since 1.1.0
     */
    KapuaMissingChannel newKapuaMissingChannel();

    /**
     * Creates a new {@link KapuaMissingPayload}.
     *
     * @return the new {@link KapuaMissingPayload}.
     * @since 1.1.0
     */
    KapuaMissingPayload newKapuaMissingPayload();
}
