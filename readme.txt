Общий градле-проект
https://www.book2s.com/tutorials/gradle-dependency-management.html

SpringCloudGateway
Гейтей связывающий AuthService и экземпляры WebApplication
https://tproger.ru/articles/pishem-java-veb-prilozhenie-na-sovremennom-steke-s-nulja-do-mikroservisnoj-arhitektury-chast-3
https://sysout.ru/spring-cloud-api-gateway/?ysclid=m5ergtfz7887528745
https://www.concretepage.com/spring-boot/spring-cloud-gateway
https://cloud.spring.io/spring-cloud-gateway/reference/html/
    + actuator
        https://www.concretepage.com/spring-boot/spring-boot-actuator-endpoints
        https://www.book2s.com/tutorials/spring-boot-actuator.html
        https://habr.com/ru/companies/otus/articles/650871/

AuthService (OAuth)
Сервер авторизации.
Client Credentials Flow - Этот поток полезен для систем, которые должны выполнять операции API, когда пользователя нет. Например, ночные операции или другие, которые предполагают обращение к защищенным API OAuth
    https://tproger.ru/articles/pishem-java-veb-prilozhenie-na-sovremennom-steke-s-nulja-do-mikroservisnoj-arhitektury-chast-2
https://jwt.io/
OAuth2
    https://struchkov.dev/blog/ru/jwt-implementation-in-spring/
    https://habr.com/ru/articles/784508/

WebApplication
Пример приложения работающего с БД и предоставляющего REST API + swagger.
Добавлены разные мелкие красивости вроде примера перехватчика запросов/ответов или общего обработчика исключений, а также пример настраиваемого мягкого/жёсткого удаления
Добавлена возможность работы через авторизацию посредством AuthService
https://struchkov.dev/blog/ru/api-swagger/
https://tproger.ru/articles/pishem-java-veb-prilozhenie-na-sovremennom-steke-s-nulja-do-mikroservisnoj-arhitektury-chast-1
https://tproger.ru/articles/pishem-java-veb-prilozhenie-na-sovremennom-steke-s-nulja-do-mikroservisnoj-arhitektury-chast-2


WebSecurityApplication
//https://habr.com/ru/articles/482552/
//https://habr.com/ru/articles/784508/
Немного информации о Spring Security
Самым фундаментальным объектом является SecurityContextHolder. В нем хранится информация о текущем контексте безопасности приложения, который включает в себя подробную информацию о пользователе (принципале), работающим с приложением.
Spring Security использует объект Authentication, пользователя авторизованной сессии.
«Пользователь» – это просто Object. В большинстве случаев он может быть
приведен к классу UserDetails. UserDetails можно представить, как адаптер между БД пользователей и тем что требуется Spring Security внутри SecurityContextHolder.
Для создания UserDetails используется интерфейс UserDetailsService, с единственным методом:
UserDetails loadUserByUsername(String username) throws UsernameNotFoundException

WebGenerateApplication
Пример автогенерации веб-сервисов из ямл-файлов
https://habr.com/ru/companies/spring_aio/articles/833096/
https://openvalue.blog/posts/2023/11/26/communicating_our_apis_part2/
https://openapi-generator.tech/docs/generators/#server-generators

SpringCloudConfig
Конфиг сервер - раздаёт конфиги другим приложениям по запросу храня их либо локально, либо в гит-е
https://sysout.ru/spring-cloud-configuration-server/
https://www.czetsuyatech.com/2019/10/spring-config-using-git.html
https://docs.spring.io/spring-cloud-config/docs/current/reference/html/
https://www.baeldung.com/spring-cloud-config-without-git

SpringBootAdminServer
Сприг-бут-админ-сервер
https://habr.com/ru/articles/479954/