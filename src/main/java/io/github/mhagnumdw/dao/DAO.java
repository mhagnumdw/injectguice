package io.github.mhagnumdw.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NamedQuery;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.SingularAttribute;

import org.hibernate.annotations.NaturalId;

import io.github.mhagnumdw.PaginationScrollStatus;
import io.github.mhagnumdw.search.SearchAbstract;
import io.github.mhagnumdw.search.SortOrder;
import io.github.mhagnumdw.util.Pair;

public interface DAO {

    public EntityManager getEntityManager();

    /**
     * Busca pela chave natural que deve ser definida na entidade por meio da anotação {@link NaturalId}.
     *
     * @param resultClass
     *            classe da entidade
     * @param naturalIdValue
     *            valor
     *
     * @return instância da entidade ou nulo se a entidade não existir
     *
     * @see #getReferenceBySimpleNaturalId(Class, Object)
     */
    public <T> T getBySimpleNaturalId(Class<T> resultClass, Object naturalIdValue);

    /**
     * Busca, podendo retornar apenas uma referência, pela chave natural que deve ser definida na entidade por meio da anotação {@link NaturalId}.
     *
     * <p>
     * <b>Assume que a entidade existe.</b>
     * </p>
     *
     * @param resultClass
     *            classe da entidade
     * @param naturalIdValue
     *            valor
     *
     * @return instância da entidade ou apenas uma referência que pode ou não existir
     *
     * @see #getBySimpleNaturalId(Class, Object)
     * @see org.hibernate.SimpleNaturalIdLoadAccess#getReference(Object)
     */
    public <T> T getReferenceBySimpleNaturalId(Class<T> resultClass, Object naturalIdValue);

    /**
     * Busca pelo {@code id}.
     *
     * @param id
     *            chave primária
     * @param resultClass
     *            classe da entidade
     *
     * @return instância da entidade ou nulo se a entidade não existir
     */
    public <T> T getById(Serializable id, Class<T> resultClass);

    /**
     * Busca por uma coleção de ids.
     *
     * @param ids
     *            chaves primárias
     * @param resultClass
     *            classe da entidade
     * @return lista contendo as instâncias das entidades ou lista vazia se não existir entidade
     */
    public <T> List<T> getByIds(Collection<?> ids, Class<T> resultClass);

    /**
     * Busca por meio de um atributo da entidade.
     * <p>
     * Lógica: FROM resultClass e WHERE e.attribute = :value
     * </p>
     *
     * @param attributeToSearch
     *            atributo da entidade pelo qual será pesquisado
     * @param value
     *            valor do atributo
     * @param resultClass
     *            classe da entidade
     *
     * @return lista contendo as instâncias das entidades ou lista vazia se não existir entidade
     */
    public <T> List<T> getByDirectAttribute(SingularAttribute<?, ?> attributeToSearch, Object value, Class<T> resultClass);

    /**
     * Busca por meio de um atributo da entidade.
     * <p>
     * Lógica: FROM resultClass e WHERE e.attribute in :values
     * </p>
     *
     * @param attributeToSearch
     *            atributo da entidade pelo qual será pesquisado
     * @param values
     *            possíveis valores do atributo
     * @param resultClass
     *            classe da entidade
     *
     * @return lista contendo as instâncias das entidades ou lista vazia se não existir entidade
     */
    public <E, T> List<E> getByDirectAttributeIn(SingularAttribute<?, T> attributeToSearch, Collection<T> values, Class<E> resultClass);

    /**
     * Busca por meio de atributos da entidade.
     * <p>
     * Lógica: FROM resultClass e WHERE e.attribute1 = :value1 AND ... AND e.attributeN = :valueN
     * </p>
     *
     * @param params
     *            mapa contendo os atributos da entidades e seus respectivos valores para servir de filtro (são as condições de pesquisa)
     * @param resultClass
     *            classe da entidade
     *
     * @return lista contendo as instâncias das entidades ou lista vazia se não existir entidade
     */
    public <T> List<T> getByDirectAttributes(Map<SingularAttribute<?, ?>, Object> params, Class<T> resultClass);

