package com.assessment.personal_finance_tracker.service;

import com.assessment.personal_finance_tracker.model.Transaction;
import com.assessment.personal_finance_tracker.model.TransactionType;
import com.assessment.personal_finance_tracker.repo.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id);
    }

    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Transaction not found with id: " + id);
        }
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findTransactionsWithinDateRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalIncome(Long userId) {
        return transactionRepository.calculateTotalAmountByTypeAndUser(TransactionType.INCOME, userId).orElse(BigDecimal.ZERO);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalExpenses(Long userId) {
        return transactionRepository.calculateTotalAmountByTypeAndUser(TransactionType.EXPENSE, userId).orElse(BigDecimal.ZERO);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateBalance(Long userId) {
        BigDecimal totalIncome = getTotalIncome(userId);
        BigDecimal totalExpenses = getTotalExpenses(userId);
        return totalIncome.subtract(totalExpenses);
    }
}