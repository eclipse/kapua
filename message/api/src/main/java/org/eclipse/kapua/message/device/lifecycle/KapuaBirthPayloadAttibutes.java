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

/**
 * {@link KapuaBirthPayloadAttibutes} definitions.
 * <p>
 * All available fields in a {@link KapuaBirthPayload}
 *
 * @since 1.0.0
 */
public class KapuaBirthPayloadAttibutes {

    private KapuaBirthPayloadAttibutes() {
    }

    /**
     * @since 1.0.0
     */
    public static final String UPTIME = "uptime";

    /**
     * @since 1.0.0
     */
    public static final String DISPLAY_NAME = "displayName";

    /**
     * @since 1.0.0
     */
    public static final String MODEL_NAME = "modelName";

    /**
     * @since 1.0.0
     */
    public static final String MODEL_ID = "modelId";

    /**
     * @since 1.0.0
     */
    public static final String PART_NUMBER = "partNumber";

    /**
     * @since 1.0.0
     */
    public static final String SERIAL_NUMBER = "serialNumber";

    /**
     * @since 1.0.0
     */
    public static final String FIRMWARE = "firmware";

    /**
     * @since 1.0.0
     */
    public static final String FIRMWARE_VERSION = "firmwareVersion";

    /**
     * @since 1.0.0
     */
    public static final String BIOS = "bios";

    /**
     * @since 1.0.0
     */
    public static final String BIOS_VERSION = "biosVersion";

    /**
     * @since 1.0.0
     */
    public static final String OS = "os";

    /**
     * @since 1.0.0
     */
    public static final String OS_ARCH = "osArch";

    /**
     * @since 1.0.0
     */
    public static final String OS_VERSION = "osVersion";

    /**
     * @since 1.0.0
     */
    public static final String JVM = "jvm";

    /**
     * @since 1.0.0
     */
    public static final String JVM_VERSION = "jvmVersion";

    /**
     * @since 1.0.0
     */
    public static final String JVM_PROFILE = "jvmProfile";

    /**
     * @since 1.0.0
     */
    public static final String CONTAINER_FRAMEWORK = "containerFramework";

    /**
     * @since 1.0.0
     */
    public static final String CONTAINER_FRAMEWORK_VERSION = "containerFrameworkVersion";

    /**
     * @since 1.0.0
     */
    public static final String APPLICATION_FRAMEWORK = "applicationFramework";

    /**
     * @since 1.0.0
     */
    public static final String APPLICATION_FRAMEWORK_VERSION = "applicationFrameworkVersion";

    /**
     * @since 1.0.0
     */
    public static final String CONNECTION_INTERFACE = "connectionInterface";

    /**
     * @since 1.0.0
     */
    public static final String CONNECTION_IP = "connectionIp";

    /**
     * @since 1.0.0
     */
    public static final String ACCEPT_ENCODING = "acceptEncoding";

    /**
     * @since 1.0.0
     */
    public static final String APPLICATION_IDENTIFIERS = "applicationIdentifiers";

    /**
     * @since 1.0.0
     */
    public static final String AVAILABLE_PROCESSORS = "availableProcessors";

    /**
     * @since 1.0.0
     */
    public static final String TOTAL_MEMORY = "totalMemory";

    /**
     * @since 1.0.0
     */
    public static final String MODEM_IMEI = "modemImei";

    /**
     * @since 1.0.0
     */
    public static final String MODEM_IMSI = "modemImsi";

    /**
     * @since 1.0.0
     */
    public static final String MODEM_ICCID = "modemIccid";

    /**
     * @since 1.5.0
     */
    public static final String EXTENDED_PROPERTIES = "extendedProperties";
}
