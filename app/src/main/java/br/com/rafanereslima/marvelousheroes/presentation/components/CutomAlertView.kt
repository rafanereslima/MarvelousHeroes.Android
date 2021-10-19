package br.com.rafanereslima.marvelousheroes.presentation.components

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import br.com.rafanereslima.marvelousheroes.R
import kotlinx.android.synthetic.main.custom_dialog.*

class CustomAlertView(
    activity: Activity?,
    private val animation: String?,
    private val title: String?,
    private val message: String?,
    private val textButtonLeft: String?,
    private val textButtonRight: String?,
    private val delegate: CustomDialogCallback?,
    var tag: Int,
    private val touchable: Boolean
) :
    AlertDialog(activity), View.OnClickListener {
    interface CustomDialogCallback {
        fun onClickLeftButton(tag: Int)
        fun onClickRightButton(tag: Int)
    }

    override fun show() {
        super.show()
        setCancelable(touchable)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureUI()
    }

    private fun configureUI() {
        setContentView(R.layout.custom_dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        buttonLeftAlert.setOnClickListener(this)
        buttonRigthAlert.setOnClickListener(this)
        if (animation == null) {
            watchAnimation.visibility = View.GONE
        } else {
            watchAnimation.setAnimation(animation)
        }
        if (title == null || "" == title) {
            textTitleDialog.visibility = View.GONE
        } else {
            textTitleDialog.text = title
        }
        if (message == null || "" == message) {
            textMessageAlert.visibility = View.GONE
        } else {
            textMessageAlert.text = message
        }
        if (textButtonLeft == null || "" == textButtonLeft) {
            buttonLeftAlert.visibility = View.GONE
            guidelineButtonAlert.setGuidelinePercent(0.0.toFloat())
        } else {
            buttonLeftAlert.text = textButtonLeft
        }
        if (textButtonRight == null || "" == textButtonRight) {
            buttonRigthAlert.visibility = View.GONE
            guidelineButtonAlert.setGuidelinePercent(1.0.toFloat())
        } else {
            buttonRigthAlert.text = textButtonRight
        }
        if ((textButtonLeft == null || "" == textButtonLeft) && (textButtonRight == null || "" == textButtonRight)) {
            buttonRigthAlert.visibility = View.GONE
            buttonLeftAlert.visibility = View.VISIBLE
            guidelineButtonAlert.setGuidelinePercent(1.0.toFloat())
            buttonLeftAlert.text = context.getText(R.string.accept)
        }
        //window!!.attributes.windowAnimations = R.style.SlidingDialogAnimation
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonLeftAlert -> {
                if (delegate != null) {
                    delegate.onClickLeftButton(tag)
                    return
                }
                hide()
            }
            R.id.buttonRigthAlert -> {
                if (delegate != null) {
                    delegate.onClickRightButton(tag)
                    return
                }
                hide()
            }
        }
    }
}