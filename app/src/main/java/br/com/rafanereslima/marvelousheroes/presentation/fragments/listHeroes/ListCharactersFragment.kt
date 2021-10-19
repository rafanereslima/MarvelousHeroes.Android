package br.com.rafanereslima.marvelousheroes.presentation.fragments.listCharacters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.rafanereslima.marvelousheroes.R
import br.com.rafanereslima.marvelousheroes.presentation.activities.MainActivity
import br.com.rafanereslima.marvelousheroes.presentation.adapters.ListCharactersAdapter
import br.com.rafanereslima.marvelousheroes.presentation.components.CustomAlertView
import br.com.rafanereslima.marvelousheroes.presentation.components.CustomLoading
import kotlinx.android.synthetic.main.list_characters_fragment.*


class ListCharactersFragment(): Fragment(), CustomAlertView.CustomDialogCallback {

    companion object {
        fun newInstance(clickListener: MainActivity) = ListCharactersFragment()
    }

    private lateinit var viewModel: ListCharactersViewModel
    private var alert: CustomAlertView? = null

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
        viewModel.getObserverCharacters().observe(viewLifecycleOwner, {
            if (listCharactersRecyclerView.adapter == null) {
                CustomLoading.getInstance(activity).hide()
                listCharactersRecyclerView.adapter = context?.let { context -> ListCharactersAdapter(context, it.data) }
            } else {
                CustomLoading.getInstance(activity).hide()
                (listCharactersRecyclerView.adapter as ListCharactersAdapter).loadMoreData(it.data)
            }
        })

        viewModel.getObserverError().observe(viewLifecycleOwner, {
            alert = CustomAlertView(activity, null, context?.getString(R.string.error), it.message, null, context?.getString(R.string.accept), this, 0, false)
            alert?.show()
        })

        cellBodyListsDetail.setOnClickListener {
            if (viewModel.isSearch()) {
                viewModel.clearSearch()
                viewModel.characters?.data?.let { it1 -> (listCharactersRecyclerView.adapter as ListCharactersAdapter).loadMoreData(it1) }
            }
        }
    }

    override fun onClickLeftButton(tag: Int) {}

    override fun onClickRightButton(tag: Int) {
        alert?.hide()
        alert = null
    }
}