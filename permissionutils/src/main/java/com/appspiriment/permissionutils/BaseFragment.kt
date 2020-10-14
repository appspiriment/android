package com.appspiriment.permissionutils

import androidx.fragment.app.Fragment
import java.lang.Exception

open class BaseFragment : Fragment() {
    protected val PERMISSION_REQUEST_CODE = 16
    protected val taskImpl by lazy { PermissionTaskImpl<MutableList<Pair<String, Int>>>() }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                mutableListOf<Pair<String, Int>>().let {
                    for (i in permissions.indices) {
                        it.add(Pair(permissions[i], grantResults[i]))
                    }
                    taskImpl.successListener?.onSuccess(it)
                }
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
//                taskImpl.failureListener?.onFailure(Exception("Permission not granted"))

            }
        }

    }

    fun requestPermission(arrayOf: Array<String>): PermissionTask<MutableList<Pair<String, Int>>> {
        requestPermissions(arrayOf, PERMISSION_REQUEST_CODE)
        return taskImpl
    }
}