package br.com.rafanereslima.marvelousheroes.presentation.fragments.listCharacters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.rafanereslima.marvelousheroes.models.CharactersModel
import br.com.rafanereslima.marvelousheroes.models.ErrorModel
import com.jorgel.marvel.coreServices.ResultResponse
import com.jorgel.marvel.coreServices.servicesHandler.CharactersServiceHandler
import com.jorgel.marvel.models.CharactersModel
import com.jorgel.marvel.models.ErrorModel

class ListCharactersViewModel : ViewModel() {
    var characters: CharactersModel? = null
    private var charactersSearch: CharactersModel? = null
    var searchText: String? = null

    private val charactersObserver = MutableLiveData<CharactersModel>()
    private val errorObserver = MutableLiveData<ErrorModel>()

    fun getCharacters() {
        CharactersServiceHandler()
            .getCharacters(
                searchText,
                getOffset(),
                getLimit(),
                object : ResultResponse<CharactersModel?> {
                override fun onSuccess(data: CharactersModel?) {
                    if (isSearch()) {
                        if (null != charactersSearch) {
                            data?.data?.results?.let { charactersSearch?.data?.results?.addAll(it) }
                            charactersSearch?.data?.total = data?.data?.total!!
                            charactersSearch?.data?.count = data.data.count
                        } else {
                            charactersSearch = data
                        }
                        charactersSearch?.data?.offset = data?.data?.offset!! + data.data.limit
                        charactersObserver.value = charactersSearch
                    } else {
                        if (null != characters) {
                            data?.data?.results?.let { characters?.data?.results?.addAll(it) }
                            characters?.data?.total = data?.data?.total!!
                            characters?.data?.count = data.data.count
                        } else {
                            characters = data
                        }
                        characters?.data?.offset = data?.data?.offset!! + data.data.limit
                        charactersObserver.value = characters
                    }
                }
                override fun onError(description: ErrorModel?) {
                    if (isSearch()) {
                        if (null != charactersSearch) {
                            charactersSearch?.data?.total = charactersSearch?.data?.results?.size!!
                            charactersSearch?.data?.count = charactersSearch?.data?.results?.size!!
                        }
                    } else {
                        if (null != characters) {
                            characters?.data?.total = characters?.data?.results?.size!!
                            characters?.data?.count = characters?.data?.results?.size!!
                        }
                    }
                    errorObserver.value = description
                }
            })
    }

    fun getObserverCharacters(): LiveData<CharactersModel> {
        return charactersObserver
    }

    fun getObserverError(): LiveData<ErrorModel> {
        return errorObserver
    }

    fun isSearch(): Boolean {
        return searchText != null && "" != searchText
    }

    fun isPagination() : Boolean {
        return if (isSearch()) {
            charactersSearch?.data?.results?.size != charactersSearch?.data?.total
        } else {
            characters?.data?.results?.size != characters?.data?.total
        }
    }

    fun clearSearch() {
        searchText = null
        charactersSearch = null
    }

    private fun getOffset(): Int? {
        return if (isSearch()) {
            charactersSearch?.data?.offset
        } else {
            characters?.data?.offset
        }
    }

    private fun getLimit(): Int? {
        return if (isSearch()) {
            charactersSearch?.data?.limit
        } else {
            characters?.data?.limit
        }
    }
}