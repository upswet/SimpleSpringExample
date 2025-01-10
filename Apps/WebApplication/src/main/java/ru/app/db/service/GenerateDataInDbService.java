package ru.app.db.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.app.db.entity.AbstractEntity;
import ru.app.db.entity.ContactEntity;
import ru.app.db.entity.WorkerEntity;
import ru.app.db.entity.enums.ContactTypeEnum;

import java.util.*;

/**Сервис генерации тестовых данных в бд*/
@Service
public class GenerateDataInDbService {

    /**Сгенерируем тестовые данные для бд: сотрудников и их контакты*/
    @Transactional
    public void generate(){
        Random randomizer = new Random();
        for(int i=0; i < 10; i++){
            WorkerEntity worker =  new WorkerEntity();
            worker.setLastName(List.of("Иванов","Сидоров","Козлов","Петров","Багров").get(randomizer.nextInt(5)));
            worker.setFirstName(List.of("Иван","Сидор","Илья","Александр","Данила").get(randomizer.nextInt(5)));

            for(int j=0; j < randomizer.nextInt(4); j++){
                ContactEntity contact = new ContactEntity();
                contact.setType(getRandomValueFromEnum(ContactTypeEnum.values()));
                contact.setContact("123456");
                contact.setWorker(worker);
                contact=AbstractEntity.save(contact);
                worker.getContacts().add(contact);
            }

            for(int j=0; j < randomizer.nextInt(4); j++)
                worker.getNotes().add(List.of("Хороший сотрудник","Плохой сотрудник","Вечно опаздывает","Постоянно перерабатывает","Коллег называет братьями и спрашивает 'в чём сила?'").get(randomizer.nextInt(4)));

            worker = AbstractEntity.save(worker);
        }
    }


    /**
     * Выбрать случайный элемент из перечислеия
     */
    private static <V extends Enum<V>> V getRandomValueFromEnum(V[] enumArray) {
        return Arrays.stream(enumArray).toList().get((new Random()).nextInt(enumArray.length));
    }
}
