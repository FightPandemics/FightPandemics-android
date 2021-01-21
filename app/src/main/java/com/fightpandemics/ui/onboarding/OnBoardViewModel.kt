package com.fightpandemics.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.core.result.Event
import com.fightpandemics.domain.OnBoardCompleteActionUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class OnBoardViewModel @Inject constructor(
    private val onBoardCompleteActionUseCase: OnBoardCompleteActionUseCase,
) : ViewModel() {

    private val _navigateToMainActivity = MutableLiveData<Event<Unit>>()
    val navigateToMainActivity: LiveData<Event<Unit>> = _navigateToMainActivity

    private val _navigateToSignIn = MutableLiveData<Event<Unit>>()
    val navigateToSignIn: LiveData<Event<Unit>> = _navigateToSignIn

    private val _navigateToSignUp = MutableLiveData<Event<Unit>>()
    val navigateToSignUp: LiveData<Event<Unit>> = _navigateToSignUp

    fun skipToHelpBoardClick() {
        viewModelScope.launch {
            onBoardCompleteActionUseCase(true)
            _navigateToMainActivity.value = Event(Unit)
        }
    }

    fun signInClick() {
        viewModelScope.launch {
            onBoardCompleteActionUseCase(true)
            _navigateToSignIn.value = Event(Unit)
        }
    }

    fun signUpClick() {
        viewModelScope.launch {
            onBoardCompleteActionUseCase(true)
            _navigateToSignUp.value = Event(Unit)
        }
    }
}
