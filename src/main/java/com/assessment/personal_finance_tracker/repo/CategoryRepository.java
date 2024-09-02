package com.assessment.personal_finance_tracker.repo;

import com.assessment.personal_finance_tracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Add custom query methods if needed
}