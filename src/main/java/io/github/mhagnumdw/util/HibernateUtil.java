package io.github.mhagnumdw.util;

import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.SingularAttribute;

import org.hibernate.Session;

/**
 * <pre>
 * Diversos:
 * http://www.petrikainulainen.net/programming/tips-and-tricks/implementing-a-custom-namingstrategy-with-hibernate/
 *
 * JPA 2 Static Metamodel:
 * http://stackoverflow.com/questions/37714560/hibernate-jpa-model-class-generation-in-maven-project
 * http://blog.caelum.com.br/consultas-fortemente-tipadas-com-jpa/
 * https://docs.jboss.org/hibernate/orm/5.0/topical/html/metamodelgen/MetamodelGenerator.html
 * </pre>
 */
public class HibernateUtil {

    // private static final EntityManagerFactory emf = buildEntityManagerFactory();

    // private static EntityManagerFactory buildEntityManagerFactory() {
    // try {
    // return Persistence.createEntityManagerFactory(Constantes.PU_NAME);
    // } catch (Throwable ex) {
    // log.error("Initial EntityManagerFactory creation failed." + ex);
    // throw new ExceptionInInitializerError(ex);
    // }
    // }

    // public static EntityManager createEntityManager() {
    // log.trace("Criando EntityManager ...");
    // EntityManager em = emf.createEntityManager();
    // log.trace("... EntityManager criado!");
    // return em;
    // }

    public static Session getSession(EntityManager em) {
        return em.unwrap(Session.class);
    }

//    public static Dialect getDialect(EntityManager em) {
//        return getDialect(getSession(em));
//    }
//
//    public static Dialect getDialect(Session session) {
//        return ((SessionFactoryImplementor) session.getSessionFactory()).getJdbcServices().getDialect();
//    }

    /**
     * Seta os parâmetros na {@code query}.
     *
     * @param query
     *            query
     * @param params
     *            parâmetro da query, pode ser nulo
     */
    public static void setParameters(Query query, Map<SingularAttribute<?, ?>, Object> params) {
        if (CollectionUtils.isNullOrEmpty(params)) {
            return;
        }
        Set<SingularAttribute<?, ?>> parametros = params.keySet();
        for (SingularAttribute<?, ?> parametro : parametros) {
            query.setParameter(parametro.getName(), params.get(parametro));
        }
    }

//    /**
//     * Seta os parâmetros na {@code query}.
//     *
//     * @param query
//     *            query
//     * @param params
//     *            parâmetro da query, pode ser nulo
//     */
//    public static void setParameters(org.hibernate.Query query, Map<SingularAttribute<?, ?>, Object> params) {
//        if (CollectionUtils.isNullOrEmpty(params)) {
//            return;
//        }
//        Set<SingularAttribute<?, ?>> parametros = params.keySet();
//        for (SingularAttribute<?, ?> parametro : parametros) {
//            query.setParameter(parametro.getName(), params.get(parametro));
//        }
//    }

