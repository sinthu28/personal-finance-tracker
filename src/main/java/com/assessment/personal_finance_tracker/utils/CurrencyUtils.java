package com.assessment.personal_finance_tracker.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {

    public static String formatCurrency(BigDecimal amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US); // Adjust locale as needed
        return currencyFormat.format(amount);
    }

}