package com.example.ambulanceondemand.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

abstract class BaseViewModel : ViewModel(){
    companion object {
        const val NEUTRAL : Int = 0
        const val SHOW_LOADING : Int = 1
        const val REMOVE_LOADING : Int = 2
        const val SUCCESS : Int = 3
        const val ERROR : Int = 4
        const val USED = 5
        const val NOT_FOUND = 6
        const val SUBMIT_SUCCESS = 7
        const val SUBMIT_ERROR = 8
        const val VALID = 9
        const val NOT_VALID = 10
        const val WAITING_APPROVAL = 11
    }
    val loading : MutableLiveData<Int> = MutableLiveData()

    protected fun launchCatchError(
        block: suspend () -> Unit,
        catch: suspend (e: Exception) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                block()
            }
            catch (e: Exception) {
                catch(e)
            }
        }
    }
}