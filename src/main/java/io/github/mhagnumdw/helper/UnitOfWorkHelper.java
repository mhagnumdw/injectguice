package io.github.mhagnumdw.helper;

import org.slf4j.Logger;

import com.google.inject.persist.UnitOfWork;

import io.github.mhagnumdw.GuiceInjector;

/**
 * Helper para iniciar e encerrar uma unidade de trabalho de persistência de dados: {@link UnitOfWork}.
 */
public final class UnitOfWorkHelper {

    /**
     * Inicia um {@link UnitOfWork}.
     *
     * @param log
     *            logger para escrita. É obrigatório.
     *
     * @return {@link UnitOfWork}
     */
    public static UnitOfWork begin(Logger log) {
        final UnitOfWork unitOfWork = GuiceInjector.get().getInstance(UnitOfWork.class);
        log.trace("call unitOfWork.begin");
        unitOfWork.begin();
        return unitOfWork;
    }

    /**
     * Encerra um {@link UnitOfWork}.
     *
     * @param unitOfWork
     *            que deve ser encerrada. Pode ser nulo.
     * @param log
     *            logger para escrita. É obrigatório.
     */
    public static void end(UnitOfWork unitOfWork, Logger log) {
        if (unitOfWork != null) {
            log.trace("call unitOfWork.end");
            unitOfWork.end();
        }
    }

}
