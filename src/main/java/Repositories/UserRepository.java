package Repositories;

import Entities.User;
import RepositoriesContract.IUserRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;


@Stateless
public class UserRepository extends _GenericRepository<User> implements IUserRepository
{
    public UserRepository()
    {
        super(User.class);
    }

    public User getUserByEmail(String email) {
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

    @Override
    public void addFriend(int friendId1, int friendId2) {

        Query query1 = entityManager.createNativeQuery("INSERT INTO friends (user_id, friend_id) VALUES (?, ?)", User.class);
        query1.setParameter(1, friendId1);
        query1.setParameter(2, friendId2);
        query1.executeUpdate();

        Query query2 = entityManager.createNativeQuery("INSERT INTO friends (user_id, friend_id) VALUES (?, ?)", User.class);
        query2.setParameter(1, friendId2);
        query2.setParameter(2, friendId1);
        query2.executeUpdate();


    }


}
