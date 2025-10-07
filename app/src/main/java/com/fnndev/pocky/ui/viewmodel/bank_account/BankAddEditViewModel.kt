package com.fnndev.pocky.ui.viewmodel.bank_account

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fnndev.pocky.data.local.models.BankAccount
import com.fnndev.pocky.data.local.repository.AccountRepository
import com.fnndev.pocky.ui.screens.bank.bank_add_edit.BankAddEditState
import com.fnndev.pocky.ui.screens.bank.bank_add_edit.BankAddEditUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BankAddEditViewModel @Inject constructor(
    private val repository: AccountRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _addEditState = MutableStateFlow(BankAddEditState())
    val addEditState: StateFlow<BankAddEditState> = _addEditState.asStateFlow()

    init {
        val bankId = savedStateHandle.get<Int>("bankId")
        if (bankId != -1 && bankId != null) {
            loadBank(bankId)
        }
    }

    fun loadBank(bankId: Int) {
        viewModelScope.launch {
            val bank = repository.getBankById(bankId)
            bank.let {
                _addEditState.value = _addEditState.value.copy(
                    id = it.id,
                    name = it.name,
                    balance = it.balance.toString()
                )
            }
        }
    }

    fun onEvent(event: BankAddEditUiEvent) {
        when (event) {
            is BankAddEditUiEvent.OnBalanceChange -> {
                _addEditState.value = _addEditState.value.copy(
                    balance = event.balance
                )
            }

            is BankAddEditUiEvent.OnNameChange -> {
                _addEditState.value = _addEditState.value.copy(
                    name = event.name
                )
            }

            BankAddEditUiEvent.OnSaveClicked -> {
                saveBank()
            }
        }
    }

    private fun saveBank() {
        val name = _addEditState.value.name
        val balance = _addEditState.value.balance.toDouble()

        if (name.isBlank()) {
            _addEditState.value = _addEditState.value.copy(
                error = "نام بانک نباید خالی باشد"
            )
            return
        }

        if (_addEditState.value.id == -1 || _addEditState.value.id == null) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.insertBank(bank = BankAccount(name = name, balance = balance.toLong()))
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateBank(
                    bank = BankAccount(
                        id = _addEditState.value.id!!,
                        name = name,
                        balance = balance.toLong()
                    )
                )
            }
        }
        _addEditState.value = _addEditState.value.copy(isSuccess = true)
    }
}