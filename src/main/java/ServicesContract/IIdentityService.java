package ServicesContract;

import DTOs.UserToLoginDto;
import DTOs.UserToRegisterDto;
import DTOs.UserToReturnDto;
import DTOs.UserToUpdateProfileDto;

public interface IIdentityService
{
    public String register(UserToRegisterDto userDto);

    public String login(UserToLoginDto userDto);

    public String logout(String token);

    public String updateProfile(String token, UserToUpdateProfileDto userDto);

    public UserToReturnDto getUserByEmail(String token , String email);

    public String deleteUser(String token , int userId);

}
