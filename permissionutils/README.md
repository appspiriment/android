# Android Utils Library - Permission Utils
[![API](https://img.shields.io/badge/API-17%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=17)
[ ![Download](https://api.bintray.com/packages/appspiriment/android/AndroidUtils/images/download.svg?version=0.1.1) ](https://bintray.com/appspiriment/android/AndroidUtils/0.1.1/link)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=102)](https://opensource.org/licenses/Apache-2.0)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/appspiriment/AndroidUtils/blob/master/LICENSE)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)


Latest Version `0.1.2`

> This library is using AndroidX. If you are still using support library and haven't migrated to AndroidX then you cannot use this library.

### About Permission Utils
A lightweight Android library which wraps boilerplate code of runtime permission and allows you to request permissions aynchronously. This will help you avoiding overriding on `onRequestPermissionsResult` and help you implement your logic in a linear fashion. 

> This library is optimized for Kotlin. Java users will be unable to use some methods. Optional values can be skipped in Kotlin. In Java, you have to pass the default value


### Why use Permission Utils Library?
* This library has several methods which helps you obtain permission results or pass the actions as callback. 
* This library also provide a `PermissionListener` interface to handle permission results.
* Functions implemented as Extention Functions for both AppCompatActivity and Fragment

### How to use?
   Add the below line to your app level `build.gradle`. Replace `{latest_version}` with the latest version of the library.

   ```gradle
   implementation 'com.appspiriment.android:permissionutils:{latest_version}'
```

## What is available?

* ###  Coroutine Scope
    This method helps you to obtain the Coroutine Scope 

    ```kotlin 
    fun getCoroutineScope(): CoroutineScope
    ```
     Usage:


    ```kotlin 
    PermissionManager.getCoroutineScope().launch{ 
        //your logic to run on coroutine scope
    }
    ```

* ###  Request Permission and get result
    This method helps you to request permissions and get the result in linear fashion using Coroutine Scope.  

    ```kotlin 
    suspend fun requestPermissionsAsync(requestId: Int,vararg permissions: String): PermissionResult
    ```

     ***Usage***:
    This could be used to get the result of permissions request, like whether user granted or denied or denied the permission permanantly. You can act upon the result.

    This is a suspended function, and should be called from a `coroutineScope`. This function is available for Activity and Fragment as extention function.

    ```kotlin
    PermissionManager.getCoroutineScope().launch{
        val REQUEST_ID = 1001 //Any integer given for identifying the request
        val result = requestPermissionsAsync(REQUEST_ID, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    }
    ```
    The result is given as `PermissionResult`. You can check the result as :

    ```kotlin 
    when (result) {
        is PermissionResult.PermissionGranted -> // Do your Permission Granted Logic
        is PermissionResult.PermissionDenied ->  // When user denies the permission for first time
        is PermissionResult.PermissionDeniedPermanently -> // When user perss "Deny and never ask again"
        is PermissionResult.ShowRational -> // When we request the permission and user has denied it previously
    }    
    ```

* ###  Request Permission with callback
    This method helps you to request permissions asynchronously. The result handlers could be passed as callbacks.  

    If you pass `requestPermissionOnRationale` as true, then this function will call requestPermissionsAsync once more time, when user has previously denied. In that case, it will not call `permissionRationalListener`.

    ```kotlin 
    fun requestPermissionsAsync(
        requestId: Int,
        permissionGrantedListener: (requestId: Int) -> Unit = {},
        permissionDeniedListener: (requestId: Int, deniedPermissions: List<String>) -> Unit = { _, _ -> },
        permissionDeniedPermanently: (requestId: Int, deniedPermissions: List<String>) -> Unit = { _, _ -> },
        permissionRationalListener: (requestId: Int) -> Unit = {},
        requestPermissionOnRationale: Boolean,
        vararg permissions: String
    ) 
    ```

     ***Usage***:
    This is NOT a suspended function,so could be called from main thread. This function is available as extention function for both Activity and Fragment
  
* ###  Request Permission with listener
    This method helps you to request permissions asynchronously. The result could be handled through a `PermissionResult` interface which could be implemented or pass as object.

    ```kotlin 
    fun requestPermissionsAsync(
        requestId: Int,
        permissionListener: PermissionListener,
        vararg permissions: String
    ) 
    ```

     ***Usage***:
    This is NOT a suspended function,so could be called from main thread. This function is available as extention function for both Activity and Fragment.

    The `PermissionListener` interface has below methods:
    ```kotlin
    ▸ fun onPermissionGranted(requestCode: Int)
    ▸ fun onPermissionDenied(requestCode: Int, deniedPermissions: List<String>)
    ▸ fun onPermissionDeniedPermanently( requestCode: Int, permanentlyDeniedPermissions: List<String>)
    ▸ fun onShowRational(requestCode: Int)
    ``` 

* ###  Request Permission with rationale
    This method helps you to request permissions asynchronously. The result could be handled through a `PermissionResult` interface which could be implemented or pass as object. You could also display a rationale when the user had denied the permission earlier.

    ```kotlin 
    fun requestPermissionsAsync(
        requestId: Int,
        permissionsList: List<String>,
        permissionListener: PermissionResultListener,
        rationaleTitle: String? = null,
        rationaleMessage: String? = null,
        rationaleTitleResId: Int? = null,
        rationaleMessageResId: Int? = null
    )
    ```

     ***Usage***:
    
    You have to provide either `rationaleTitle` or `rationaleTitleResId`. Either one should be given other wise it will raise an exception. If both are given, the function will take `rationaleTitle` only. Same is applicable for `rationaleMessage` and `rationaleMessageResId`.

    The `PermissionResultListener` has the following methods:

    ```kotlin
    ▸ fun onPermissionGranted(requestCode: Int)
    ▸ fun onPermissionDenied(requestCode: Int, deniedPermissions: List<String>)
    ▸ fun onPermissionDeniedPermanently( requestCode: Int, permanentlyDeniedPermissions: List<String>)
    ```

    This functionality can also be obtained using lambda callbacks for permission results:

    
    ```kotlin
    fun requestPermissionsAsync(
        requestId: Int,
        permissionsList: List<String>,
        permissionGrantedListener: (requestId: Int) -> Unit,
        permissionDeniedListener: (requestId: Int, deniedPermissions: List<String>) -> Unit,
        permissionDeniedPermanently: (requestId: Int, deniedPermissions: List<String>) -> Unit,
        rationaleTitle: String? = null,
        rationaleMessage: String? = null,
        rationaleTitleResId: Int? = null,
        rationaleMessageResId: Int? = null
    )
    ```

Both of the above functions are NOT suspended functions,so could be called from main thread. These functions are available as extention functions only for Activity. If you want this on fragment, call as `requireActivity().requestPermissionsAsync`.

## CREDITS
* [Mr. Vipin BP](https://github.com/vipinbpkyr/Permission-Helper) - For the initial idea and encouragement

<br>

#### Copyright (C) 2019 Appspiriment Labs

       ```
       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at
       
       http://www.apache.org/licenses/LICENSE-2.0
       
       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.
       ```
