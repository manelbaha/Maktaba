package com.ElOuedUniv.maktaba.presentation.book

import com.ElOuedUniv.maktaba.data.model.Book

/**
 * UI Actions representing user interactions on the Book screen.
 * TODO: Student must implement and use these actions in the ViewModel.
 */
sealed interface BookUiAction {

    data object OnAddBookClick : BookUiAction

    data object OnDismissAddBook : BookUiAction

    data class OnAddBookConfirm(
        val book: Book
    ) : BookUiAction
}
