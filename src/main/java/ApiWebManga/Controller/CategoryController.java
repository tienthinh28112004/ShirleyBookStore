package ApiWebManga.Controller;

import ApiWebManga.Entity.Category;
import ApiWebManga.dto.Request.ApiResponse;
import ApiWebManga.dto.Request.CategoryRequest;
import ApiWebManga.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/insertCategory")
    @Operation(summary = "insert Category")
    public ApiResponse<Category> createCategory (@RequestBody @Valid CategoryRequest categoryRequest) {
        return ApiResponse.<Category>builder()
                .result(categoryService.createCategory(categoryRequest))
                .build();
    }

    @GetMapping (value = "/getCategoryById/{categoryId}")
    @Operation(summary = "name Category")
    public ApiResponse<Category> categoryName (@PathVariable Long categoryId) {
        return ApiResponse.<Category>builder()
                .result(categoryService.getCategoryById(categoryId))
                .build();
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping (value = "/listCategory")
    @Operation(summary = "list all category")
    public ApiResponse<List<Category>> listAllCategory () {
        return ApiResponse.<List<Category>>builder()
                .result(categoryService.getAllCategories())
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/deleteCategory/{categoryId}")
    @Operation(summary = "delete category")
    public ApiResponse<Void> deleteCategory (@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.<Void>builder()
                .message("delete category successfully")
                .build();
    }
}