    /**
     * Busca por meio de atributos da entidade fazendo ordenação.
     *
     * @param params
     *            mapa contendo os atributos da entidades e seus respectivos valores para servir de filtro (são as condições de pesquisa)
     * @param resultClass
     *            classe da entidade
     * @param orderByAttribute
     *            atributo que dita a ordem
     * @param sortOrder
     *            tipo de ordenação conforme {@link SortOrder}
     *
     * @return lista contendo as instâncias das entidades ou lista vazia se não existir entidade
     */
    public <T> List<T> getByDirectAttributes(Map<SingularAttribute<?, ?>, Object> params, Class<T> resultClass, SingularAttribute<?, ?> orderByAttribute, SortOrder sortOrder);

    /**
     * Busca por meio de um atributo da entidade que inicie com {@code value}. O atributo deve ser do tipo {@code String}.
     * <p>
     * Lógica: FROM resultClass e WHERE e.attribute like %:value
     * </p>
     *
     * @param attributeToSearch
     *            atributo da entidade pelo qual será pesquisado
     * @param value
     *            valor do atributo
     * @param resultClass
     *            classe da entidade
     * @return lista contendo as instâncias das entidades ou lista vazia se não existir entidade
     */
    public <T> List<T> getByDirectAttributeStartWith(SingularAttribute<?, String> attributeToSearch, String value, Class<T> resultClass);

    /**
     * Busca todas as entidades.
     *
     * @param resultClass
     *            classe da entidade
     *
     * @return lista contendo as instâncias das entidades ou lista vazia se não existir entidade
     */
    public <T> List<T> getAll(Class<T> resultClass);

    /**
     * Busca todas as entidades fazendo ordenação.
     *
     * @param resultClass
     *            classe da entidade
     * @param orderByAttribute
     *            atributo que dita a ordem
     * @param sortOrder
     *            tipo de ordenação conforme {@link SortOrder}
     * @return lista ordenada contendo as instâncias das entidades ou lista vazia se não existir entidade
     *
     * @see #getAll(Class, SingularAttribute, SortOrder, boolean)
     */
    public <T> List<T> getAll(Class<T> resultClass, SingularAttribute<?, ?> orderByAttribute, SortOrder sortOrder);

    /**
     * Busca todas as entidades fazendo ordenação.
     *
     * @param resultClass
     *            classe da entidade
     * @param orderByAttribute
     *            atributo que dita a ordem
     * @param sortOrder
     *            tipo de ordenação conforme {@link SortOrder}
     * @param lowerInOrderBy
     *            se {@code true} aplica um SQL LOWER no order by
     * @return lista ordenada contendo as instâncias das entidades ou lista vazia se não existir entidade
     *
     * @see #getAll(Class, SingularAttribute, SortOrder)
     */
    public <T> List<T> getAll(Class<T> resultClass, SingularAttribute<?, ?> orderByAttribute, SortOrder sortOrder, boolean lowerInOrderBy);

    /**
     * Busca retornando um único resultado.
     *
     * @param namedQuery
     *            nome da {@link NamedQuery}
     * @param resultClass
     *            classe da entidade
     * @return o resultado ou nulo se não houver resultado
     */
    public <T> T getSingleResult(String namedQuery, Class<T> resultClass);

    /**
     * Busca retornando um único resultado.
     *
     * @param namedQuery
     *            nome da {@link NamedQuery}
     * @param params
     *            parâmetros/condições
     * @param resultClass
     *            classe da entidade
     * @return o resultado ou nulo se não houver resultado
     *
     * @see #getSingleResult(String, Class, Map)
     */
    public <T> T getSingleResult(String namedQuery, Map<SingularAttribute<?, ?>, Object> params, Class<T> resultClass);

