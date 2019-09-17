package top.pythong.noteblog.base.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_alert.*
import org.apache.commons.lang3.RandomUtils
import org.jetbrains.anko.AlertBuilder
import org.jetbrains.anko.alert
import org.jetbrains.anko.customView
import org.jetbrains.anko.editText
import top.pythong.noteblog.R
import top.pythong.noteblog.app.download.DownloadService
import java.io.Serializable

class Alert : AppCompatActivity() {

//    var title = ""
//    var message = ""
//    var customView: View? = null
//    var nBText = ""
//    var nPText = ""
//    var pBText = ""
//
//    var nBClick = {}
//    var nPClick = {}
//    var pBClick = {}

    companion object {

        private var alert: Alert? = null

        var titleT = ""
        var message = ""
        var customView: View? = null
        var nBText = ""
        var nPText = ""
        var pBText = ""

        var nBClick = { this.dismiss() }
        var nPClick = { this.dismiss() }
        var pBClick = { this.dismiss() }

        var nb = false
            private set
        var np = false
            private set
        var pb = false
            private set

        fun negativeButton(text: String = "取消", onClick: () -> Unit) {
            nBText = text
            nBClick = onClick
            nb = true
        }

        fun neutralPressed(text: String = "中立", onClick: () -> Unit) {
            nPText = text
            nPClick = onClick
            np = true
        }

        fun positiveButton(text: String = "确定", onClick: () -> Unit) {
            pBText = text
            pBClick = onClick
            pb = true
        }

        fun show(context: Context) {
            val intent = Intent(context, Alert::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(context, intent, null)
        }

        fun dismiss() {
            alert?.finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        alert = this

        other.setOnClickListener {
            finish()
        }

        if (titleT.isNotBlank()) {
            mTitle.text = titleT
        }

        if (message.isNotBlank()) {
            mMessage.text = message
        }

        if (customView != null) {
            mCustomView.removeAllViews()
            mCustomView.addView(customView)
            mCustomView.visibility = View.VISIBLE
        }

        if (nb) {
            mNegativeButton.visibility = View.VISIBLE
            mNegativeButton.text = nBText
            mNegativeButton.setOnClickListener {
                nBClick()

                dismiss()
            }

        }

        if (np) {
            mNeutralPressed.visibility = View.VISIBLE
            mNeutralPressed.setOnClickListener {
                nPClick()

                dismiss()
            }
            mNeutralPressed.text = nPText
        }


        if (pb) {
            mPositiveButton.visibility = View.VISIBLE
            mPositiveButton.setOnClickListener {
                pBClick()

                dismiss()
            }
            mPositiveButton.text = pBText
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        alert = null
        customView = null
    }
}
