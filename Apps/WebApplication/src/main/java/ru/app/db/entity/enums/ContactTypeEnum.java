package ru.app.db.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**Тип контакта*/
@Getter
@AllArgsConstructor
public enum ContactTypeEnum {
    MOBILE_PHONE("Мобильный телефон"),
    EMAIL("Электронная почта"),
    SOCIAL_NET("Социальная сеть"),
    MESSAGER("Мессенджер");

    final String local;
}