    /**
     * Retorna um único resultado.
     *
     * @param namedQuery
     *            nome da {@link NamedQuery}
     * @param resultClass
     *            classe da entidade
     * @param params
     *            parâmetros/condições
     * @return o resultado ou nulo se não houver resultado
     *
     * @see #getSingleResult(String, Map, Class)
     */
    public <T> T getSingleResult(String namedQuery, Class<T> resultClass, Map<String, Object> params);

    /**
     * Busca retornando um único resultado.
     * <p>
     * Aplica LIMIT 1 na consulta por meio do query.setMaxResults(1). Para casos especiais, geralmente em razão de performance.
     * </p>
     *
     * @param namedQuery
     *            nome da {@link NamedQuery}
     * @param params
     *            parâmetros/condições
     * @param resultClass
     *            classe da entidade
     * @return o resultado ou nulo se não houver resultado
     *
     * @see #getSingleResult(String, Map, Class)
     */
    public <T> T getSingleResultLimitTo1(String namedQuery, Map<SingularAttribute<?, ?>, Object> params, Class<T> resultClass);

    /**
     * Busca retornando um único resultado.
     * <p>
     * Aplica LIMIT 1 na consulta por meio do query.setMaxResults(1). Para casos especiais, geralmente em razão de performance.
     * </p>
     *
     * @param namedQuery
     *            nome da {@link NamedQuery}
     * @param resultClass
     *            classe da entidade
     * @param params
     *            parâmetros/condições
     * @return o resultado ou nulo se não houver resultado
     *
     * @see #getSingleResult(String, Class, Map)
     */
    public <T> T getSingleResultLimitTo1(String namedQuery, Class<T> resultClass, Map<String, Object> params);

    /**
     * Busca, retornando um único resultado, por meio de um atributo da entidade.
     * <p>
     * Lógica: FROM resultClass e WHERE e.attribute = :value
     * </p>
     *
     * @param attributeToSearch
     *            atributo da entidade pelo qual será pesquisado
     * @param value
     *            valor do atributo
     * @param resultClass
     *            classe da entidade
     *
     * @return o resultado ou nulo se não houver resultado
     *
     * @see #getSingleResultByDirectAttributes(Map, Class)
     */
    public <T> T getSingleResultByDirectAttribute(SingularAttribute<?, ?> attributeToSearch, Object value, Class<T> resultClass);

    /**
     * Busca, retornando um único resultado, por meio de um atributo da entidade.
     * <p>
     * Lógica: FROM resultClass e WHERE e.attribute = :value
     * </p>
     *
     * @param attributeToSearch
     *            atributo da entidade pelo qual será pesquisado
     * @param value
     *            valor do atributo
     * @param resultClass
     *            classe da entidade
     * @param lowerInSearch
     *            se {@code true} aplica um SQL LOWER no WHERE pra se tornar case-insensitive
     *
     * @return o resultado ou nulo se não houver resultado
     *
     * @see #getSingleResultByDirectAttributes(Map, Class)
     */
    public <T> T getSingleResultByDirectAttribute(SingularAttribute<?, ?> attributeToSearch, Object value, Class<T> resultClass, boolean lowerInSearch);

    /**
     * Busca, retornando um único resultado, por meio de atributos da entidade.
     * <p>
     * Lógica: FROM resultClass e WHERE e.attribute1 = :value1 AND ... AND e.attributeN = :valueN
     * </p>
     *
     * @param params
     *            mapa contendo os atributos da entidades e seus respectivos valores para servir de filtro (são as condições de pesquisa)
     *
     * @param resultClass
     *            classe da entidade
     *
     * @return o resultado ou nulo se não houver resultado
     *
     * @see #getSingleResultByDirectAttribute(SingularAttribute, Object, Class)
     */
    public <T> T getSingleResultByDirectAttributes(Map<SingularAttribute<?, ?>, Object> params, Class<T> resultClass);

