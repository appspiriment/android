package com.appspiriment.baseclasses

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.standalone.KoinComponent

open class BaseViewModel : ViewModel(), KoinComponent {

    val viewState = MutableLiveData<Int>()
    val navigate = MutableLiveData<Int>()


    /***************************************
     * On Backpress
     ***************************************/
    fun setViewState(state: Int) {
        viewState.postValue(state)
    }

    /***************************************
     * On Backpress
     ***************************************/
    fun setNavigate(viewId: Int) {
        navigate.postValue(viewId)
    }

    /***************************************
     * On Backpress
     ***************************************/
    fun onClick(view: View) {
        onViewClicked(view.id)
    }

    /***************************************
     * On Backpress
     ***************************************/
    open fun onViewClicked(viewId: Int) {

    }
}