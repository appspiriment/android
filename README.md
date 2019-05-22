# Android Utils Library
[![API](https://img.shields.io/badge/API-9%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[ ![Download](https://api.bintray.com/packages/appspiriment/android/androidutils/images/download.svg?version=0.1.0) ](https://bintray.com/appspiriment/android/androidutils/0.1.0/link)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=102)](https://opensource.org/licenses/Apache-2.0)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/amitshekhariitbhu/Fast-Android-Networking/blob/master/LICENSE)

## About Android Utils Library

Android Utils library is a simple library which helps developers to use some commonly used methods easily. 

## Why use Android Utils Library?
* It brings a bunch of most commonly used methods into a single library, which helps you write clean ode, and avoid clutter. The methods are organized into different classess based on purpose. 
* This library provides easier implementation of network/API access using a single entry method. This library implements [Amit Sekhar's Fast Android Networking Library](https://github.com/amitshekhariitbhu/Fast-Android-Networking/blob/master/README.md).

Utils available:
        * LogUtils
        * NetworkUtils
        * MessageUtils
        * DeviceUtils
        
> ***Optional values can be skipped in Kotlin. In Java, you have to pass the default value***

#### LogUtils
 This util class helps you print logs and stacktrace. This methods print the Log and Stacktrace only on DEBUG mode, hence avoids the requirement of clearing the Log statements on release.
 
* #### Print Log     `printLog(message: Any?, tag: String = "LogTag: ", isError: Boolean = false)`
    This method is the implementation of Log.w method. This method helps your print the log in two levels - warning and error. The advantage of using this method is that this will print the log only in debug mode, and you don't need to take care of the data type in the message part.
    ```
      function name : printLog
      arguments:
            message - You can give the message here. You can give any data type. 
                      The data will be converted to String by the library. 
                      You can even pass null, which prints "NULL"
            tag     - Tag for the Log message. [Optional. Will print "LogTag" if not given]
            isError - will print as Error if true else will print as Warning 
     ```
    Sample Usage in Kotlin:

    ```java
        LogUtils.printLog("Log Message")
        LogUtils.printLog("LogMessage", "LogTag")
        LogUtils.printLog(message= "LogMessage", isError = true)
    ```
    Sample Usage in Java:

    ```java
        LogUtils.Companion.printLog("Log Message")
        LogUtils.Companion.printLog("LogMessage", "LogTag")
        LogUtils.Companion.printLog(message= "LogMessage", isError = true)
    ```
* #### Print Stacktrace     `fun printStacktrace(e: Exception)`
    This method is the implementation of error.printStacktrace. The advantage of using this method is that this will print the stack trace only in debug mode.
    
    ```
      function name : printStacktrace
      arguments:
            error - any error which is a subclass of Android Exception
     ```
    Sample Usage in Kotlin:

    ```java
        LogUtils.printStacktrace(error)
    ```
    
    Sample Usage in java:

    ```java
        LogUtils.Companion.printStacktrace(error)
    ```
 
  ### MessageUtils
     This util provide methods to display messages to the user - AlertDialogs and Toasts.
 
 * #### Show Toast `fun showToast(context: Context, message: String, isLong: Boolean = false) `
        This method helps to hide the softkeyboard manually. 
     
     ```
        function name : showToast
        arguments:
                context - Context
                message - Message to be displayed
                isLong  - Whether the toast to be displayed Long or Short (Duration) 
     ```
    Sample Usage in Kotlin:

    ```java
        MessageUtils.showToast(this, "Sample Text")
        MessageUtils.showToast(this, "Sample Text", true)
    ```
    
    Sample Usage in java:

    ```java
        MessageUtils.Companion.showToast(this, "Sample Text", false)
        MessageUtils.Companion.showToast(this, "Sample Text", true)
    ```
    
* #### Show Message Dialog `showMsgDialog( activityThis: Activity, title: String? = null, message: String? = null, titleRes: Int = -1, messageRes: Int = -1, viewRes: Int = -1, view: View? = null, positiveButton: String = "OK", negativeButton: String? = null, neutralButton: String? = null, finishActivityOnOk: Boolean = false, finishActivityOnCancel: Boolean = false, isCancellable: Boolean = false, onClickListen: (Int) -> Unit = { }, onCancelListen: () -> Unit = {}, iconRes: Int = R.mipmap.ic_launcher)`
        
    This method is a util method to display an Alert Dialog. This is a highly customizable method, in which different combinations are possible.
     
     ```
        function name : showMsgDialog
        arguments:
                activityThis - Activity reference of which the Alert dialog to be displayed
                title - Title String of the Dialog [Optional]
                titleRes - Resource Id of the String value for the title of Dialog. Either this or `title` could be used [Optional]
                message  - Message String of the Dialog [Optional]
                messageRes - Resource Id of the String value for the message of  Dialog. Either this or `message` could be used [Optional]
                viewRes - Resource id for the custom view for the dialog [Optional]
                view - View object for the custom view for the dialog [Optional]
                positiveButton - Text for the Positive Button  [Optional - will display 'OK' if not given]
                negativeButton - Text for the Negative Button  [Optional - will hide negative button if not given]
                neutralButton - Text for the Neutral Button  [Optional - will hide neutral button if not given]
                finishActivityOnOk - if this is true, the activity will be finished on click of Positive Button [Optional]
                finishActivityOnCancel - if this is true, the activity will be finished on cancelling the dialog box [Optional]
                isCancellable - Whether the dialog is cancellable or not [Optional]
                onClickListen - Labmda function to handle the onclick. The button id will be passed as a labda parameter, and could be managed inside the lambda. [Optional - will close the dialog if not given]
                onCancelListen - Labmda function to handle the onCancel event. [Optional]
                iconRes - Resource id for Dialog Icon

     ```
    Sample Usage in Kotlin:

    ```java
        MessageUtils.showMsg(this, "Sample Title", "Sample Text")
        MessageUtils.showMsg(this, "Sample Title", "Sample Text", "YES", "NO")
    ```

### DeviceUtils
     This util provide methods related to the device and hardware.
 
 * #### Hide Keyboard `fun hideKeyboard(activity: Activity)`
        This method helps to hide the softkeyboard manually. 
     
     ```
        function name : printStacktrace
        arguments:
                activity - Instance of the activity in which the keyboard to be hidden
     ```
    Sample Usage in Kotlin:

    ```java
        DeviceUtils.hideKeyboard(this)
    ```
     Sample Usage in java:

    ```java
        DeviceUtils.Companion.hideKeyboard(this)
    ```
    
 ### NetworkUtils
 
 This util provide methods related to the Network operations. The library makes use of [Amit Sekhar's Fast Android Networking Library](https://github.com/amitshekhariitbhu/Fast-Android-Networking) since it take care of several issues with Volley, and is easier to setup than retrofit. Powerful enough to handle most of the use-cases. Read more on Amit's [Github](https://github.com/amitshekhariitbhu/Fast-Android-Networking).
 
 **This class needs to be initialized using context and could not be used in static context**. It is required for initialization of Android Networking Library, and to enable Dependecy Injection.
 
 * #### Constants: 
    ```java
    val API_REQUEST_STRING = 101
    val API_REQUEST_JSON = 102
    val API_REQUEST_JSON_ARRAY = 103
    ```
 
 * #### Check Network Connection `fun isNetworkConnected() : Boolean`
        This method helps to access the network state. This will check if the device have network connection by mobile network or WiFi. It will not tell you whether internet is available, but only whether the network is connected.
     
     ```
        function name : isNetworkConnected
        arguments: None
        return: Boolean - true or false based on the avilability of network connection
     ```
    Sample Usage:

    ```java
        Java :
                NetworkUtils networkUtils = new NetworkUtils(this);
                boolean networkAvailable = networkUtils.isNetworkConnected();
        Kotlin:
                val isNetworkAvailable = NetworkUtils(this).isNetworkConnected()
    ```



