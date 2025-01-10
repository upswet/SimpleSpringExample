package ru.app.db.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.app.db.entity.AbstractEntity;

import java.util.List;

/**
 * Сервис работы с сущностями в ДБ
 *
 * https://hibernate-refdoc.3141.ru/ch16.HQL?ysclid=m2lijbbewo31087775
 * https://docs.jboss.org/hibernate/core/3.3/reference/en/html/queryhql.html
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Getter
public class EntityService {
    @PersistenceContext
    private EntityManager em;

    public static EntityService self;

    /**Истина - жёсткое удаления, ложь - мягкое удаление*/
    @Value("${app.delete-is-true:false}")
    private boolean deleteIsTrue;

    @PostConstruct
    public void postConstructor(){
        self=this;
    }

    /**
     * Получить ссылку на сущность по её уид-у (можно исп для проверки существования сущности)
     *
     * @param id          - уид сущности
     * @param entityClass - класс искомой сущности
     * @return - сущность или null, если сущности не существует
     */
    public <A extends AbstractEntity> A getReferenceForId(Class<A> entityClass, Long id){
        /*Hibernate не выполняет SQL-запрос, когда вы вызываете метод getReference.
        Если сущность не является управляемой, Hibernate инстанцирует прокси-объект и инициализирует атрибут первичного ключа

        Это очень похоже на неинициализированную ассоциацию с ленивой загрузкой, которая тоже дает вам прокси-объект.
        В обоих случаях устанавливаются только атрибуты первичного ключа. Как только вы попытаетесь получить доступ к первому же атрибуту, не являющемуся первичным ключем, Hibernate выполнит запрос к базе данных, чтобы получить все атрибуты.
        Это также первый раз, когда Hibernate проверит, существует ли указанный объект сущности. Если выполненный запрос не возвращает ожидаемого результата, Hibernate генерирует исключение
        * */
        return em.getReference(entityClass, id);
    }

    /**
     * Получить сущность по её уид-у
     *
     * @param id          - уид сущности
     * @param entityClass - класс искомой сущности
     * @return - сущность
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <A extends AbstractEntity> A findById(Class<A> entityClass, Long id) {
        try {
            A e = (A) em.createQuery("from " + entityClass.getSimpleName() + " where id=" + id.toString()).getSingleResult();
            return e;
        } catch (Exception ex) {
            log.error("No find entity for id"+id, ex);
            return null;
        }
    }

    /**
     * Получить набор сущностей по условию
     *
     * @param hql           - запрос со всеми условиями
     * @param startPosition - с какой записи получать
     * @param maxResult     - по какую запись получить
     * @param entityClass   - класс искомой сущности
     * @return - сущность
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <A extends AbstractEntity> List<A> findByAllFromConditionals(Class<A> entityClass, String hql, Integer startPosition, Integer maxResult) {
        if (hql.toLowerCase().contains("insert") || hql.toLowerCase().contains("update"))
            throw new RuntimeException("bad sql request: "+hql);

        if (startPosition == null) startPosition = 0;
        if (maxResult == null) maxResult = Integer.MAX_VALUE;
        try {
            log.info(hql);
            List<A> list = em.createQuery("select distinct e "+hql, entityClass).setFirstResult(startPosition).setMaxResults(maxResult).getResultList();
            return list;
        } catch (Exception ex) {
            log.error("Error select for: " + hql, ex);
            throw ex;
        }
    }

    /**
     * Получить количество сущностей в выбранном наборе согласно условиям
     *
     * @param hql - запрос со всеми условиями
     * @return - общее количество выбранных сущностей
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <A extends AbstractEntity> Integer findByAllFromConditionalsCount(String hql) {
        if (hql.toLowerCase().contains("insert") || hql.toLowerCase().contains("update"))
            throw new RuntimeException("bad sql request: "+hql);

        int i=hql.indexOf("order");
        if (i>0)
            hql=hql.substring(0, i);
        try {
            return Integer.parseInt(em.createQuery("select count(distinct e) " + hql).getSingleResult().toString());
        } catch (Exception ex) {
            log.error("Error count select for: " + hql, ex);
            throw ex;
        }
    }


    /**
     * Сохранить сущность в бд
     *
     * @param entity - сохраняемая сущность
     * @return - сохранённую сущность (мб добавить ай-ди или другие поля)
     */
    @SneakyThrows
    @Transactional
    public <S extends AbstractEntity> S save(S entity) {
        entity.beforeSave();
        if (entity.getId() == null) em.persist(entity);
        else entity=em.merge(entity);

        return entity;
    }

    /**Удаляем запись. Удаление или настоящее, или псевдо-удаление в зависимости от настроек*/
    @Transactional
    public void delete(AbstractEntity entity) {
        entity.beforeDelete();

        //em.remove(em.contains(entity) ? entity : em.merge(entity));//delete entity

        if (deleteIsTrue)
            em.remove(em.contains(entity) ? entity : em.merge(entity));//hard delete entity
        else{//soft delete
            entity.setDeleted(true);
            //todo: надо добавить также удаление всех связанных сущностей согласно CascadeType
            /*также для реализации "мягкого" удаления можно использовать аннотации вида
            @SQLDelete(sql = "update Worker_Entity set deleted=true where id=?") но тогда имя таблицы приходится задавать как константу
            @SoftDelete(columnName = "deleted") но тогда нельзя использовать lazy-стратегии загрузки связанных сущностей*/
            save(entity);
        }
    }

    /**Получить класс сущности по её текствому имени посредством поиска во всех сущностях
     * @param entityClassName - имя класса сущности
     * @return - класс сущности*/
    public Class<? extends AbstractEntity> getEntityClassForName(String entityClassName){
        var entityImpl =em.getMetamodel().getEntities().stream().filter(elem -> elem.getName().equals(entityClassName)).findFirst().orElseThrow(()-> new RuntimeException("no find entity class with class name is "+entityClassName));
        return (Class<? extends AbstractEntity>) entityImpl.getJavaType();
    }

    /**
     * Когда мы получаем вложенную сущность через ленивую загрузку, иногда приходится принудительно инициализировать её поля. Новый запрос в БД не идёт
     *
     * @param ae - потомок AbstractEntity, вложенная сущность полученная через ленивую загрузку
     * @return - та же сущность, но с инициализированными полями
     */
    @SuppressWarnings("unchecked")
    public static <R extends AbstractEntity> R lazyInitializer(R ae) {
        if (ae != null)
            if (ae.id == null)
                if (ae.getId() != null)
                    return (R) ((HibernateProxy) ae).getHibernateLazyInitializer().getImplementation(); //если по каким-то причинам поле не было извлечено, то извлечём его принудительно
                else return null;
            else return ae;
        return null;
    }

}