    /**
     * Checa se um registro existe por meio de um atributo da entidade.
     * <p>
     * Case-sensitive.
     * </p>
     *
     * @param entityClass
     *            classe da entidade
     * @param attributeToSearch
     *            atributo da entidade pelo qual será pesquisado
     * @param value
     *            valor do atributo
     *
     * @return {@code true} se existir, {@code false} caso contrário
     *
     * @see #exist(Class, SingularAttribute, Object, boolean)
     */
    public <E> boolean exist(Class<E> entityClass, SingularAttribute<?, ?> attributeToSearch, Object value);

    /**
     * Checa se um registro existe por meio de atributos da entidade.
     * <p>
     * Case-sensitive.
     * </p>
     *
     * @param entityClass
     *            classe da entidade
     * @param attributesToSearch
     *            mapa contendo os atributos da entidades e seus respectivos valores pelo quais será pesquisado
     *
     * @return {@code true} se existir, {@code false} caso contrário
     */
    public <E> boolean exist(Class<E> entityClass, Map<SingularAttribute<?, ?>, Object> attributesToSearch);

    /**
     * Checa se um registro existe por meio de um atributo da entidade.
     *
     * @param entityClass
     *            classe da entidade
     * @param attributeToSearch
     *            atributo da entidade pelo qual será pesquisado
     * @param value
     *            valor do atributo
     * @param lowerOrTruncInSearch
     *            se {@code true} aplica um SQL LOWER ou TRUNC no WHERE, LOWER para derivados de CharSequence e TRUNC para Date
     *
     * @return {@code true} se existir, {@code false} caso contrário
     *
     * @see #exist(Class, SingularAttribute, Object)
     */
    public <E> boolean exist(Class<E> entityClass, SingularAttribute<?, ?> attributeToSearch, Object value, boolean lowerOrTruncInSearch);

    /**
     * Busca um atributo específico de uma entidade.
     * <p>
     * Lógica: SELECT e.attributeName FROM entityClass e
     * </p>
     *
     * @param entityClass
     *            classe da entidade
     * @param attributeToReturn
     *            atributo de retorno
     * @return lista contendo as instâncias de {@code attributeToReturn} ou lista vazia se não existir resultado
     */
    public <E, T> List<T> getAttributes(Class<E> entityClass, SingularAttribute<?, T> attributeToReturn);

    /**
     * Busca um atributo específico de uma entidade.
     *
     * @param entityClass
     *            classe da entidade
     * @param attributeToReturn
     *            atributo de retorno
     * @param orderByAttribute
     *            atributo que dita a ordem
     *
     * @return lista contendo as instâncias de {@code attributeToReturn} ou lista vazia se não existir resultado
     */
    public <E, T> List<T> getAttributes(Class<E> entityClass, SingularAttribute<?, T> attributeToReturn, SingularAttribute<?, ?> orderByAttribute);

    /**
     * Busca um atributo específico de uma entidade.
     *
     * @param entityClass
     *            classe da entidade
     * @param attributeToReturn
     *            atributo de retorno
     * @param orderByAttribute
     *            atributo que dita a ordem
     * @param sortOrder
     *            tipo de ordenação conforme {@link SortOrder}
     * @param lowerInOrderBy
     *            se {@code true} aplica um SQL LOWER no order by
     *
     * @return lista contendo as instâncias de {@code attributeToReturn} ou lista vazia se não existir resultado
     */
    public <E, T> List<T> getAttributes(Class<E> entityClass, SingularAttribute<?, T> attributeToReturn, SingularAttribute<?, ?> orderByAttribute, SortOrder sortOrder, boolean lowerInOrderBy);

