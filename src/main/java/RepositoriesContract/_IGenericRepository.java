package RepositoriesContract;

import java.util.List;

public interface _IGenericRepository<T>
{
    public List<T> getAll();

    public T getById(int id);

    public void add(T entity);

    public void update(T entity);

    public void delete(int id);
}
