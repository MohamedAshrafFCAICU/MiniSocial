package Repositories;

import Entities._BaseEntity;
import RepositoriesContract._IGenericRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class _GenericRepository<T extends _BaseEntity> implements _IGenericRepository<T>
{
    @PersistenceContext(unitName = "MiniSocial")
    protected EntityManager entityManager;

    private Class<T> entityClassType;

    protected _GenericRepository(Class<T> entityClassType)
    {
        this.entityClassType = entityClassType;
    }



    @Override
    public List<T> getAll()
    {
        return entityManager.createQuery("SELECT e FROM " + entityClassType.getSimpleName() + " e", entityClassType)
                .getResultList();
    }

    @Override
    public T getById(int id)
    {
        return entityManager.find(entityClassType, id);
    }

    @Override
    public void Add(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void Update(T entity) {
        entityManager.merge(entity);
    }

    @Override
    public void Delete(int id) {
        T entity = entityManager.find(entityClassType, id);
        if(entity != null)
        {
            entityManager.remove(entity);
        }

    }
}
