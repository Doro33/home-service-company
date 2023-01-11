package ir.maktab.homeservicecompany.utils.base.service;

import ir.maktab.homeservicecompany.utils.base.entity.BaseEntity;

import java.util.List;

public interface BaseService<E extends BaseEntity> {
    E saveOrUpdate(E e);

    void delete(E e);

    E findById(Long id);

    List<E> findAll();
}