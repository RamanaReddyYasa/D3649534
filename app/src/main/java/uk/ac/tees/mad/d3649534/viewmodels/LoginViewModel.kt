package uk.ac.tees.mad.d3649534.viewmodels


import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import uk.ac.tees.mad.d3649534.data.domain.SignInResult
import uk.ac.tees.mad.d3649534.data.domain.SignInState
import uk.ac.tees.mad.d3649534.data.domain.SignInState1
import javax.inject.Inject


class LoginViewModel @Inject constructor(
) : ViewModel() {

    private val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    private val _state = MutableStateFlow(SignInState1())
    val state = _state.asStateFlow()

    private val _currentUser = MutableStateFlow(FirebaseAuth.getInstance().currentUser)
    val currentUser = _currentUser.asStateFlow()


    fun onSignInWithGoogleResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState1() }
    }

//
//    fun loginUser(email: String, password: String) = viewModelScope.launch {
//        repository.loginUser(email, password).collect { result ->
//            when (result) {
//                is Resource.Error -> {
//                    _signInState.send(SignInState(isError = result.message))
//                }
//
//                is Resource.Loading -> {
//                    _signInState.send(SignInState(isLoading = true))
//                }
//
//                is Resource.Success -> {
//                    _signInState.send(SignInState(isSuccess = "Sign In Success"))
//                    _loginResponse.update {
//                        it.copy(email = email)
//                    }
//                }
//            }
//        }
//    }

    fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
    }

}