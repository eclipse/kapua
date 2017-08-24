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
package org.eclipse.kapua.app.console.module.tag.shared.util;

import org.eclipse.kapua.app.console.module.api.shared.util.KapuaGwtCommonsModelConverter;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.service.tag.Tag;

public class KapuaGwtTagModelConverter {

    private KapuaGwtTagModelConverter() { }

    /**
     * Converts a {@link Tag} into a {@link GwtTag}
     *
     * @param tag The {@link Tag} to convertKapuaId
     * @return The converted {@link GwtTag}
     * @since 1.0.0
     */
    public static GwtTag convertTag(Tag tag) {

        GwtTag gwtTag = new GwtTag();
        //
        // Covert commons attributes
        KapuaGwtCommonsModelConverter.convertEntity(tag, gwtTag);

        //
        // Convert other attributes
        gwtTag.setTagName(tag.getName());

        return gwtTag;
    }
}
