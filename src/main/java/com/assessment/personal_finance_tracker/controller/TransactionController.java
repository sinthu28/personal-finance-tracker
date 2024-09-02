package com.assessment.personal_finance_tracker.controller;

import com.assessment.personal_finance_tracker.model.Transaction;
import com.assessment.personal_finance_tracker.service.CategoryService;
import com.assessment.personal_finance_tracker.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public TransactionController(TransactionService transactionService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @GetMapping("/new")
    public String showTransactionForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("categories", categoryService.findAll());
        return "transaction-form";
    }

    @PostMapping("/save")
    public String saveTransaction(@Valid @ModelAttribute("transaction") Transaction transaction, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "transaction-form";
        }
        transactionService.saveTransaction(transaction);
        return "redirect:/";
    }

    @GetMapping("/history")
    public String showTransactionHistory(Model model) {
        model.addAttribute("transactions", transactionService.findAll());
        return "transaction-history";
    }
}