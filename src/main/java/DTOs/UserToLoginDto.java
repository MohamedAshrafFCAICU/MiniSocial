package DTOs;

import jakarta.validation.constraints.*;
import java.io.Serializable;

public class UserToLoginDto implements Serializable
{
    @Email(message = "Invalid Email Format.")
    @NotNull(message = "Email Must Have a Value.")
    private String email;

    @NotNull(message = "Password Must Have a Value.")
    private String password;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
