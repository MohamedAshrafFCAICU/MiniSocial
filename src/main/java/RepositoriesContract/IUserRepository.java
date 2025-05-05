package RepositoriesContract;

import Entities.User;

public interface IUserRepository extends _IGenericRepository<User>
{
    public User getUserByEmail(String email);

    public User getUserByPhone(String phone);

}
