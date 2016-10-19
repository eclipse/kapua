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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum ConsoleSettingKeys implements SettingKey {
    SKIN_RESOURCE_DIR("skin.resource.dir"), //
    LOGIN_BACKGROUND_CREDITS("login.background.credits"), //

    DEVICE_CONFIGURATION_ICON_FOLDER("device.configuration.icon.folder"), //
    DEVICE_CONFIGURATION_ICON_CACHE_TIME("device.configuration.icon.cache.time"), //
    DEVICE_CONFIGURATION_ICON_SIZE_MAX("device.configuration.icon.size.max"),

    DEVICE_CONFIGURATION_SERVICE_IGNORE("device.configuration.service.ignore"),//

    DEVICE_MAP_TILE_URI("device.map.tile.uri"), //

    FILE_UPLOAD_SIZE_MAX("file.upload.size.max"), //
    FILE_UPLOAD_INMEMORY_SIZE_THRESHOLD("file.upload.inmemory.size.threshold"), //

    ;

    private String key;

    private ConsoleSettingKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
