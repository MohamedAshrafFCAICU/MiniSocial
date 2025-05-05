package Repositories;

import Entities._BaseEntity;
import RepositoriesContract._IGenericRepository;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class _GenericRepository<T extends _BaseEntity> implements _IGenericRepository<T>
{
    @PersistenceContext(unitName = "MiniSocial")
    protected EntityManager entityManager;

    private Class<T> entityClassType;

    protected _GenericRepository(Class<T> entityClassType)
    {
        this.entityClassType = entityClassType;
    }

    public _GenericRepository() {
        // No-args constructor for CDI
    }

    @Override
    public List<T> getAll()
    {
       TypedQuery<T> objects =   entityManager.createQuery("SELECT e FROM " + entityClassType.getSimpleName() + " e", entityClassType);
       List<T> list = objects.getResultList();

       return list.isEmpty() ? null : list;


    }

    @Override
    public T getById(int id)
    {
        T obj = entityManager.find(entityClassType, id);
        return obj == null? null : obj;
    }

    @Override
    public void add(T entity)
    {
        if(entity != null)
            entityManager.persist(entity);
    }

    @Override
    public void update(T entity)
    {
        if(entity != null)
            entityManager.merge(entity);
    }

    @Override
    public void delete(int id) {
        T entity = entityManager.find(entityClassType, id);
        if(entity != null)
        {
            entityManager.remove(entity);
        }

    }
}
