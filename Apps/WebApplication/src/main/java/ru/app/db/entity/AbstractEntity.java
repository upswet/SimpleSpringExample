package ru.app.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;
import org.springframework.core.annotation.AnnotatedElementUtils;
import ru.app.db.service.EntityService;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**Общий абстрактный предок для всех сущностей*/
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC)
@SQLRestriction("deleted = false")
public abstract class AbstractEntity implements Cloneable, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "boolean DEFAULT false")
    @JsonIgnore
    Boolean deleted = false;

    @CreationTimestamp //https://www.baeldung.com/hibernate-creationtimestamp-updatetimestamp
    LocalDateTime createdDt;
    @UpdateTimestamp
    LocalDateTime modifiedDt;

    /**Вызывается перед сохранением. Здесь можно проверять сложные условия перед сохранением или как-то изменять поля*/
    public void beforeSave(){}

    /**Вызывается перед удалением. Здесь можно проверять сложные условия перед удалением и не дать удалить*/
    public void beforeDelete(){}

    /**Сохранить сущность
     * @param entity - сохраняемая сущности
     * @return - сама сущность*/
    static public<A extends AbstractEntity> A save(A entity){
        return EntityService.self.save(entity);
    }

    /**Получить сущность по её уид-у
     * @param id - уид сущности
     * @param entityClass - класс искомой сущности
     * @return - сама сущность или исключение если её не было найдено*/
    static public<A extends AbstractEntity> A findById(Class<A> entityClass, Long id){
        return EntityService.self.findById(entityClass, id);
    }

    /** Удалить сущность
     * @param entity - удаляемая сущность
     **/
    static public <A extends AbstractEntity> void delete(A entity){
        EntityService.self.delete(entity);
    }

    /**Получить набор сущностей по запросу
     * @param query- запрос
     * @param isNativeQuery - истина если нативный скл запрос и ложь если hql-запрос
     * @param entityClass - класс искомой сущности
     * @return - сама сущность или исключение если её не было найдено
     *
     * Примеры запросов
     * "select distinct e from WorkerEntity e where element(e.contacts).contact like '"+"%@%"+"'"
     * */
    static public<A extends AbstractEntity> List<A> findForQuery(String query, Boolean isNativeQuery, Class<A> entityClass){
        List<A> list = isNativeQuery ? EntityService.self.getEm().createNativeQuery(query, entityClass).getResultList() : EntityService.self.getEm().createQuery(query, entityClass).getResultList();
        return list.stream().map(e -> {
            e = EntityService.lazyInitializer(e);
            return e;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getId()==null || ((AbstractEntity) obj).getId()==null) return false;
        if (!this.getClass().equals(obj.getClass())) return  false;
        return ((AbstractEntity) obj).getId().equals(this.getId());
    }
}
