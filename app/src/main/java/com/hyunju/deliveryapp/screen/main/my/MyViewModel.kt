package com.hyunju.deliveryapp.screen.main.my

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hyunju.deliveryapp.data.preference.AppPreferenceManager
import com.hyunju.deliveryapp.screen.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(
    private val appPreferenceManager: AppPreferenceManager
) : BaseViewModel() {

    val myStateLiveData = MutableLiveData<MyState>(MyState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        myStateLiveData.value = MyState.Loading
        appPreferenceManager.getIdToken()?.let {
            myStateLiveData.value = MyState.Login(it)
        } ?: kotlin.run {
            myStateLiveData.value = MyState.Success.NotRegistered
        }
    }

    fun saveToken(idToken: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            appPreferenceManager.putIdToken(idToken)
            fetchData()
        }
    }

    fun setUserInfo(firebaseUser: FirebaseUser?) = viewModelScope.launch {
        firebaseUser?.let { user ->
            myStateLiveData.value = MyState.Success.Registered(
                userName = user.displayName ?: "익명",
                profileImageUri = user.photoUrl
            )
        } ?: kotlin.run {
            myStateLiveData.value = MyState.Success.NotRegistered
        }
    }

    fun signOut()  = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            appPreferenceManager.removeIdToken()
        }
        fetchData()
    }

}