package com.appspiriment.permissionutils

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


class HeadlessFragment : BaseFragment() {
    private val mutableLiveData = MutableLiveData<Array<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mutableLiveData.observe(this, Observer {
            requestPermissions(it, PERMISSION_REQUEST_CODE)
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fragmentManager?.popBackStack()
    }

    fun requestPermissionInternal(arrayOf: Array<String>): PermissionTaskImpl<MutableList<Pair<String, Int>>> {
        requestPermissions(arrayOf, PERMISSION_REQUEST_CODE)
        return taskImpl
    }
}