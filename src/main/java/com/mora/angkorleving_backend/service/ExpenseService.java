package com.mora.angkorleving_backend.service;

import com.mora.angkorleving_backend.DTOs.request.ExpenseRequest;
import com.mora.angkorleving_backend.DTOs.response.ExpenseResponse;

import java.util.List;

public interface ExpenseService {
    ExpenseResponse createExpense(ExpenseRequest request);

    List<ExpenseResponse> getAllExpenses();

    ExpenseResponse getExpenseById(Long id);

    List<ExpenseResponse> getExpensesByRoom(Long roomId);

    ExpenseResponse updateExpense(Long id, ExpenseRequest request);

    void deleteExpense(Long id);
}