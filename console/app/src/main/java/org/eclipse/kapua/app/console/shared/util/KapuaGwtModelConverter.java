/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.util;

import org.eclipse.kapua.app.console.commons.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.commons.shared.model.GwtUpdatableEntityModel;
import org.eclipse.kapua.app.console.shared.model.GwtTag;
import org.eclipse.kapua.app.console.shared.model.GwtTopic;
import org.eclipse.kapua.app.console.commons.shared.model.GwtUpdatableEntityModel;
import org.eclipse.kapua.app.console.shared.model.device.management.assets.GwtDeviceAsset;
import org.eclipse.kapua.app.console.shared.model.device.management.assets.GwtDeviceAssetChannel;
import org.eclipse.kapua.app.console.shared.model.device.management.assets.GwtDeviceAssets;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnection.GwtConnectionUserCouplingMode;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnectionOption;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.tag.Tag;

public class KapuaGwtModelConverter {

    private KapuaGwtModelConverter() {
    }

    /**
     * Converts a {@link KapuaId} into its {@link String} short id representation.
     * <p>
     * Example: 1 =&gt; AQ
     * </p>
     *
     * @param kapuaId The {@link KapuaId} to convertKapuaId
     * @return The short id representation of the {@link KapuaId}
     * @since 1.0.0
     */
    public static String convert(KapuaId kapuaId) {
        if (kapuaId == null) {
            return null;
        }

        //
        // Return converted entity
        return kapuaId.toCompactId();
    }

    /**
     * Converts a {@link Tag} into a {@link GwtTag}
     *
     * @param tag The {@link Tag} to convertKapuaId
     * @return The converted {@link GwtTag}
     * @since 1.0.0
     */
    public static GwtTag convert(Tag tag) {

        GwtTag gwtTag = new GwtTag();
        //
        // Covert commons attributes
        convertEntity(tag, gwtTag);

        //
        // Convert other attributes
        gwtTag.setTagName(tag.getName());

        return gwtTag;
    }

    /**
     * Utility method to convertKapuaId commons properties of {@link KapuaUpdatableEntity} object to the GWT matching {@link GwtUpdatableEntityModel} object
     *
     * @param kapuaEntity The {@link KapuaUpdatableEntity} from which to copy values
     * @param gwtEntity   The {@link GwtUpdatableEntityModel} into which copy values
     * @since 1.0.0
     */
    private static void convertEntity(KapuaUpdatableEntity kapuaEntity, GwtUpdatableEntityModel gwtEntity) {
        if (kapuaEntity == null || gwtEntity == null) {
            return;
        }

        convertEntity((KapuaEntity) kapuaEntity, (GwtEntityModel) gwtEntity);

        gwtEntity.setModifiedOn(kapuaEntity.getModifiedOn());
        gwtEntity.setModifiedBy(convert(kapuaEntity.getModifiedBy()));
        gwtEntity.setOptlock(kapuaEntity.getOptlock());
    }

    /**
     * Utility method to convertKapuaId commons properties of {@link KapuaEntity} object to the GWT matching {@link GwtEntityModel} object
     *
     * @param kapuaEntity The {@link KapuaEntity} from which to copy values
     * @param gwtEntity   The {@link GwtEntityModel} into which copy values
     * @since 1.0.0
     */
    private static void convertEntity(KapuaEntity kapuaEntity, GwtEntityModel gwtEntity) {
        if (kapuaEntity == null || gwtEntity == null) {
            return;
        }

        gwtEntity.setScopeId(convert(kapuaEntity.getScopeId()));
        gwtEntity.setId(convert(kapuaEntity.getId()));
        gwtEntity.setCreatedOn(kapuaEntity.getCreatedOn());
        gwtEntity.setCreatedBy(convert(kapuaEntity.getCreatedBy()));
    }

}
