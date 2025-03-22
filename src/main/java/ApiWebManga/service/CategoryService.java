package ApiWebManga.service;

import ApiWebManga.Entity.Category;
import ApiWebManga.dto.Request.CategoryRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryService{
    Category createCategory(CategoryRequest request);
    Category getCategoryById(Long categoryId);
    List<Category> getAllCategories();
    Void deleteCategory(Long categoryId);
}
