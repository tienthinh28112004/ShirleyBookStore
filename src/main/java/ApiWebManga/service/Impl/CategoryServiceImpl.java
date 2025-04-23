package ApiWebManga.service.Impl;

import ApiWebManga.Entity.Category;
import ApiWebManga.Exception.NotFoundException;
import ApiWebManga.dto.Request.CategoryRequest;
import ApiWebManga.repository.CategoryRepository;
import ApiWebManga.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public Category createCategory(CategoryRequest request) {
        Category category=Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        log.info("Lưu truyện thành công");
        log.info("category {}",category.getName());
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(()->new NotFoundException("category not found"));

    }
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
