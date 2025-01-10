package ru.app.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.SoftDelete;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**Сотрудник*/
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PUBLIC)
@Lazy
@SQLRestriction("deleted = false")
@SQLDelete(sql = "update Worker_Entity set deleted=true where id=?") //имя таблицы приходится задавать как константу
//@SoftDelete(columnName = "deleted") нельзя использовать lazy-стратегии загрузки связанных сущностей
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkerEntity extends AbstractEntity {
    /**Имя сотрудника*/
    @JsonIgnore
    String lastName="";
    @JsonIgnore
    String firstName="";

    public String getFullName(){
        return lastName +" " +firstName;
    }

    /**Контакты сотрудника*/
    @OneToMany(mappedBy = "worker", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@JsonManagedReference
    Set<ContactEntity> contacts=new HashSet<>();

    /**Заметки о сотруднике*/
    @ElementCollection
    List<String> notes=new ArrayList<>();
}
