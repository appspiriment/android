package com.appspiriment.permissionutils

class PermissionTaskImpl<TResult> : PermissionTask<TResult>() {
    var successListener: OnSuccessListener<in TResult>? = null
        private set
    var failureListener: OnFailureListener? = null
        private set
    override var isComplete = false
    override var isSuccessful = false
    override var isCanceled = false
    override var result : TResult? = null

    override fun addOnSuccessListener(var1: OnSuccessListener<TResult>): PermissionTask<TResult> {
        successListener = var1
        return this
    }

    override fun addOnFailureListener(var1: OnFailureListener): PermissionTask<TResult> {
        failureListener = var1
        return this
    }

}