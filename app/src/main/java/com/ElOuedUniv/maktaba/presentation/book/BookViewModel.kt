package com.ElOuedUniv.maktaba.presentation.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.domain.usecase.GetBooksUseCase
import com.ElOuedUniv.maktaba.domain.usecase.AddBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.ElOuedUniv.maktaba.data.model.Book






@HiltViewModel

class BookViewModel @Inject constructor(
    private val getBooksUseCase: GetBooksUseCase,
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<BookUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getBooksUseCase()
                .catch {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .collect { bookList ->
                    _uiState.update {
                        it.copy(
                            books = bookList,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun refreshBooks() {
        loadBooks()
    }

    // ✅ Exercise 3 — Handle UI Actions
    fun onAction(action: BookUiAction) {

        when(action) {

            BookUiAction.OnAddBookClick -> {
                _uiState.update {
                    it.copy(isAddingBook = true)
                }
            }

            BookUiAction.OnDismissAddBook -> {
                _uiState.update {
                    it.copy(isAddingBook = false)
                }
            }

            is BookUiAction.OnAddBookConfirm -> {

                viewModelScope.launch {

                    addBookUseCase(action.book)

                    _uiState.update {
                        it.copy(isAddingBook = false)
                    }

                    _uiEvent.emit(
                        BookUiEvent.ShowToast("Book Added Successfully")
                    )

                }
            }
        }
    }
}