    /**
     * Busca um atributo específico de uma entidade. Retorna uma lista de resultados.
     * <p>
     * Lógica: SELECT e.attributeToReturn FROM entityClass e WHERE e.attributeToSearch = :valueToSearch
     * </p>
     *
     * @param entityClass
     *            classe da entidade
     * @param attributeToReturn
     *            atributo de retorno
     * @param attributeToSearch
     *            atributo usado para localizar
     * @param valueToSearch
     *            valor usado para localizar
     * @return lista contendo as instâncias de {@code attributeToReturn} ou lista vazia se não existir resultado
     *
     * @see #getAttribute(Class, SingularAttribute, SingularAttribute, Object)
     */
    public <E, T> List<T> getAttributes(Class<E> entityClass, Attribute<?, T> attributeToReturn, SingularAttribute<?, ?> attributeToSearch, Object valueToSearch);

    /**
     * Busca um atributo específico de uma entidade. Retorna um único resultado.
     * <p>
     * Lógica: SELECT e.attributeToReturn FROM entityClass e WHERE e.attributeToSearch = :valueToSearch
     * </p>
     *
     * @param entityClass
     *            classe da entidade
     * @param attributeToReturn
     *            atributo de retorno
     * @param attributeToSearch
     *            atributo usado para localizar
     * @param valueToSearch
     *            valor usado para localizar
     * @return o resultado ou nulo se não houver resultado
     *
     * @see #getAttributes(Class, SingularAttribute, SingularAttribute, Object)
     */
    public <E, T> T getAttribute(Class<E> entityClass, Attribute<?, T> attributeToReturn, SingularAttribute<?, ?> attributeToSearch, Object valueToSearch);

    /**
     * Retorna um mapa onde a chave é o atributo {@code keyToReturn} e o valor é o atributo {@code valueToReturn}.
     *
     * @param entityClass
     *            classe da entidade
     * @param keyToReturn
     *            chave do mapa
     * @param valueToReturn
     *            valor do mapa
     *
     * @return um mapa contendo o resultado
     */
    public <E, K, T> Map<K, T> getAttributesMap(Class<E> entityClass, SingularAttribute<?, K> keyToReturn, SingularAttribute<?, T> valueToReturn);

    /**
     * Busca de acordo com a named query {@code namedQuery}.
     *
     * @param namedQuery
     *            nome da {@link NamedQuery}
     * @param resultClass
     *            classe da entidade
     * @return lista contendo as instâncias das entidades ou lista vazia se não existir entidade
     */
    public <T> List<T> get(String namedQuery, Class<T> resultClass);

    /**
     * Busca de acordo com a named query {@code namedQuery}.
     *
     * @param namedQuery
     *            nome da {@link NamedQuery}
     * @param params
     *            parâmetros/condições
     * @param resultClass
     *            classe da entidade
     * @return lista contendo as instâncias das entidades ou lista vazia se não existir entidade
     *
     * @see #get(String, Class, Map)
     */
    public <T> List<T> get(String namedQuery, Map<SingularAttribute<?, ?>, Object> params, Class<T> resultClass);

    /**
     * Busca de acordo com a named query {@code namedQuery}.
     *
     * @param namedQuery
     *            nome da {@link NamedQuery}
     * @param resultClass
     *            classe da entidade
     * @param params
     *            parâmetros/condições
     *
     * @return lista contendo as instâncias das entidades ou lista vazia se não existir entidade
     *
     * @see #get(String, Map, Class)
     */
    public <T> List<T> get(String namedQuery, Class<T> resultClass, Map<String, Object> params);

    /**
     * Busca de acordo com a named query {@code namedQuery}.
     *
     * @param namedQuery
     *            nome da {@link NamedQuery}
     * @param resultClass
     *            classe da entidade
     * @param params
     *            parâmetros/condições
     * @param maxResult
     *            número máximo de resultados para recuperar
     *
     * @return lista contendo as instâncias das entidades ou lista vazia se não existir entidade
     *
     * @see #get(String, Map, Class)
     */
    public <T> List<T> get(String namedQuery, Class<T> resultClass, Map<String, Object> params, int maxResult);

