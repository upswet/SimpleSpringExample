package ru.auth.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients", indexes = {
        @Index(name = "clientId_audience", columnList = "clientId, audience", unique = true)
})
/**Сущность для хранения польователей*/
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**УИД-клиента или его псевдоним*/
    private String clientId;

    /**Его роли в рамках данной audience через запятую*/
    private String roles;

    /**Система для которой клиенту предоставлен доступ под нужной ролью*/
    private String audience;

    /**Хэш его пароля*/
    private String hash;
}
