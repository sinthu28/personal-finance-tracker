package com.assessment.personal_finance_tracker.repo;

import com.assessment.personal_finance_tracker.model.Transaction;
import com.assessment.personal_finance_tracker.model.TransactionType;
import com.assessment.personal_finance_tracker.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsWithinDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type AND t.user.id = :userId")
    // BigDecimal calculateTotalAmountByTypeAndUser(@Param("type") TransactionType type, @Param("userId") Long userId);

    List<Transaction> findByCategoryId(Long categoryId);

    List<Transaction> findByUser(User user);

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByType(TransactionType type);

    @Query("SELECT t FROM Transaction t WHERE t.type = :type AND t.transactionDate BETWEEN :startDate AND :endDate AND t.user.id = :userId")
    List<Transaction> findTransactionsByTypeAndDateRangeAndUser(
        @Param("type") TransactionType type,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        @Param("userId") Long userId
    );

    @Query("SELECT t FROM Transaction t WHERE FUNCTION('MONTH', t.transactionDate) = :month AND FUNCTION('YEAR', t.transactionDate) = :year AND t.user.id = :userId")
    List<Transaction> findTransactionsByMonthAndYearAndUser(
        @Param("month") int month, 
        @Param("year") int year,
        @Param("userId") Long userId
    );

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.type = :type")
    Optional<BigDecimal> calculateTotalAmountByType(@Param("type") TransactionType type);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = :type AND t.user.id = :userId")
    Optional<BigDecimal> calculateTotalAmountByTypeAndUser(@Param("type") TransactionType type, @Param("userId") Long userId);

}