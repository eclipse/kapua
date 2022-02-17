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
package org.eclipse.kapua.app.console.module.api.shared.util;

import com.extjs.gxt.ui.client.data.BaseModel;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtConfigParameter;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for convertKapuaId {@link BaseModel}s to {@link KapuaEntity}ies and other Kapua models
 */
public class GwtKapuaCommonsModelConverter {

    private GwtKapuaCommonsModelConverter() {
    }

    /**
     * Utility method to convertKapuaId commons properties of {@link GwtUpdatableEntityModel} object to the matching {@link KapuaUpdatableEntity} object
     *
     * @param gwtEntity
     *            The {@link GwtUpdatableEntityModel} from which copy values
     * @param kapuaEntity
     *            The {@link KapuaUpdatableEntity} into which to copy values
     * @since 1.0.0
     */
    public static void convertUpdatableEntity(GwtUpdatableEntityModel gwtEntity, KapuaUpdatableEntity kapuaEntity) {
        if (kapuaEntity == null || gwtEntity == null) {
            return;
        }

        convertEntity(gwtEntity, kapuaEntity);

        kapuaEntity.setOptlock(gwtEntity.getOptlock());
    }

    /**
     * Utility method to convertKapuaId commons properties of {@link GwtEntityModel} object to the matching {@link KapuaEntity} object
     *
     * @param gwtEntity
     *            The {@link GwtEntityModel} from which copy values
     * @param kapuaEntity
     *            The {@link KapuaEntity} into which to copy values
     * @since 1.0.0
     */
    public static void convertEntity(GwtEntityModel gwtEntity, KapuaEntity kapuaEntity) {
        if (kapuaEntity == null || gwtEntity == null) {
            return;
        }

        kapuaEntity.setId(convertKapuaId(gwtEntity.getId()));
    }

    /**
     * Converts a {@link KapuaId} form the short form to the actual object.
     * <p>
     * Example: AQ =&gt; 1
     * </p>
     *
     * @param shortKapuaId
     *            the {@link KapuaId} in the short form
     * @return The converted {@link KapuaId}
     * @since 1.0.0
     */
    public static KapuaId convertKapuaId(String shortKapuaId) {
        if (shortKapuaId == null) {
            return null;
        }
        return KapuaEid.parseCompactId(shortKapuaId);
    }

    public static Map<String, Object> convertConfigComponent(GwtConfigComponent configComponent) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        for (GwtConfigParameter gwtConfigParameter : configComponent.getParameters()) {
            switch (gwtConfigParameter.getType()) {
            case BOOLEAN:
                parameters.put(gwtConfigParameter.getId(), gwtConfigParameter.getValue() != null ? Boolean.parseBoolean(gwtConfigParameter.getValue()) : null);
                break;
            case BYTE:
                parameters.put(gwtConfigParameter.getId(), gwtConfigParameter.getValue() != null ? Byte.parseByte(gwtConfigParameter.getValue()) : null);
                break;
            case CHAR:
                parameters.put(gwtConfigParameter.getId(), gwtConfigParameter.getValue() != null ? gwtConfigParameter.getValue().toCharArray() : null);
                break;
            case DOUBLE:
                parameters.put(gwtConfigParameter.getId(), gwtConfigParameter.getValue() != null ? Double.parseDouble(gwtConfigParameter.getValue()) : null);
                break;
            case FLOAT:
                parameters.put(gwtConfigParameter.getId(), gwtConfigParameter.getValue() != null ? Float.parseFloat(gwtConfigParameter.getValue()) : null);
                break;
            case INTEGER:
                parameters.put(gwtConfigParameter.getId(), gwtConfigParameter.getValue() != null ? Integer.parseInt(gwtConfigParameter.getValue()) : null);
                break;
            case LONG:
                parameters.put(gwtConfigParameter.getId(), gwtConfigParameter.getValue() != null ? Long.parseLong(gwtConfigParameter.getValue()) : null);
                break;
            case PASSWORD:
            case STRING:
            default:
                parameters.put(gwtConfigParameter.getId(), gwtConfigParameter.getValue());
                break;
            case SHORT:
                parameters.put(gwtConfigParameter.getId(), gwtConfigParameter.getValue() != null ? Short.parseShort(gwtConfigParameter.getValue()) : null);
                break;
            }
        }
        return parameters;
    }

}
