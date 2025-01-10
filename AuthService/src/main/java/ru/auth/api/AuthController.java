package ru.auth.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.auth.api.dto.AuthRequestDto;
import ru.auth.api.dto.AuthResponseDto;
import ru.auth.api.dto.ErrorResponseDto;
import ru.auth.db.entity.ClientEntity;
import ru.auth.db.repository.ClientRepository;
import ru.auth.service.TokenService;

import javax.security.auth.login.LoginException;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ClientRepository userRepository;
    private final TokenService tokenService;
    private final Map<String, String> refreshStorage = new HashMap<>(); //хранилище выданных рефреш-токенов. Для дополнительной проверки что мы действительно выдавали данный токен

    /**Сервис только для админов. Зарегистрировать нового клиента
     * @param clientId - клиент
     * @param clientSecret - пароль,
     * @param audience - система на которую даём права клиенту
     * @param roles - права которые даём*/
    @PostMapping("/reg")
    public void register(String clientId, String clientSecret, String audience, String roles) throws LoginException {
        if(userRepository.findByClientIdAndAudience(clientId, audience).isPresent())
            throw new LoginException("Client with id=" + clientId+" audience="+audience + " already registered");

        String hash = BCrypt.hashpw(clientSecret, BCrypt.gensalt());
        userRepository.save(new ClientEntity(null,clientId, roles, audience, hash));
    }

    /**Логин
     * @param user - пользователь для которого хотим получить токены на доступ к заданной системе
     * @return - ацесс и рефреш токены*/
    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto user) throws LoginException{
        checkCredentials(user.getClientId(), user.getClientSecret(), user.getAudience());
        return generateNewAccessAndRefreshTokens(user.getClientId(), user.getAudience());
    }

    /**Обновить асцесс токен по рефреш токену
     * @param refreshToken - рефреш токен
     * @return - новый ацесс токен*/
    @PostMapping("/access")
    public AuthResponseDto getUpdateAccessToken(String refreshToken) throws LoginException{
        if (tokenService.checkRefreshToken(refreshToken)){
            DecodedJWT decodedJWT = tokenService.decodeRefreshToken(refreshToken);
            String clientId=decodedJWT.getSubject();
            String audience=decodedJWT.getAudience().get(0);
            String savingRefreshToken = this.refreshStorage.get(clientId+"-"+audience);
            if (!refreshToken.equals(savingRefreshToken))
                throw new LoginException("Refresh token not found");

            ClientEntity user=userRepository.findByClientIdAndAudience(clientId, audience).get();
            return new AuthResponseDto(tokenService.generateAccessToken(user.getClientId(), user.getAudience(), user.getRoles()), null);
        }
        return new AuthResponseDto(null, null);
    }
    /**Обновить ацесс и рефреш токен по рефреш токену
     * @param refreshToken - рефреш токен
     * @return - ацесс и рефреш токены*/
    @PostMapping("/refresh")
    public AuthResponseDto getUpdateAccessAndRefreshToken(String refreshToken) throws LoginException{
        if (tokenService.checkRefreshToken(refreshToken)){
            DecodedJWT decodedJWT = tokenService.decodeRefreshToken(refreshToken);
            String clientId=decodedJWT.getSubject();
            String audience=decodedJWT.getAudience().get(0);
            String savingRefreshToken = this.refreshStorage.get(clientId+"-"+audience);
            if (!refreshToken.equals(savingRefreshToken))
                throw new LoginException("Refresh token not found");

            return generateNewAccessAndRefreshTokens(clientId, audience);
        }
        return new AuthResponseDto(null, null);
    }

    @ExceptionHandler({LoginException.class})
    public ResponseEntity<ErrorResponseDto> handleUserRegistrationException(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponseDto(ex.getMessage()));
    }

    /**Проверить пароль для данного пользователя и данной системы
     * @param clientId - клиент
     * @param clientSecret - пароль
     * @param audience - система на которую даём права клиенту*/
    private void checkCredentials(String clientId, String clientSecret, String audience) throws LoginException {
        Optional<ClientEntity> optionalUserEntity = userRepository.findByClientIdAndAudience(clientId, audience);
        if (optionalUserEntity.isEmpty())
            throw new LoginException("Client with id=" + clientId+" and audience="+audience + " not found");

        ClientEntity clientEntity = optionalUserEntity.get();
        if (!BCrypt.checkpw(clientSecret, clientEntity.getHash()))
            throw new LoginException("Secret is incorrect");
    }

    /**Сгенерировать новые ацесс и рефреш токены*/
    private AuthResponseDto generateNewAccessAndRefreshTokens(String clientId, String audience) {
        String roles=userRepository.findByClientIdAndAudience(clientId, audience).get().getRoles();
        String access=tokenService.generateAccessToken(clientId, audience, roles);
        String refresh=tokenService.generateRefreshToken(clientId, audience);
        this.refreshStorage.put(clientId+"-"+ audience, refresh);
        return new AuthResponseDto(access, refresh);
    }
}
