package rsoi.lab2.model;

import java.sql.Timestamp;
import java.util.UUID;

public class UserInfo {

    private Long idUser;

    private String name;

    private String username;

    private String birthDate;

    private String login;

    private String password;

    private UUID uid;

    private UUID token;

    private UUID refreshToken;

    private Timestamp dttmCurrentToken;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public UUID getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(UUID refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Timestamp getDttmCurrentToken() {
        return dttmCurrentToken;
    }

    public void setDttmCurrentToken(Timestamp dttmCurrentToken) {
        this.dttmCurrentToken = dttmCurrentToken;
    }
}
