package RepositoriesContract;

import Entities.Authentication;

public interface IAuthenticationRepository extends _IGenericRepository<Authentication>
{
    public Authentication getAuthenticationByToken(String token);
}
