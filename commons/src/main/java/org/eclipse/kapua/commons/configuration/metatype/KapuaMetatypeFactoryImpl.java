/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration.metatype;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.config.metatype.*;

/**
 * Kapua metatype objects factory service implementation.
 *
 * @since 1.0
 *
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
