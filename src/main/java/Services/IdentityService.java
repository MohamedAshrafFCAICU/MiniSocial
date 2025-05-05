package Services;

import CustomizedExceptions.ClientException;
import CustomizedExceptions.InternalServerException;
import DTOs.UserToLoginDto;
import DTOs.UserToRegisterDto;
import DTOs.UserToUpdateProfileDto;
import Entities.Authentication;
import Entities.User;
import Enums.Gender;
import Enums.Role;
import RepositoriesContract.IAuthenticationRepository;
import RepositoriesContract.IUserRepository;
import ServicesContract.IIdentityService;
import jakarta.ejb.*;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class IdentityService implements IIdentityService
{

    @EJB
    private IUserRepository userRepository;

    @EJB
    private IAuthenticationRepository authenticationRepository;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String register(UserToRegisterDto userDto)
    {
        // To Register We Follow These Business Rules:
        /*
         * 1. Validate The Dto Got From The Request Body (Done Using JPA Annotations)
         * 2. Check if Email and Phone Number do not Exist Before
         * 3. Hash The Password
         * 4. Mapping From UserDto -> User
         * 5. Add The User in The Persistence Context
         */

        try
        {
            if(userRepository.getUserByEmail(userDto.getEmail()) != null)
                throw new ClientException("The Email Already Exists.");

            if(userRepository.getUserByPhone(userDto.getPhoneNumber()) != null)
                throw new ClientException("The Phone Number Already Exists.");

            String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(12));

            User user = new User();
            user.setfName(userDto.getfName());
            user.setlName(userDto.getlName());
            user.setEmail(userDto.getEmail());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setHashedPassword(hashedPassword);
            user.setBio(userDto.getBio());
            user.setAge(userDto.getAge());
            user.setRole(userDto.getRole().toLowerCase().equals("user")? Role.USER : Role.ADMIN);
            user.setGender(userDto.getGender().toLowerCase().equals("male")? Gender.MALE : Gender.FEMALE);

            userRepository.add(user);
            return "User Registered Successfully";
        }
        catch (Exception e)
        {
            throw new InternalServerException("Error during register a user" , e);
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String login(UserToLoginDto userDto)
    {
        // To Login We Follow These Business Rules:
        /*
         * 1. Validate The Dto Got From The Request Body
         * 2. Check if Email with Hashed Password are Matching
         * 3. Generate Token(UUID) and Update User
         * 4. Return The Token
         */

        try
        {
            User user = userRepository.getUserByEmail(userDto.getEmail());
            if(user == null)
                throw new ClientException("Invalid Login.");

            boolean isMatched =  BCrypt.checkpw(userDto.getPassword(), user.getHashedPassword());
            if(!isMatched)
                throw new ClientException("Invalid Login.");


            Authentication authentication = user.getAuthentication();
            if(authentication != null)
            {
                user.setAuthentication(null);
                authenticationRepository.delete(authentication.getId());
            }

            user.setAuthentication(new Authentication());
            userRepository.update(user);

            return user.getAuthentication().getToken();
        }
        catch (Exception e)
        {
            throw new InternalServerException("Error during login a user" , e);
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String logout(String token)
    {

        try
        {
            User user = _AuthenticationService.authenticate(token,  authenticationRepository);

            Authentication authentication = user.getAuthentication();
            user.setAuthentication(null);
            user.setActive(false);
            userRepository.update(user);
            authenticationRepository.delete(authentication.getId());

            return "User Logout Successfully";
        }
        catch (Exception e)
        {
            throw new InternalServerException("Error during logout a user" , e);
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String updateProfile(String token, UserToUpdateProfileDto userDto)
    {

        try
        {
            User user = _AuthenticationService.authenticate(token , authenticationRepository);


            if(userDto.getEmail() != null && !userDto.getEmail().isBlank())
            {
                User userWithSameEmail = userRepository.getUserByEmail(userDto.getEmail());
                if(userWithSameEmail != null && userWithSameEmail.getId() != user.getId())
                    throw new ClientException("The Email Already Exists.");
            }

            if(userDto.getPhoneNumber() != null && !userDto.getPhoneNumber().isBlank())
            {
                User userWithSamePhoneNumber = userRepository.getUserByPhone(userDto.getPhoneNumber());
                if(userWithSamePhoneNumber != null && userWithSamePhoneNumber.getId() != user.getId())
                    throw new ClientException("The Phone Number Already Exists.");
            }



            user.setfName(userDto.getfName() == null  || userDto.getfName().isBlank()  ? user.getfName() : userDto.getfName());
            user.setlName(userDto.getlName() == null || userDto.getlName().isBlank()? user.getlName() : userDto.getlName());
            user.setEmail(userDto.getEmail() == null || userDto.getEmail().isBlank() ? user.getEmail() : userDto.getEmail());
            user.setHashedPassword(userDto.getPassword() == null || userDto.getPassword().isBlank() ? user.getHashedPassword() : BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt(12)));
            user.setBio(userDto.getBio() == null || userDto.getBio().isBlank() ? user.getBio() : userDto.getBio());
            user.setAge(userDto.getAge() == 0 ? user.getAge() : userDto.getAge());
            user.setPhoneNumber(userDto.getPhoneNumber() == null || userDto.getPhoneNumber().isBlank() ? user.getPhoneNumber() : userDto.getPhoneNumber());

            userRepository.update(user);

            return "Profile Updated Successfully";

        }
        catch (Exception e)
        {
            throw new InternalServerException("Error during update profile for a user" , e);
        }


    }
}
