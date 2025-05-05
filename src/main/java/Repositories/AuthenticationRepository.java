package Repositories;

import Entities.Authentication;
import RepositoriesContract.IAuthenticationRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class AuthenticationRepository extends _GenericRepository<Authentication> implements IAuthenticationRepository
{
    public AuthenticationRepository()
    {
        super(Authentication.class);
    }

    @Override
    public Authentication getAuthenticationByToken(String token)
    {
        TypedQuery<Authentication> authentication = entityManager.createQuery("SELECT a FROM Authentication a WHERE a.token = :token", Authentication.class);
        authentication.setParameter("token", token);

        List<Authentication> authentications = authentication.getResultList();
        return authentications.isEmpty() ? null: authentications.get(0);
    }
}
