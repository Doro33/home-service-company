package ir.maktab.homeservicecompany.models.category.dao;
import ir.maktab.homeservicecompany.models.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryDao extends JpaRepository<Category,Long> {
    Optional<Category> findByName(String name);
}