package com.fightpandemics.login.ui

import androidx.lifecycle.ViewModel
import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.login.domain.LoginUseCase
import javax.inject.Inject

@ActivityScope
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
}



/*
*
* class LoginViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {

    private val _signUp = MutableLiveData<Result<SignUpModel>>()
    val signUp: LiveData<Result<SignUpModel>> = _signUp

    private val _login = MutableLiveData<Result<LoginModel>>()
    val login: LiveData<Result<LoginModel>> = _login

    private val _changePassword = MutableLiveData<Result<ChangePasswordModel>>()
    val changePassword: LiveData<Result<ChangePasswordModel>> = _changePassword

    fun executeSignUp(email: String, password: String, confirmPassword: String) {
        val signUpRequest = SignUpRequest(email, password, confirmPassword)
        _signUp.postValue(Result.Loading)
        viewModelScope.launch {
            try {
                val response = repository.signUp(signUpRequest)
                if (response.isSuccessful && response.code() == 0) {
                    val model = SignUpDataMapper().transformRemoteToLocal(response.body()!!)
                    _signUp.postValue(Result.Success(model))
                    //Consume the response here - Save it to local or display to the User if needed
                } else {
                    _signUp.postValue(Result.Error(Exception(response.message())))
                }
            } catch (error: Throwable) {
                _signUp.postValue(Result.Error(error))
            }
        }
    }

    fun doLogin(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        _login.postValue(Result.Loading)
        viewModelScope.launch {
            try {
                val response = repository.login(loginRequest)
                if (response.isSuccessful && response.code() == 0) {
                    val model = LoginDataMapper().transformRemoteToLocal(response.body()!!)
                    _login.postValue(Result.Success(model))
                    //Consume the response here - Save it to local or display to the User if needed
                } else {
                    _login.postValue(Result.Error(Exception(response.message())))
                }
            } catch (error: Throwable) {
                _login.postValue(Result.Error(error))
            }
        }
    }

    fun changePassword(email: String) {
        viewModelScope.launch {
            try {
                val response = repository.changePassword(email)
                if (response.isSuccessful && response.code() == 0) {
                    val model = ChangePasswordDataMapper().transformRemoteToLocal(response.body()!!)
                    _changePassword.postValue(Result.Success(model))
                    //Consume the response here - Save it to local or display to the User if needed
                } else {
                    _changePassword.postValue(Result.Error(Exception(response.message())))
                }
            } catch (error: Throwable) {
                _changePassword.postValue(Result.Error(error))
            }
        }
    }

    fun allInputFieldsHaveBeenFilled(email: String, password: String): Boolean {
        if (email == "" || password == "") {
            return false
        }
        return true
    }

    fun inValidEmail(value: String): String? {
        val valueMatcher = Pattern.compile(Patterns.EMAIL_ADDRESS.pattern()).matcher(value)
        if (valueMatcher.matches()) {
            return null
        }
        return "Please Enter a Valid Email Address"
    }
}
* */