    /**
     * Seta os parâmetros na {@code query}.
     *
     * @param query
     *            query
     * @param params
     *            parâmetro da query, pode ser nulo
     */
    public static void setParametersString(Query query, Map<String, Object> params) {
        if (CollectionUtils.isNullOrEmpty(params)) {
            return;
        }
        Set<String> parametros = params.keySet();
        for (String parametro : parametros) {
            query.setParameter(parametro, params.get(parametro));
        }
    }

//    /**
//     * Seta os parâmetros na {@code query}.
//     *
//     * @param query
//     *            query
//     * @param params
//     *            parâmetro da query, pode ser nulo
//     */
//    public static void setParametersString(org.hibernate.Query query, Map<String, Object> params) {
//        if (CollectionUtils.isNullOrEmpty(params)) {
//            return;
//        }
//        Set<String> parametros = params.keySet();
//        for (String parametro : parametros) {
//            query.setParameter(parametro, params.get(parametro));
//        }
//    }
//
//    /**
//     * Retorna o nome dos parâmetros da query. O padrão dos nomes é <code>:name</code>.
//     *
//     * @param queryString
//     *            query
//     *
//     * @return parâmetros da query
//     */
//    public static List<String> getParametersNames(String queryString) {
//        final String REGEX_PARAM = ":[^\\s]+";
//        List<String> names = RegexUtils.getAllOccurrences(queryString, REGEX_PARAM);
//        return names.stream().map(n -> n.replaceFirst(":", "")).collect(Collectors.toList());
//    }
//
//    /**
//     * Monta a query como String adicionando a {@code whereClause}.
//     * <p>
//     * <b>ATENÇÃO: Esse método é bem limitado, mas funciona muito bem para query simples [{@code queryString}], como exemplo:
//     * <i>{@literal SELECT d FROM Demanda d WHERE d.id > 0 ORDER BY d.dataInicio DESC, d.id}</i></b>
//     * </p>
//     *
//     * <p>
//     * Ideia obtida da classe HqlUtil do GRPFOR.
//     * </p>
//     *
//     * @param queryString
//     *            query
//     * @param whereClause
//     *            condições do where, pode ser nulo
//     *
//     * @return queryString com {@code whereClause} adicionda
//     */
//    public static String addWhereClause(String queryString, String whereClause) {
//        if (StringUtils.isNotBlank(whereClause)) {
//            if (queryString.matches("(?i).+\\s+order\\s+by\\s+.+")) {
//                String querySelect = queryString.replaceAll("(?i)\\s+order\\s+by.+", "");
//                queryString = querySelect + " WHERE " + whereClause + queryString.replace(querySelect, "");
//            } else {
//                queryString = queryString + " WHERE " + whereClause;
//            }
//        }
//        return queryString;
//    }
//
//    /**
//     * Cria uma instância de {@code TypedQuery} com base num objeto derivado de {@code SearchAbstract}.
//     *
//     * @param search
//     *            objeto que contém as diretivas para realizar a pesquisa
//     * @param em
//     *            EntityManager
//     *
//     * @return instância de {@link TypedQuery}
//     *
//     * @see #createCountQuery(SearchAbstract, EntityManager)
//     */
//    public static <T, S extends SearchAbstract<T>> TypedQuery<T> createQuery(S search, EntityManager em) {
//        FilterCriteriaBuilder<T> fcb = createFilterCriteriaBuilder(search, em);
//        CriteriaQuery<T> criteriaQuery = fcb.getQuery(); // CriteriaQuery da JPA
//        TypedQuery<T> query = em.createQuery(criteriaQuery);
//        return query;
//    }
//
//    /**
//     * Cria uma instância de {@code TypedQuery} com base num objeto derivado de {@code SearchAbstract} para realizar um {@code count}.
//     *
//     * @param search
//     *            objeto que contém as diretivas para realizar a pesquisa
//     * @param em
//     *            EntityManager
//     *
//     * @return instância de {@link TypedQuery}
//     *
//     * @see #createQuery(SearchAbstract, EntityManager)
//     */
//    public static <T, S extends SearchAbstract<T>> TypedQuery<Long> createCountQuery(S search, EntityManager em) {
//        FilterCriteriaBuilder<T> fcb = createFilterCriteriaBuilder(search, em);
//        CriteriaQuery<Long> criteriaQuery = fcb.getCountQuery(); // CriteriaQuery da JPA
//        TypedQuery<Long> query = em.createQuery(criteriaQuery);
//        return query;
//    }
//
//    private static <T, S extends SearchAbstract<T>> FilterCriteriaBuilder<T> createFilterCriteriaBuilder(S search, EntityManager em) {
//        FilterCriteriaBuilder<T> fcb = new FilterCriteriaBuilder<T>(em, search.getType());
//        List<FieldFilter> filters = buildFilters(search);
//        LinkedHashMap<String, Boolean> orders = search.getOrderBy();
//        fcb.addFilters(filters);
//        fcb.addOrders(orders);
//        return fcb;
//    }
//
//    private static <S extends SearchAbstract<?>> List<FieldFilter> buildFilters(S search) {
//        List<FieldFilter> filters = new ArrayList<>();
//        List<Field> fields = FieldUtils.getFieldsListWithAnnotation(search.getClass(), SearchFilter.class);
//        for (Field field : fields) {
//            SearchFilter filterAnnotation = field.getAnnotation(SearchFilter.class);
//            String attribute = filterAnnotation.attribute();
//            EnumSet<Option> options = EnumSet.copyOf(Arrays.asList(filterAnnotation.options()));
//            Object value = ReflectionUtils.get(search, field);
//            if (value instanceof CharSequence && StringUtils.isBlank((CharSequence) value)) {
//                value = null;
//            }
//            if (value == null /* && !options.contains(FieldFilter.Option.IS_NULL) */) {
//                continue;
//            }
//            filters.add(new FieldFilter(attribute, value, options));
//        }
//        return filters;
//    }
//
//    /**
//     * Recupera as estatísticas do Hibernate para a <i>session factory</i> ligada ao {@code EntityManager} passado como parâmetro.
//     *
//     * @param em
//     *            EntityManager
//     *
//     * @return HibernateStatistics
//     */
//    public static HibernateStatistics getHibernateStatistics(EntityManager em) {
//        Statistics statistics = getStatistics(em);
//        return new HibernateStatistics(statistics);
//    }
//
//    private static Statistics getStatistics(EntityManager em) {
//        Session session = getSession(em);
//        SessionFactory sessionFactory = session.getSessionFactory();
//        return sessionFactory.getStatistics();
//    }
//
//    /**
//     * Força a inicialização de todos os proxies da entidade, percorrendo todo o grafo da entidade.
//     * <p>
//     * Esse método serializa toda a entidade para forçar que o Hibernate inicialize os proxies, contornando a limitação do método {@link Hibernate#initialize(Object)}
//     * </p>
//     *
//     * @param entity
//     *            entidade
//     */
//    public static void initialize(Object entity) {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
//        Gson gson = gsonBuilder.create();
//        gson.toJson(entity);
//    }
//
//    /**
//     * Verifica se o {@code EntityManager} está sujo.
//     *
//     * @param unitOfWork
//     *            instância de {@link UnitOfWork}
//     *
//     * @return {@code true} se sujo, {@code false} se não, {@code null} se tiver havido exceção
//     */
//    public static Boolean entityManagerIsDirty(UnitOfWork unitOfWork) {
//        try {
//            ThreadLocal<EntityManager> emThreadLocal = (ThreadLocal<EntityManager>) ReflectionUtils.get(unitOfWork, "entityManager");
//            EntityManager em = emThreadLocal.get();
//            Session session = HibernateUtil.getSession(em);
//            return session.isDirty();
//        } catch (Exception e) {
//            return null;
//        }
//    }

}
