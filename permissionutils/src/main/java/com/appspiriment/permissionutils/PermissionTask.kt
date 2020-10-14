package com.appspiriment.permissionutils

abstract class PermissionTask<TResult> {
    abstract var isComplete: Boolean
    abstract var isSuccessful: Boolean
    abstract var isCanceled: Boolean
    abstract var result: TResult?
    abstract fun addOnSuccessListener(var1: OnSuccessListener<TResult>): PermissionTask<TResult>
    abstract fun addOnFailureListener(var1: OnFailureListener): PermissionTask<TResult>?
}