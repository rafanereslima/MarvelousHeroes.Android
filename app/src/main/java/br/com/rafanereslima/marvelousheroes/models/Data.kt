package br.com.rafanereslima.marvelousheroes.models

import com.google.gson.annotations.SerializedName

data class Data (
    @SerializedName("offset") var offset : Int,
    @SerializedName("limit") var limit : Int,
    @SerializedName("total") var total : Int,
    @SerializedName("count") var count : Int,
    @SerializedName("results") val results : MutableList<Results>
)