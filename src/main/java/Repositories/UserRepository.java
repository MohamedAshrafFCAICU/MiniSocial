package Repositories;

import Entities.User;
import RepositoriesContract.IUserRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import java.util.List;


@Stateless
public class UserRepository extends _GenericRepository<User> implements IUserRepository
{
    public UserRepository()
    {
        super(User.class);
    }

    public User getUserByEmail(String email)
    {
        TypedQuery<User> user = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        user.setParameter("email", email);

        List<User> results = user.getResultList();
        return results.isEmpty() ? null: results.get(0);
    }

    @Override
    public User getUserByPhone(String phone) {
        TypedQuery<User> user = entityManager.createQuery("SELECT u FROM User u WHERE u.phoneNumber = :phone", User.class);
        user.setParameter("phone", phone);

        List<User> results = user.getResultList();
        return results.isEmpty() ? null: results.get(0);
    }


}
