package com.assessment.personal_finance_tracker.controller;

import com.assessment.personal_finance_tracker.model.Transaction;
import com.assessment.personal_finance_tracker.model.TransactionType;
import com.assessment.personal_finance_tracker.service.TransactionService;
import com.assessment.personal_finance_tracker.model.Category;
import com.assessment.personal_finance_tracker.model.User;
import com.assessment.personal_finance_tracker.service.CategoryService;
import com.assessment.personal_finance_tracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

@Controller
public class DashboardController {

    private final TransactionService transactionService;
    private final UserService userService; // Assuming you have a UserService
    private final CategoryService categoryService; // Assuming you have a CategoryService

    public DashboardController(TransactionService transactionService, UserService userService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        Long userId = getCurrentUserId(); // Replace with your method to get current user ID

        if (userId == null) {
            model.addAttribute("error", "User not found");
            return "error";
        }

        BigDecimal totalIncome = transactionService.getTotalIncome(userId);
        BigDecimal totalExpenses = transactionService.getTotalExpenses(userId);

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

    @PostMapping("/addTransaction")
    public String addTransaction(
            @RequestParam("description") String description,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("type") String typeStr,
            @RequestParam("userId") Long userId,
            @RequestParam("categoryId") Long categoryId,
            Model model) {

        TransactionType type;
        try {
            type = TransactionType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid transaction type.");
            return "redirect:/";
        }

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setTransactionDate(LocalDateTime.now());

        try {
            User user = userService.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            Category category = categoryService.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Category not found"));
            transaction.setUser(user);
            transaction.setCategory(category);

            transactionService.saveTransaction(transaction);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/";
        }

        return "redirect:/";
    }

    private Long getCurrentUserId() {
        // Replace with your actual logic to fetch the current user ID
        return 1L; // Placeholder value
    }
}