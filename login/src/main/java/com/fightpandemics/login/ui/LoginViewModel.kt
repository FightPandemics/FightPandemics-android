package com.fightpandemics.login.ui

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fightpandemics.dagger.scope.ActivityScope
import com.fightpandemics.login.data.Result
import com.fightpandemics.login.data.mapper.ChangePasswordMapper
import com.fightpandemics.login.data.model.ChangePasswordModel
import com.fightpandemics.login.data.repository.DataRepository
import com.fightpandemics.login.networking.LoginRequest
import com.fightpandemics.login.networking.SignUpRequest
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@ActivityScope
class LoginViewModel @Inject constructor(private val repository: DataRepository) : ViewModel() {

    private val _signUp = MutableLiveData<Result<Nothing>>()
    val signUp: LiveData<Result<Nothing>> = _signUp

    private val _login = MutableLiveData<Result<Nothing>>()
    val login: LiveData<Result<Nothing>> = _login

    private val _changePassword = MutableLiveData<Result<ChangePasswordModel>>()
    val changePassword: LiveData<Result<ChangePasswordModel>> = _changePassword

    fun executeSignUp(email: String, password: String, confirmPassword: String) {
        val signUpRequest = SignUpRequest(email, password, confirmPassword)
        _signUp.postValue(Result.Loading)
        viewModelScope.launch {
            try {
                val response = repository.signUp(signUpRequest)
                if (response.isSuccessful && response.code() == 0) {
                    _signUp.postValue(Result.Success())
                    //Consume the response here - Save it to local or display to the User if needed
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
                    _login.postValue(Result.Success())
                    //Consume the response here - Save it to local or display to the User if needed
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
                    val responseModel = ChangePasswordMapper().transformRemoteToLocal(response.body()!!)
                    _changePassword.postValue(Result.Success(responseModel))
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