package br.com.rafanereslima.marvelousheroes.network

import br.com.rafanereslima.marvelousheroes.base.BaseApplication
import br.com.rafanereslima.marvelousheroes.models.ErrorModel
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ConnectionManager {

    private val manager = Volley.newRequestQueue(BaseApplication.applicationContext())

    companion object {
        fun newInstance() = ConnectionManager()
    }

    fun request(method: Int, url: String, delegate: ResultResponse<JSONObject?>) {
        val request = StringRequest(method, url, {
            delegate.onSuccess(JSONObject(it))
        }, {
            delegate.onError(
                ErrorModel(
                it.hashCode(),
                it.message.toString()
            )
            )
        })
        manager.add(request)
    }
}