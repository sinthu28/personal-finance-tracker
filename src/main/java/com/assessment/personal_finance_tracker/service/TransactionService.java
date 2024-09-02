package com.assessment.personal_finance_tracker.service;

import com.assessment.personal_finance_tracker.model.Transaction;
import com.assessment.personal_finance_tracker.model.TransactionType;
import com.assessment.personal_finance_tracker.repo.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    public List<Transaction> getTransactionsWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findTransactionsWithinDateRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalIncome() {
        BigDecimal totalIncome = transactionRepository.calculateTotalAmountByType(TransactionType.INCOME);
        return totalIncome != null ? totalIncome : BigDecimal.ZERO;
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getTotalExpenses() {
        BigDecimal totalExpenses = transactionRepository.calculateTotalAmountByType(TransactionType.EXPENSE);
        return totalExpenses != null ? totalExpenses : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByCategory(Long categoryId) {
        return transactionRepository.findByCategoryId(categoryId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByUser(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByType(TransactionType type) {
        return transactionRepository.findByType(type);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByTypeAndDateRange(TransactionType type, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findTransactionsByTypeAndDateRange(type, startDate, endDate);
    }

    @Transactional
    public void saveTransaction(Transaction transaction) {
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateBalance(Long userId) {
        BigDecimal totalIncome = transactionRepository.calculateTotalAmountByType(TransactionType.INCOME);
        BigDecimal totalExpenses = transactionRepository.calculateTotalAmountByType(TransactionType.EXPENSE);

        return totalIncome.subtract(totalExpenses);
    }

    @Transactional
    public void saveAllTransactions(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);  // Spring Data JPA will automatically batch insert/update
    }

    @Transactional
    public void transferFunds(Long fromTransactionId, Long toTransactionId, BigDecimal amount) {
        Transaction fromTransaction = transactionRepository.findById(fromTransactionId).orElseThrow();
        Transaction toTransaction = transactionRepository.findById(toTransactionId).orElseThrow();

        fromTransaction.setAmount(fromTransaction.getAmount().subtract(amount));
        toTransaction.setAmount(toTransaction.getAmount().add(amount));

        transactionRepository.save(fromTransaction);
        transactionRepository.save(toTransaction);
    }
}


