package Services;

import CustomizedExceptions.UnAuthorizedException;
import Entities.Authentication;
import Entities.User;
import RepositoriesContract.IAuthenticationRepository;

public class _AuthenticationService
{
    public static User authenticate(String token , IAuthenticationRepository authenticationRepository)
    {
        Authentication authentication = authenticationRepository.getAuthenticationByToken(token);
        if (authentication == null)
            throw new UnAuthorizedException("Invalid Token.");

        User user = authentication.getUser();
        if (user == null)
            throw new UnAuthorizedException("Invalid Token.");

        return user;
    }

}
