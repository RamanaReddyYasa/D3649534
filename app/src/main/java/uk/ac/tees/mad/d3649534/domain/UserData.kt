package uk.ac.tees.mad.d3649534.domain

data class UserData(
    val userId: String,
    val username: String?,
    val email: String?
)
data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class SignInState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)

data class SignInState1(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