    /**
     * Itera todas as entidades e para cada registro executa {@code process}.
     *
     * @param targetClass
     *            classe da entidade
     * @param process
     *            instância de {@link Process} com a lógica de negócio para ser executada para cada registro
     *
     * @see #processAllSave(Class, Process)
     */
    public <T> void processAll(Class<T> targetClass, Consumer<T> process);

    /**
     * Itera todas as entidades e para cada registro executa {@code process} <b>e salva o registro</b>.
     *
     * @param targetClass
     *            classe da entidade
     * @param process
     *            instância de {@link Process} com a lógica de negócio para ser executada para cada registro
     *
     * @see #processAllSave(Class, Process)
     */
    public <T> void processAllSave(Class<T> targetClass, Consumer<T> process);

    /**
     * Itera de acordo com a named query {@code namedQuery} e para cada registro executa {@code process}.
     *
     * @param namedQuery
     *            nome da {@link NamedQuery}
     * @param params
     *            parâmetros/condições
     * @param process
     *            instância de {@link Process} com a lógica de negócio para ser executada para cada registro
     *
     * @see #processSave(String, Map, Process)
     */
    public <T> void process(String namedQuery, Map<String, Object> params, Consumer<T> process);

    /**
     * Itera de acordo com a named query {@code namedQuery} e para cada registro executa {@code process} <b>e salva o registro</b>.
     *
     * @param namedQuery
     *            nome da {@link NamedQuery}
     * @param params
     *            parâmetros/condições
     * @param process
     *            instância de {@link Process} com a lógica de negócio para ser executada para cada registro
     *
     * @see #process(String, Map, Process)
     */
    public <T> void processSave(String namedQuery, Map<String, Object> params, Consumer<T> process);

    /**
     * Busca de acordo com a query nativa {@code nativeQuery} usando um resultSetMapping.
     *
     * @param nativeQuery
     *
     * @param resultSetMapping
     *
     * @return lista contendo as instâncias das entidades ou lista vazia se não existir entidade
     */
    public <T> List<T> getByNativeQuery(String nativeQuery, String resultSetMapping);

    /**
     * Busca paginada com controle de paginação.
     *
     * @param resultClass
     *            classe da entidade
     * @param page
     *            página
     * @param pageSize
     *            quantidade de registros por página
     * @return um {@link Pair} contendo o resultado e o controle da paginação
     */
    public <T> Pair<List<T>, PaginationScrollStatus> getPaged(Class<T> resultClass, int page, int pageSize);

    /**
     * Busca paginada com controle de paginação fazendo ordenação.
     *
     * @param resultClass
     *            classe da entidade
     * @param orderByAttributes
     *            atributos que ditam a ordem
     * @param page
     *            página
     * @param pageSize
     *            quantidade de registros por página
     * @return um {@link Pair} contendo o resultado ordenado e o controle da paginação
     */
    public <T> Pair<List<T>, PaginationScrollStatus> getPaged(Class<T> resultClass, Map<SingularAttribute<?, ?>, SortOrder> orderByAttributes, int page, int pageSize);

    /**
     * Busca paginada com controle de paginação fazendo ordenação.
     *
     * @param resultClass
     *            classe da entidade
     * @param orderByAttributes
     *            atributos que ditam a ordem
     * @param nullsFirst
     *            se {@code true} nulos primeiros
     * @param page
     *            página
     * @param pageSize
     *            quantidade de registros por página
     * @return um {@link Pair} contendo o resultado ordenado e o controle da paginação
     */
    public <T> Pair<List<T>, PaginationScrollStatus> getPaged(Class<T> resultClass, Map<SingularAttribute<?, ?>, SortOrder> orderByAttributes, boolean nullsFirst, int page, int pageSize);

