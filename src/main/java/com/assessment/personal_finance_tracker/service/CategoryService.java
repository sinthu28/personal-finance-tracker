package com.assessment.personal_finance_tracker.service;

import com.assessment.personal_finance_tracker.model.Category;
import com.assessment.personal_finance_tracker.repo.CategoryRepository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    @Cacheable("categories")  // Cache the result to avoid hitting the database repeatedly
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}