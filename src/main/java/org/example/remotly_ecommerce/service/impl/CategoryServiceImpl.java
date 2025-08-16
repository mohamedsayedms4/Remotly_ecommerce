package org.example.remotly_ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.remotly_ecommerce.exception.CategoryException;
import org.example.remotly_ecommerce.model.Category;
import org.example.remotly_ecommerce.repository.CategoryRepository;
import org.example.remotly_ecommerce.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    /**
     * @param id
     * @return
     */
//    @Override
//    public Optional<Category> findById(Long id) throws CategoryException {
//        log.debug("Finding category by ID: {}", id);
//        Category category = categoryRepository.findById(id)
//                .orElseThrow(() -> {
//                    throw new CategoryException("Category Not Found with ID: " + id)
//                });
//        return Optional.of(category);
//    }
    @Override
    public Optional<Category> findById(Long id) throws CategoryException {
        log.debug("Finding category by ID: {}", id);
        Category category = categoryRepository.findById(id).orElseThrow(() -> {
            log.info("Category with ID {} not found", id);
            return new CategoryException("Category Not Found with ID: " + id);
        });
        return Optional.of(category);
    }



    /**
     * @return
     */
    @Override
    public List<Category> findAll() throws CategoryException{
        log.debug("Finding all categories");
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            log.info("No categories found");
            throw new CategoryException("No categories found");
        }
        return categories;
    }

    /**
     * @param category
     * @return
     */
    @Override
    public Category createCategory(Category category) throws CategoryException {
        log.debug("Creating category: {}", category.getName());

        // تحقق لو الكاتيجوري بنفس الاسم موجودة بالفعل
        if (categoryRepository.existsByName(category.getName())) {
            log.warn("Category already exists with name: {}", category.getName());
            throw new CategoryException("Category already exists with name: " + category.getName());
        }

        // حفظ الكاتيجوري الجديدة
        Category savedCategory = categoryRepository.save(category);
        return savedCategory;
    }


    /**
     * @param category
     * @return
     */
    @Override
    public Optional<Category> updateCategory(Category category) {
        return Optional.empty();
    }

    /**
     * @param id
     */
    @Override
    public void deleteCategory(int id) {

    }
}
