/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.translator.cache;

import org.eclipse.kapua.commons.cache.Cache;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.message.Message;
import org.eclipse.kapua.translator.Translator;

import javax.validation.constraints.NotNull;

/**
 * Local {@link Cache} for {@link Translator}.
 * <p>
 * This has been introduced to avoid each time to look throught the {@link java.util.ServiceLoader} available {@link Class}es.
 *
 * @see Cache
 * @see LocalCache
 * @since 1.2.0
 */
public class TranslatorCache extends LocalCache<TranslatorCacheKey, Translator<?, ?>> implements Cache<TranslatorCacheKey, Translator<?, ?>> {

    private static final TranslatorCache TRANSLATOR_CACHE = new TranslatorCache();

    private TranslatorCache() {
        super(50, null);
    }

    /**
     * Gets the {@link Translator} for the given {@link Message} classes if cached.
     *
     * @param fromMessageClass The {@link Message} type from which the {@link Translator} {@link Translator#translate(Message)} from.
     * @param toMessageClass   The {@link Message} type to which the {@link Translator} {@link Translator#translate(Message)} to.
     * @return The matching cached {@link Translator} or {@code null} if not yet cached.
     * @since 1.2.0
     */
    public static <FROM_M extends Message<?, ?>, TO_M extends Message<?, ?>, T extends Translator<FROM_M, TO_M>> T getCachedTranslator(@NotNull Class<? extends FROM_M> fromMessageClass, @NotNull Class<? extends TO_M> toMessageClass) {
        return (T) TRANSLATOR_CACHE.get(new TranslatorCacheKey(fromMessageClass, toMessageClass));
    }

    /**
     * Caches the {@link Translator} for the given {@link Message} classes.
     *
     * @param fromMessageClass The {@link Message} type from which the {@link Translator} {@link Translator#translate(Message)} from.
     * @param toMessageClass   The {@link Message} type to which the {@link Translator} {@link Translator#translate(Message)} to.
     * @param translator       The {@link Translator} to cache.
     * @since 1.2.0
     */
    public static <FROM_M extends Message<?, ?>, TO_M extends Message<?, ?>, T extends Translator<FROM_M, TO_M>> void cacheTranslator(@NotNull Class<? extends FROM_M> fromMessageClass, @NotNull Class<? extends TO_M> toMessageClass, T translator) {
        TranslatorCacheKey key = new TranslatorCacheKey(fromMessageClass, toMessageClass);

        TRANSLATOR_CACHE.put(key, translator);
    }
}
