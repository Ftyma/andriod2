package com.example.andriod2


import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amity.socialcloud.sdk.api.core.AmityCoreClient
import com.amity.socialcloud.sdk.api.core.endpoint.AmityEndpoint
import com.amity.socialcloud.sdk.core.session.model.SessionState
import com.amity.socialcloud.sdk.model.core.session.SessionHandler
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<String?>(null)
    val loginState: StateFlow<String?> get() = _loginState

    private val _sessionState = MutableStateFlow<SessionState?>(null)
    val sessionState: StateFlow<SessionState?> get() = _sessionState

    fun authenticate(sessionHandler: SessionHandler, userId: String) {
        viewModelScope.launch {
            try {
                AmityCoreClient.login(userId, sessionHandler)
                    .displayName(displayName = "John Doe") // optional
                    .build()
                    .submit()
                    .doOnComplete {
                        //success
                        _loginState.value = "Login Success"
                        observeSessionState()
                    }
                    .doOnError{
                        _loginState.value = "Login Failed: ${it.message}"
                    }
                    .subscribe()
            } catch (e:Exception){
                _loginState.value = "Login Failed: ${e.message} "
            }
        }

    }
    fun observeSessionState() {
        AmityCoreClient.observeSessionState()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { sessionState: SessionState ->
                // SessionState
                _sessionState.value = sessionState
            }
            .doOnError {
                // Exception
                _sessionState.value = null
            }
            .subscribe()
    }

}



