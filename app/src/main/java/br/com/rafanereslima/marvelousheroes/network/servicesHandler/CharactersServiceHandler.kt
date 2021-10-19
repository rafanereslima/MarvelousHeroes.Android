package br.com.rafanereslima.marvelousheroes.network.servicesHandler

import br.com.rafanereslima.marvelousheroes.models.CharactersModel
import br.com.rafanereslima.marvelousheroes.models.ErrorModel
import br.com.rafanereslima.marvelousheroes.network.ConnectionManager
import br.com.rafanereslima.marvelousheroes.network.ResultResponse
import com.android.volley.Request
import com.google.gson.Gson
import org.json.JSONObject

class CharactersServiceHandler: BaseServiceHandler() {
    private val baseServiceUrl = "characters"

    fun getCharacters(
        nameStartsWith: String? = null,
        offset: Int? = null,
        limit: Int? = null,
        delegate: ResultResponse<CharactersModel?>
    ) {
        ConnectionManager.newInstance().request(
            Request.Method.GET,
            getUrl(nameStartsWith = nameStartsWith, offset = offset, limit = limit),
            object : ResultResponse<JSONObject?> {
                override fun onSuccess(data: JSONObject?) {
                    if (fromJson(data.toString()) != null) {
                        delegate.onSuccess(fromJson(data.toString()))
                    } else {
                        delegate.onError(fromErrorJson(data.toString()))
                    }
                }
                override fun onError(description: ErrorModel?) {
                    delegate.onError(description)
                }
            }
        )
    }

    fun getDetailCharacters(
        characterId: Int,
        delegate: ResultResponse<CharactersModel?>
    ) {
        ConnectionManager.newInstance().request(
            Request.Method.GET,
            getUrl(characterId = characterId),
            object : ResultResponse<JSONObject?> {
                override fun onSuccess(data: JSONObject?) {
                    if (fromJson(data.toString()) != null) {
                        delegate.onSuccess(fromJson(data.toString()))
                    } else {
                        delegate.onError(fromErrorJson(data.toString()))
                    }
                }
                override fun onError(description: ErrorModel?) {
                    delegate.onError(description)
                }
            }
        )
    }

    private fun getUrl(
        nameStartsWith: String? = null,
        offset: Int? = null,
        limit: Int? = null,
        characterId: Int? = null
    ): String {
        return getBaseUrl(
            baseServiceUrl + getPathURL(characterId) + getParameters(
                nameStartsWith,
                offset,
                limit
            )
        )
    }

    private fun getPathURL(characterId: Int?): String {
        if (characterId != null) {
            return  "/$characterId"
        }
        return ""
    }

    private fun fromJson(jsonObject: String): CharactersModel? {
        return Gson().fromJson(jsonObject, CharactersModel::class.java)
    }

    private fun fromErrorJson(jsonObject: String): ErrorModel? {
        return Gson().fromJson(jsonObject, ErrorModel::class.java)
    }
}