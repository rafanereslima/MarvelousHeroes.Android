package br.com.rafanereslima.marvelousheroes.models

import com.google.gson.annotations.SerializedName

data class ErrorModel (
        @SerializedName("code") val code : Int,
        @SerializedName("message", alternate = ["status"]) val message : String
)