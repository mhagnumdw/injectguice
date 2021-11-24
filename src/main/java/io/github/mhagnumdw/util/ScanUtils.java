package io.github.mhagnumdw.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mhagnumdw.EmptyRoot;

/**
 * Utilitário para escanear Classes, Métodos, Anotações etc em tempo de execução.
 *
 * @see ReflectionUtils
 */
public class ScanUtils {

    private static final Logger log = LoggerFactory.getLogger(ScanUtils.class);

    private static Reflections instance = null;

    private static Reflections getInstance() {
        if (instance == null) {
            synchronized (ScanUtils.class) {
                if (instance == null) {
                    instance = buildDefaultReflections();
                }
            }
        }
        return instance;
    }

    /**
     * Retorna os {@link Method}'s anotados com {@code annotation}.
     *
     * @param annotation
     *            Classe que estenda de {@link Annotation}
     *
     * @return {@link Method}'s anotados com {@code annotation}.
     */
    public static Set<Method> getMethodsAnnotatedWith(final Class<? extends Annotation> annotation) {
        return getInstance().getMethodsAnnotatedWith(annotation);
    }

    /**
     * Retorna as sub-classes e as classes que implementam {@code type}.
     *
     * @param type
     *            tipo
     *
     * @return tipos
     */
    public static <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type) {
        return getInstance().getSubTypesOf(type);
    }

    /**
     * Retorna as sub-classes e as classes que implementam {@code type} que possuam a anotação {@code annotation}.
     *
     * @param type
     *            tipo
     * @param annotation
     *            Classe que estenda de {@link Annotation}
     *
     * @return tipos
     */
    public static <T> Set<Class<? extends T>> getSubTypesOfAnnotatedWith(final Class<T> type, final Class<? extends Annotation> annotation) {
        Set<Class<? extends T>> subTypes = getInstance().getSubTypesOf(type);
        return subTypes.stream().filter(clazz -> clazz.isAnnotationPresent(annotation)).collect(Collectors.toSet());
    }

    /**
     * Criar a instância de {@link Reflections} para um pacote específico.
     *
     * @param packageToScan
     *            Pacote onde a busca deve ocorrer
     */
    private static Reflections buildReflections(String packageToScan) {
        log.info("Criando configuracao");

        final ConfigurationBuilder builder = new ConfigurationBuilder();

        builder.addUrls(ClasspathHelper.forPackage(packageToScan));

        // @formatter:off
        builder.setScanners(
            new MethodAnnotationsScanner(),
            new SubTypesScanner() // É possível adicionar outros, como: new TypeAnnotationsScanner()
        );
        // @formatter:on

        final Reflections reflections = new Reflections(builder);

        log.info("Configuracao criada");

        return reflections;
    }

    private static Reflections buildDefaultReflections() {
        return buildReflections(EmptyRoot.class.getPackage().getName());
    }

}
