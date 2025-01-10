package ru.app.api.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import ru.app.config.AppProperties;

import java.util.List;

@Slf4j
public class AuthorizationService {
    /**
     * Проверит, что текущий пользователь имеет указанную роль или поднимет исключение
     * Если код выполнялся от лица системы, то исключения не будет
     */
    public static void checkRole(String roleName) {
        if (!AppProperties.self.getEnabled()){
            log.info("authorization disabled");
            return;
        }

        try {
            String clientId = (String) RequestContextHolder.getRequestAttributes().getAttribute("clientId", 0);
            List<String> roles = (List<String>) RequestContextHolder.getRequestAttributes().getAttribute("roles", 0);
            Boolean result = roles.contains(roleName);
            log.info("clientId={} with roles={} check roleName={} result is {}", clientId, roles, roleName, result);
            if (!result)
                throw new RuntimeException("clientId=" + clientId + " no have role=" + roleName);

        } catch (NullPointerException e) {
            log.info("clientId={} with roles={} check roleName={} result is {}", "system", "[*]", roleName, true);
        }
    }


    /**
     * Проверит, что текущий пользователь имеет все указанные роли или поднимет исключение
     * Если код выполнялся от лица системы, то исключения не будет
     */
    public static void checkRolesAnd(List<String> roles) {
        roles.forEach(roleName -> checkRole(roleName));
    }

    /**
     * Проверит, что текущий пользователь имеет хотя бы одну указанныю роль или поднимет исключение
     * Если код выполнялся от лица системы, то исключения не будет
     */
    public static void checkRolesOr(List<String> roles) {
        boolean result=false;
        for (String roleName : roles)
            try {
                checkRole(roleName);
                result=true;
                break;
            } catch (RuntimeException re) {}
        if (!result)
            throw new RuntimeException("No find role in client for roles=" + roles);

    }
}
