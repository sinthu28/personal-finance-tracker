package com.assessment.personal_finance_tracker.controller;

import com.assessment.personal_finance_tracker.service.TransactionService;
import com.assessment.personal_finance_tracker.model.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

@Controller
public class DashboardController {

    private final TransactionService transactionService;

    public DashboardController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        BigDecimal totalIncome = transactionService.getTotalIncome();
        BigDecimal totalExpenses = transactionService.getTotalExpenses();
    
        if (totalIncome == null) {
            totalIncome = BigDecimal.ZERO;
        }
        if (totalExpenses == null) {
            totalExpenses = BigDecimal.ZERO;
        }
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String formattedTotalIncome = currencyFormat.format(totalIncome);
        String formattedTotalExpenses = currencyFormat.format(totalExpenses);
        BigDecimal balance = totalIncome.subtract(totalExpenses);
        String formattedBalance = currencyFormat.format(balance);

        List<Transaction> recentTransactions = transactionService.getTransactionsWithinDateRange(
                LocalDateTime.now().minusMonths(1), LocalDateTime.now()
        );
    
        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("balance", balance);
        model.addAttribute("recentTransactions", recentTransactions);
        model.addAttribute("formattedTotalIncome", formattedTotalIncome);
        model.addAttribute("formattedTotalExpenses", formattedTotalExpenses);
        model.addAttribute("formattedBalance", formattedBalance);
    
        return "dashboard";
    }
}
