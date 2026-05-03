package app.multiplatform.commons.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.multiplatform.commons.home.domain.ContributionsRepository
import app.multiplatform.commons.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ContributionsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchContributions()
    }

    fun fetchContributions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = repository.getContributions(itemLimit = 20, continuation = emptyMap())) {
                is Result.Success -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            contributions = result.data 
                        ) 
                    }
                }
                is Result.Error -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            error = result.error 
                        ) 
                    }
                }
            }
        }
    }
}
