package br.com.rafanereslima.marvelousheroes.network

import br.com.rafanereslima.marvelousheroes.models.ErrorModel

interface ResultResponse<T> {
    fun onSuccess(data: T)
    fun onError(description: ErrorModel?)
}