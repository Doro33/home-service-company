package ir.maktab.homeservicecompany.utils.base.service;

import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;
import ir.maktab.homeservicecompany.utils.exception.InvalidIdException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class BaseServiceImpl<E extends BaseEntity, R extends JpaRepository<E, Long>> implements BaseService<E> {
    protected final R repository;

    public BaseServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public E saveOrUpdate(E e) {
        return repository.save(e);
    }

    @Override
    public void delete(E e) {
        repository.delete(e);
    }

    @Override
    public E findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new InvalidIdException("This id is not valid."));
    }

    @Override
    public List<E> findAll() {
        return repository.findAll();
    }
}