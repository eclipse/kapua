/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.configuration.metatype;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTdesignate;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTobject;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.model.config.metatype.KapuaTscalar;

/**
 * Kapua metatype objects factory service implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class KapuaMetatypeFactoryImpl implements KapuaMetatypeFactory {

    @Override
    public KapuaTocd newKapuaTocd() {
        return new TocdImpl();
    }

    @Override
    public KapuaTad newKapuaTad() {
        return new TadImpl();
    }

    @Override
    public KapuaTscalar newKapuaTscalar(String type) {
        return TscalarImpl.fromValue(type);
    }

    @Override
    public KapuaToption newKapuaToption() {
        return new ToptionImpl();
    }

    @Override
    public KapuaTicon newKapuaTicon() {
        return new TiconImpl();
    }

    public KapuaTmetadata newKapuaTmetadata() {
        return new TmetadataImpl();
    }

    public KapuaTdesignate newKapuaTdesignate() {
        return new TdesignateImpl();
    }

    public KapuaTobject newKapuaTobject() {
        return new TobjectImpl();
    }
}
