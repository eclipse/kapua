package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.storage.TxContext;

import java.util.function.BiConsumer;

public interface EventStorer extends BiConsumer<TxContext, KapuaEntity> {
    @Override
    void accept(TxContext tx, KapuaEntity kapuaEntity);
}
