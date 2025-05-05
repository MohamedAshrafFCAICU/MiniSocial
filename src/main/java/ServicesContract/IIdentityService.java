package ServicesContract;

import DTOs.UserToLoginDto;
import DTOs.UserToRegisterDto;
import DTOs.UserToUpdateProfileDto;

public interface IIdentityService
{
    public String register(UserToRegisterDto userDto);

    public String login(UserToLoginDto userDto);

    public String logout(String token);

    public String updateProfile(String token, UserToUpdateProfileDto userDto);

}
