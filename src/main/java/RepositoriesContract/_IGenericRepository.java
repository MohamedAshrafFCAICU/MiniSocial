package RepositoriesContract;

import java.util.List;

public interface _IGenericRepository<T>
{
    public List<T> getAll();

    public T getById(int id);

    public void Add(T entity);

    public void Update(T entity);

    public void Delete(int id);
}
