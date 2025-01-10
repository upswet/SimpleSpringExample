package ru.app.db.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;
import ru.app.db.entity.enums.ContactTypeEnum;

/**Контакт*/
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PUBLIC)
@SQLRestriction("deleted = false")
@SQLDelete(sql = "update Contact_Entity set deleted=true where id=?") //имя таблицы приходится задавать как константу
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactEntity extends AbstractEntity {
    /**тип контакта*/
    @Enumerated(EnumType.STRING)
    ContactTypeEnum type;

    /**сам контакт*/
    String contact;

    /**поле обратной связи*/
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    //@JsonBackReference  https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion
    @JsonIgnore
    WorkerEntity worker;
}
