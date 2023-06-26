package net.pmarques.user_authreg_restapi.dto;
import lombok.Data;
@Data
public class UserDto {
private String usernameOrEmail;
private String password;
}