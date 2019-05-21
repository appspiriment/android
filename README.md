# Android Utils Library
[![API](https://img.shields.io/badge/API-9%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=9)
[ ![Download](https://api.bintray.com/packages/appspiriment/android/androidutils/images/download.svg?version=0.1.0) ](https://bintray.com/appspiriment/android/androidutils/0.1.0/link)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=102)](https://opensource.org/licenses/Apache-2.0)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/amitshekhariitbhu/Fast-Android-Networking/blob/master/LICENSE)

## About Android Utils Library

Android Utils library is a simple library which helps developers to use some commonly used methods easily. 

## Why use Android Utils Library?
* It brings a bunch of most commonly used methods into a single library, which helps you write clean ode, and avoid clutter. The methods are organized into different classess based on purpose. 
* This library provides easier implementation of network/API access using a single entry method. This library implements [!Amit Sekhar's Fast Android Networking Library](https://github.com/amitshekhariitbhu/Fast-Android-Networking/blob/master/README.md).

Utils available:
        1. LogUtils
        1. NetworkUtils
        1. MessageUtils
        1. DeviceUtils
        

### LogUtils
* This util class helps you print logs and stacktrace. This methods print the Log and Stacktrace only on DEBUG mode, hence avoids the requirement of clearing the Log statements on release.
    #### Methods
    ##### Print Log     `printLog(message: Any?, tag: String = "LogTag: ", isError: Boolean = false)`
    This method is the implementation of Log.w method. This method helps your print the log in two levels - warning and error. The advantage of using this method is that this will print the log only in debug mode, and you don't need to take care of the data type in the message part.
    ```java
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
    ##### Print Stacktrace     `fun printStacktrace(e: Exception)`
    This method is the implementation of error.printStacktrace. The advantage of using this method is that this will print the stack trace only in debug mode.
    
    ```java
      function name : printStacktrace
      arguments:
            error - any error which is a subclass of Android Exception
     ```
    Sample Usage in Kotlin:

    ```java
        LogUtils.printStacktrace(error)
    ```
    
