package br.com.rafanereslima.marvelousheroes.presentation.fragments.listCharacters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jorgel.marvel.R
import com.jorgel.marvel.views.activities.CharacterClickCallbacks
import com.jorgel.marvel.views.genericViews.CustomAlertView
import com.jorgel.marvel.views.genericViews.CustomLoading
import com.jorgel.marvel.views.genericViews.CustomSearchAlertView
import kotlinx.android.synthetic.main.list_characters_fragment.*


interface ListCharactersCallbacks {
    fun onCharacterSelectedClick(title: String, characterSelected: Int)
}

class ListCharactersFragment(private var clickListener: CharacterClickCallbacks): Fragment(), ListCharactersCallbacks, CustomAlertView.CustomDialogCallback, CustomSearchAlertView.CustomDialogCallback {

    companion object {
        fun newInstance(clickListener: CharacterClickCallbacks) = ListCharactersFragment(clickListener)
    }

    private lateinit var viewModel: ListCharactersViewModel
    private var alert: CustomAlertView? = null
    private var searchView: CustomSearchAlertView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.list_characters_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        CustomLoading.getInstance(activity).show()
        viewModel = ViewModelProvider(this).get(ListCharactersViewModel::class.java)
        viewModel.getCharacters()
        configureUI()
    }

    private fun configureUI() {
        listCharactersRecyclerView.layoutManager = LinearLayoutManager(context)
        listCharactersRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (viewModel.isPagination()) {
                        viewModel.getCharacters()
                    }
                }
            }
        })
        configureSearch()
        viewModel.getObserverCharacters().observe(this, {
            if (listCharactersRecyclerView.adapter == null) {
                CustomLoading.getInstance(activity).hide()
                listCharactersRecyclerView.adapter = context?.let { context -> ListCharactersAdapter(context, it.data, this) }
            } else {
                CustomLoading.getInstance(activity).hide()
                (listCharactersRecyclerView.adapter as ListCharactersAdapter).loadMoreData(it.data)
            }
        })

        viewModel.getObserverError().observe(this, {
            alert = CustomAlertView(activity, null, context?.getString(R.string.error), it.message, null, context?.getString(R.string.accept), this, 0, false)
            alert?.show()
        })

        cellBodyListsDetail.setOnClickListener {
            if (viewModel.isSearch()) {
                viewModel.clearSearch()
                configureSearch()
                viewModel.characters?.data?.let { it1 -> (listCharactersRecyclerView.adapter as ListCharactersAdapter).loadMoreData(it1) }
            } else {
                searchView = CustomSearchAlertView(activity, context?.getString(R.string.search), context?.getString(R.string.cancel), context?.getString(R.string.accept), this)
                searchView?.show()
            }
        }
    }

    private fun configureSearch() {
        if (viewModel.isSearch()) {
            imageViewCharacter.setImageResource(R.drawable.ic_close)
            characterTitle.text = viewModel.searchText
        } else {
            imageViewCharacter.setImageResource(R.drawable.ic_search)
            characterTitle.text = context?.getText(R.string.search)
        }
    }

    override fun onCharacterSelectedClick(title: String, characterSelected: Int) {
        clickListener.onCharacterSelectedClick(title, characterSelected)
    }

    override fun onClickLeftButton(tag: Int) {}

    override fun onClickRightButton(tag: Int) {
        alert?.hide()
        alert = null
    }

    override fun onClickCancelButton() {
        searchView?.hide()
        searchView = null
    }

    override fun onClickSearchButton(stringSearch: String?) {
        searchView?.hide()
        searchView = null
        viewModel.searchText = stringSearch
        configureSearch()
        CustomLoading.getInstance(activity).show()
        viewModel.getCharacters()
    }
}