    /**
     * Busca paginada com controle de paginação fazendo ordenação.
     *
     * @param resultClass
     *            classe da entidade
     * @param params
     *            parâmetros/condições
     * @param orderByAttributes
     *            atributos que ditam a ordem
     * @param nullsFirst
     *            se {@code true} nulos primeiros
     * @param page
     *            página
     * @param pageSize
     *            quantidade de registros por página
     * @return um {@link Pair} contendo o resultado ordenado e o controle da paginação
     */
    public <T> Pair<List<T>, PaginationScrollStatus> getPaged(Class<T> resultClass, Map<SingularAttribute<?, ?>, Object> params, Map<SingularAttribute<?, ?>, SortOrder> orderByAttributes,
            boolean nullsFirst, int page, int pageSize);

    /**
     * Busca paginada com controle de paginação.
     *
     * @param resultClass
     *            tipo do retorno
     * @param namedQuery
     *            NamedQuery utilizada pela consulta
     * @param params
     *            parâmetros/condições
     * @param page
     *            página
     * @param pageSize
     *            quantidade de registros por página
     *
     * @return um {@link Pair} contendo o resultado ordenado e o controle da paginação
     */
    public <T> Pair<List<T>, PaginationScrollStatus> getPaged(Class<T> resultClass, String namedQuery, Map<String, Object> params, int page, int pageSize);

//    /**
//     * Busca paginada com controle de paginação.
//     *
//     * @param search
//     *            objeto que contém as diretivas para realizar a pesquisa
//     * @param page
//     *            página
//     * @param pageSize
//     *            quantidade de registros por página
//     *
//     * @return um {@link Pair} contendo o resultado e o controle da paginação
//     */
//    public <T, S extends SearchAbstract<T>> Pair<List<T>, PaginationScrollStatus> getPaged(S search, int page, int pageSize);

    /**
     * Deleta todos os registros no banco de dados referentes a entidade {@code entityClass}.
     *
     * @param entityClass
     *            classe da entidade
     * @return quantidade de registros deletados
     */
    public <T> int deleteAll(Class<T> entityClass);

    /**
     * Deleta todos os registros no banco de dados referentes a entidade {@code entityClass} que coincidam com {@code attribute} = {@code value}.
     *
     * @param attributeToSearch
     *            atributo da entidade pelo qual será pesquisado
     * @param value
     *            valor do atributo
     * @param entityClass
     *            classe da entidade
     * @return quantidade de registros deletados
     */
    public <T> int deleteByDirectAttribute(Class<T> entityClass, SingularAttribute<?, ?> attributeToSearch, Object value);

    /**
     * Deleta uma entidade pelo {@code id}.
     *
     * @param id
     *            chave primária
     * @param entityClass
     *            classe da entidade
     *
     * @throws EntityNotFoundException
     *             if the entity not exists
     */
    public <T> void deleteById(Serializable id, Class<T> entityClass);

    /**
     * Executa uma atualização (update) ou deleção (delete).
     *
     * @param namedQuery
     *            nome da {@link NamedQuery}
     * @param params
     *            parâmetros/condições
     *
     * @return a quantidade de entidades atualizadas ou deletadas
     */
    public int executeUpdate(String namedQuery, Map<String, Object> params);

    /**
     * Persiste a entidade. O método decide se realiza um {@link EntityManager#persist(Object)} ou {@link EntityManager#merge(Object)}.
     *
     * @param entity
     *            instância da entidade
     *
     * @return entidade persistida
     */
    public <T> T persist(T entity);

    /**
     * Persiste uma coleção de entidades. O método decide se realiza um {@link EntityManager#persist(Object)} ou {@link EntityManager#merge(Object)}.
     *
     * @param entities
     *            instâncias da entidades
     */
    public <T> void persist(Collection<T> entities);

    /**
     * Similar ao {@link EntityManager#refresh(Object)}, mas se a entidade estiver desatachada um {@link EntityManager#merge(Object)} será feito antes do refresh.
     *
     * @param entity
     *            instância da entidade
     * @return instância da entidade
     */
    public <T> T refresh(T entity);

}
