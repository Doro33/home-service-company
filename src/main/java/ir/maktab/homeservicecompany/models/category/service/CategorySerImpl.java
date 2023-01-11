package ir.maktab.homeservicecompany.models.category.service;

import ir.maktab.homeservicecompany.models.category.dao.CategoryDao;
import ir.maktab.homeservicecompany.models.category.entity.Category;
import ir.maktab.homeservicecompany.utils.base.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategorySerImpl extends BaseServiceImpl<Category, CategoryDao> implements CategoryService {
    public CategorySerImpl(CategoryDao repository) {
        super(repository);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Category addNewCategory(String name) {
        if (findByName(name).isPresent())
            throw new IllegalArgumentException("this category has already been  added.");
        return saveOrUpdate(new Category(name));
    }
}