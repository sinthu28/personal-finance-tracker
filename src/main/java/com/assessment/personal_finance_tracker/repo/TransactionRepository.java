package com.assessment.personal_finance_tracker.repo;

import com.assessment.personal_finance_tracker.model.Transaction;
import com.assessment.personal_finance_tracker.model.TransactionType;
import com.assessment.personal_finance_tracker.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsWithinDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type")
    BigDecimal calculateTotalAmountByType(@Param("type") TransactionType type);

    List<Transaction> findByCategoryId(Long categoryId);

    List<Transaction> findByUser(User user);

    // Page<Transaction> findByUserId(Long userId, Pageable pageable);
    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByType(TransactionType type);

    @Query("SELECT t FROM Transaction t WHERE t.type = :type AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsByTypeAndDateRange(
        @Param("type") TransactionType type,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT t FROM Transaction t WHERE FUNCTION('MONTH', t.transactionDate) = :month AND FUNCTION('YEAR', t.transactionDate) = :year")
    List<Transaction> findTransactionsByMonthAndYear(@Param("month") int month, @Param("year") int year);
}
