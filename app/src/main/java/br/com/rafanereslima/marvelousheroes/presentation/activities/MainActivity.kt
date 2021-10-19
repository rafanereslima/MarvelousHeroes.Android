package br.com.rafanereslima.marvelousheroes.presentation.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.rafanereslima.marvelousheroes.R
import com.google.android.material.appbar.AppBarLayout
import br.com.rafanereslima.marvelousheroes.presentation.fragments.listCharacters.ListCharactersFragment

enum class TypeFragment {
    LIST_CHARACTER, DETAIL_CHARACTERS
}

interface CharacterClickCallbacks {
    fun onCharacterSelectedClick(title: String, characterSelected: Int)
}

class MainActivity : AppCompatActivity() {

    private val listTypeFragment: MutableMap<TypeFragment, Fragment> = mutableMapOf()
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureUi()
    }

    private fun configureUi() {
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)
        loadListCharactersFragment()
    }

    private fun loadListCharactersFragment() {
        if (!listTypeFragment.containsKey(TypeFragment.LIST_CHARACTER)) {
            listTypeFragment[TypeFragment.LIST_CHARACTER] = ListCharactersFragment.newInstance(this)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, listTypeFragment[TypeFragment.LIST_CHARACTER]!!)
            .commitNow()
        imageToolbar.visibility = View.VISIBLE
        titleToolbar.visibility = View.GONE
        val params: AppBarLayout.LayoutParams = mainToolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = 0
        mainToolbar.layoutParams = params
        mainToolbar.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    private fun loadDetailCharacterFragment(title: String, characterSelected: Int? = null) {
        if (!listTypeFragment.containsKey(TypeFragment.DETAIL_CHARACTERS)) {
            listTypeFragment[TypeFragment.DETAIL_CHARACTERS] = DetailCharacterFragment.newInstance(characterSelected!!, this)
        }
        this.title = title
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, listTypeFragment[TypeFragment.DETAIL_CHARACTERS]!!)
            .commitNow()
        configureToolbarTitle(title)
        mainToolbar.setNavigationOnClickListener {
            this.title = ""
            listTypeFragment.remove(TypeFragment.DETAIL_CHARACTERS)
            loadListCharactersFragment()
        }
    }

    private fun loadWebViewFragment(title: String, url: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, WebViewFragment.newInstance(url))
            .commitNow()
        mainToolbar.setNavigationOnClickListener { loadDetailCharacterFragment(this.title) }
        configureToolbarTitle(title)
    }

    private fun configureToolbarTitle(title: String) {
        imageToolbar.visibility = View.GONE
        titleToolbar.visibility = View.VISIBLE
        titleToolbar.text = title
        val params: AppBarLayout.LayoutParams = mainToolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = 0
        mainToolbar.layoutParams = params
        mainToolbar.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCharacterSelectedClick(title: String, characterSelected: Int) {
        loadDetailCharacterFragment(title, characterSelected)
    }

    override fun backList() {
        loadListCharactersFragment()
    }

    override fun openLink(title: String, url: String) {
        loadWebViewFragment(title, url)
    }
}