package ir.maktab.homeservicecompany.models.category.service;

import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.utils.base.service.BaseService;

import java.util.Optional;

public interface CategoryService extends BaseService<Category> {
    Optional<Category> findByName(String name);

    Category addNewCategory(String name);
}