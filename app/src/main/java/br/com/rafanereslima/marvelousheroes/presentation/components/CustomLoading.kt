package br.com.rafanereslima.marvelousheroes.presentation.components

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import br.com.rafanereslima.marvelousheroes.R

internal class CustomLoading(activity: Activity?) : AlertDialog(activity) {

    init {
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureUi()
    }

    private fun configureUi() {
        setContentView(R.layout.custom_loading)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
    }

    companion object {

        private var instance: CustomLoading? = null
        fun getInstance(activity: Activity?): CustomLoading {
            if (instance == null) {
                instance = CustomLoading(activity)
            }
            return instance!!
        }
    }
}