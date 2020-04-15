package com.appspiriment.androidutils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.text.Html
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog

/********************************************
 * Class for methods related to Messages
 * *******************************************/
object UiUtils {
    /**
     * **************************************
     * Method actionTo show an Alert Dialog
     * ****************************************
     */
    fun Context.showToast(
        message: String? = null,
        isLong: Boolean = false,
        messageRes: Int = -1
    ) {
        Toast.makeText(
            this,
            if (messageRes != -1) this.getString(messageRes) else message ?: "Unkown error!",
            if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * **************************************
     * Method actionTo show an Alert Dialog with onClick Listeners
     * ****************************************
     */
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun Activity.showMsgDialog(
        title: String? = null, message: String? = null, titleRes: Int = -1,
        messageRes: Int = -1, viewRes: Int = -1, view: View? = null,
        positiveButton: String = "OK",
        negativeButton: String? = null,
        neutralButton: String? = null,
        finishActivityOnOk: Boolean = false,
        finishActivityAffinity: Boolean = false,
        finishActivityOnCancel: Boolean = false,
        isCancellable: Boolean = false,
        positiveClickListen: () -> Unit = {},
        negativeClickListen: () -> Unit = {},
        neutralClickListen: () -> Unit = {},
        onCancelListen: () -> Unit = {},
        iconRes: Int = R.mipmap.ic_launcher,
        isHtmlMessage: Boolean = false
    ) {


        if (this == null) return

        val dialogView = if (view == null && viewRes > 0) {
            layoutInflater.inflate(viewRes, null)
        } else view

        val titleText = if (title == null && titleRes > 0) {
            resources.getString(titleRes)
        } else title

        val messageText = if (message == null && messageRes != -1) {
            resources.getString(messageRes)
        } else message


        val onClickListener = DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> positiveClickListen()
                DialogInterface.BUTTON_NEGATIVE -> negativeClickListen()
                DialogInterface.BUTTON_NEUTRAL -> neutralClickListen()
            }
            if (finishActivityOnOk && which == DialogInterface.BUTTON_POSITIVE) {
                if (finishActivityAffinity && Build.VERSION.SDK_INT > 15)
                    finishAffinity()
                else finish()
            }
        }

        val onCancelListener = DialogInterface.OnCancelListener { dialog ->
            dialog.dismiss()
            onCancelListen()
            if (finishActivityOnCancel)
                if (finishActivityAffinity) finishAffinity() else finish()
        }

        AlertDialog.Builder(this).apply {
            if (titleText != null) setTitle(titleText)
            if (messageText != null) setMessage(
                if (isHtmlMessage) Html.fromHtml(messageText) else messageText
            )
            setCancelable(isCancellable)
            setPositiveButton(positiveButton, onClickListener)
            setIcon(iconRes)

            if (dialogView != null) setView(dialogView)

            if (negativeButton != null) setNegativeButton(negativeButton, onClickListener)
            if (neutralButton != null) setNeutralButton(neutralButton, onClickListener)

            setOnCancelListener(onCancelListener)
        }.create().show()
    }

    /**
     * **************************************
     * Method to hide Soft Keyboard
     * ****************************************
     */
    fun Activity.hideKeyboard() {
        try {
            val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = this.currentFocus
            if (view == null) {
                view = View(this)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
        }
    }

}

