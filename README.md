# Android Utils Library
[![API](https://img.shields.io/badge/API-9%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=9)
[ ![Download](https://api.bintray.com/packages/appspiriment/android/androidutils/images/download.svg?version=0.1.0) ](https://bintray.com/appspiriment/android/androidutils/0.1.0/link)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=102)](https://opensource.org/licenses/Apache-2.0)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/amitshekhariitbhu/Fast-Android-Networking/blob/master/LICENSE)

## About Android Utils Library

Android Utils library is a simple library which helps developers to use some commonly used methods easily. 

## Why use Fast Android Networking ?
* It brings a bunch of most commonly used methods into a single library, which helps you write clean ode, and avoid clutter. The methods are organized into different classess based on purpose.

Utils available:

### LogUtils
* This util class helps you print logs and stacktrace. This methods print the Log and Stacktrace only on DEBUG mode, hence avoids the requirement of clearing the Log statements on release.
    #### Methods
    ```java
      printLog(message: Any?, tag: String = "LogTag: ", isError: Boolean = false)
     ```
