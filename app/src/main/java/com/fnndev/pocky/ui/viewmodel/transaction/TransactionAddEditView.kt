package com.fnndev.pocky.ui.viewmodel.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fnndev.pocky.data.local.models.Transaction
import com.fnndev.pocky.data.local.repository.AccountRepository
import com.fnndev.pocky.ui.screens.transaction.transaction_add_edit.TransactionAddEditEvent
import com.fnndev.pocky.ui.screens.transaction.transaction_add_edit.TransactionAddEditState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionAddEditView @Inject constructor(
    private val repository: AccountRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _addEditTransactionState = MutableStateFlow(TransactionAddEditState())
    val addEditTransactionState = _addEditTransactionState.asStateFlow()
    init {
        val transactionId = savedStateHandle.get<Int>("transactionId")
        val bankId = savedStateHandle.get<Int>("bankId")
        if (bankId != null) {
            _addEditTransactionState.value = _addEditTransactionState.value.copy(bankId = bankId)
        }
        if (transactionId != null && transactionId != -1) {
            loadTransaction(transactionId)
        }
    }

    private fun loadTransaction(transactionId: Int) {
        viewModelScope.launch {
            val transaction = repository.getTransactionById(transactionId)
            transaction.let {
                _addEditTransactionState.value = _addEditTransactionState.value.copy(
                    transactionId = it!!.id,
                    bankId = it.bankAccountId,
                    transactionDate = it.date,
                    transactionType = it.type,
                    transactionAmount = it.amount.toString(),
                    transactionDescription = it.description
                )
            }
        }
    }

    fun onEvent(event: TransactionAddEditEvent) {
        when (event) {
            is TransactionAddEditEvent.OnAmountChange -> {
                _addEditTransactionState.value = _addEditTransactionState.value.copy(
                    transactionAmount = event.amount
                )
            }

            is TransactionAddEditEvent.OnDateChange -> {
                _addEditTransactionState.value = _addEditTransactionState.value.copy(
                    transactionDate = event.date
                )
            }

            is TransactionAddEditEvent.OnDescriptionChange -> {
                _addEditTransactionState.value = _addEditTransactionState.value.copy(
                    transactionDescription = event.description
                )
            }

            TransactionAddEditEvent.OnSaveClicked -> {
                saveTransaction()
            }

            is TransactionAddEditEvent.OnTransactionTypeChange -> {
                _addEditTransactionState.value = _addEditTransactionState.value.copy(
                    transactionType = event.transactionType
                )
            }
        }
    }

    private fun saveTransaction() {
        val transactionId = addEditTransactionState.value.transactionId
        val bankId = addEditTransactionState.value.bankId
        val date = addEditTransactionState.value.transactionDate
        val type = addEditTransactionState.value.transactionType
        val amount = addEditTransactionState.value.transactionAmount.toDouble()
        val description = addEditTransactionState.value.transactionDescription

        if (date.isBlank() || amount.toString().isBlank()) {
            _addEditTransactionState.value = _addEditTransactionState.value.copy(
                error = "اطلاعات را واردی کنید"
            )
            return
        }

        if (transactionId == null) {
            viewModelScope.launch {
                repository.insertTransaction(
                    Transaction(
                        bankAccountId = bankId!!,
                        date = date,
                        type = type,
                        amount = amount.toLong(),
                        description = description
                    )
                )

            }
        } else {
            viewModelScope.launch {
                repository.updateTransaction(
                    Transaction(
                        id = transactionId,
                        bankAccountId = bankId!!,
                        date = date,
                        type = type,
                        amount = amount.toLong(),
                        description = description
                    )
                )
            }
        }
        _addEditTransactionState.value = _addEditTransactionState.value.copy(isSuccess = true)
    }
}