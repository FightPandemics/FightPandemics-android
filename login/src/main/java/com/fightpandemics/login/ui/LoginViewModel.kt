package com.fightpandemics.login.ui

import androidx.lifecycle.ViewModel
import com.fightpandemics.dagger.scope.ActivityScope
import com.fightpandemics.domain.repository.LoginRepository
import com.fightpandemics.login.domain.LoginUseCase
import javax.inject.Inject

@ActivityScope
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
}