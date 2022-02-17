/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.spi.util;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Helper methods for working with {@link Future}s or {@link CompletionStage}s
 */
public final class Futures {

    private Futures() {
    }

    public static <T> CompletableFuture<T> completedExceptionally(Throwable error) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(error);
        return future;
    }

    private static <T> CompletionStage<T> reportTo(final CompletionStage<T> source, final CompletableFuture<T> target) {
        return source.whenComplete((value, error) -> {
            if (error != null) {
                target.completeExceptionally(error);
            } else {
                target.complete(value);
            }
        });
    }

    public static <T> CompletableFuture<T> mapFailed(final CompletionStage<T> stage, final Function<Throwable, CompletionStage<T>> function) {

        Objects.requireNonNull(stage);
        Objects.requireNonNull(function);

        final CompletableFuture<T> future = new CompletableFuture<>();

        stage.whenComplete((value, error) -> {
            try {
                if (error != null) {
                    final CompletionStage<T> result = function.apply(error);
                    if (result != null) {
                        reportTo(result, future);
                    } else {
                        future.completeExceptionally(error);
                    }
                } else {
                    future.complete(value);
                }
            } catch (final Exception e) {
                future.completeExceptionally(new CompletionException(e));
            }
        });

        return future;
    }

    public static <T, R> CompletableFuture<R> map(final CompletionStage<T> stage, final BiFunction<T, Throwable, CompletionStage<R>> function) {

        Objects.requireNonNull(stage);
        Objects.requireNonNull(function);

        final CompletableFuture<R> future = new CompletableFuture<>();

        stage.whenComplete((value, error) -> {
            try {
                final CompletionStage<R> result = function.apply(value, error);
                reportTo(result, future);
            } catch (final Exception e) {
                future.completeExceptionally(new CompletionException(e));
            }
        });

        return future;
    }
}
