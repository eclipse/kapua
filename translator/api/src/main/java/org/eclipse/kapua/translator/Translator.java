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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.translator;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.message.Channel;
import org.eclipse.kapua.message.Message;
import org.eclipse.kapua.message.Payload;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;
import org.eclipse.kapua.translator.exception.TranslatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link javassist.Translator} defintions.
 * <p>
 * Translators are used to allow heterogeneous systems to exchange messages through layered messages domain.
 * Kapua has 3 different layer:
 *
 * <ul>
 * <li>Kapua layeer</li>
 * <li>Application layer (i.e. Kura)</li>
 * <li>Transport layer (ie jms, mqtt, ...)</li>
 * </ul>
 *
 * @param <FROM_M> {@link Message} type from which {@link #translate(Message)}.
 * @param <TO_M>   {@link Message} type to which {@link #translate(Message)}.
 * @since 1.0.0
 */
public abstract class Translator<FROM_M extends Message, TO_M extends Message> {

    private static final Logger LOG = LoggerFactory.getLogger(Translator.class);

    private final AccountService accountService;
    private final LocalCache<String, Account> accountsByNameCache;

    /**
     * Constructor for {@link Translator}s that do not need to resolve {@link Account}s (see {@link #findAccountIdByName(String)}).
     *
     * @since 1.0.0
     */
    protected Translator() {
        this.accountService = null;
        this.accountsByNameCache = null;
    }

    /**
     * Constructor for {@link Translator}s that need to resolve {@link Account} ids from {@link Account} names (see {@link #findAccountIdByName(String)}).
     *
     * @param accountService      The {@link AccountService} used to find the {@link Account}s that are not cached.
     * @param accountsByNameCache The {@link LocalCache} of {@link Account}s by {@link Account#getName()}, shared among {@link Translator}s.
     *                            If {@code null}, {@link Account}s are not cached.
     * @since 2.1.0
     */
    protected Translator(AccountService accountService, LocalCache<String, Account> accountsByNameCache) {
        this.accountService = accountService;
        this.accountsByNameCache = accountsByNameCache;
    }

    /**
     * Gets the {@link Account#getId()} of the {@link Account} with the given {@link Account#getName()}.
     * <p>
     * {@link Account}s are cached in the {@link LocalCache} given to {@link #Translator(AccountService, LocalCache)}:
     * <ul>
     *     <li>Cached {@link Account}s are returned without invoking the {@link AccountService}, so permissions are checked only when the {@link Account} is not cached.</li>
     *     <li>Cached {@link Account}s are not invalidated when updated or deleted: a deleted and recreated {@link Account} can be resolved to its old id until its entry expires.</li>
     *     <li>Not found {@link Account}s are never cached.</li>
     * </ul>
     *
     * @param accountName The {@link Account#getName()}.
     * @return The {@link Account#getId()}, or {@code null} if no {@link Account} with the given name exists.
     * @throws KapuaException        If the {@link AccountService} fails to find the {@link Account}.
     * @throws IllegalStateException If this {@link Translator} has been constructed without an {@link AccountService}.
     * @since 2.1.0
     */
    protected KapuaId findAccountIdByName(String accountName) throws KapuaException {
        if (accountService == null) {
            throw new IllegalStateException(String.format("%s has been constructed without an AccountService", getClass().getName()));
        }
        // Argument validation
        ArgumentValidator.notEmptyOrNull(accountName, "accountName");

        // Check cache
        Account account = accountsByNameCache != null ? accountsByNameCache.get(accountName) : null;
        if (account == null) {
            // Do find
            account = accountService.findByName(accountName);
            if (account == null) {
                return null;
            }

            if (accountsByNameCache != null) {
                accountsByNameCache.put(accountName, account);
            }
        }

        // Return result
        return account.getId();
    }

    /**
     * Translates {@link Message} from the domain FROM_M to the domain TO_M
     *
     * @param message The {@link Message} to translate.
     * @return the translated {@link Message}.
     * @throws InvalidChannelException If the {@link Channel} cannot be translated.
     * @throws InvalidPayloadException If the {@link Payload} cannot be translated.
     * @throws InvalidMessageException If the {@link Message} cannot be translated.
     * @throws TranslateException      This is the parent {@link TranslatorException} of:
     *                                 <ul>
     *                                     <li>InvalidChannelException</li>
     *                                     <li>InvalidPayloadException</li>
     *                                     <li>InvalidMessageException</li>
     *                                 </ul>
     *                                 which can be used to catch 'em all.
     * @since 1.0.0
     */
    public abstract TO_M translate(FROM_M message) throws TranslateException;

    /**
     * Returns the FROM_M {@link Message} type.
     *
     * @return The FROM_M {@link Message} type.
     * @since 1.0.0
     */
    public abstract Class<FROM_M> getClassFrom();

    /**
     * Returns the TO_M {@link Message} type.
     *
     * @return The TO_M {@link Message} type.
     * @since 1.0.0
     */
    public abstract Class<TO_M> getClassTo();
}
