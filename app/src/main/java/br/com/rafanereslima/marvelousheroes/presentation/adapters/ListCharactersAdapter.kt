package br.com.rafanereslima.marvelousheroes.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import br.com.rafanereslima.marvelousheroes.R
import br.com.rafanereslima.marvelousheroes.models.Data
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.cell_character.view.*

class ListCharactersAdapter(private val context: Context, private var data: Data) :
        RecyclerView.Adapter<ListCharactersAdapter.ListCharactersHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCharactersHolder {
        return ListCharactersHolder(LayoutInflater.from(context).inflate(R.layout.cell_character, parent, false))
    }

    override fun onBindViewHolder(holder: ListCharactersHolder, position: Int) {
        holder.bindItems(context, position)
    }

    override fun getItemCount(): Int {
        return if (isPagination()) {
            data.results.size + 1
        } else {
            data.results.size
        }
    }

    fun loadMoreData(data: Data) {
        this.data = data
        notifyDataSetChanged()
    }

    private fun isPagination() : Boolean {
        return data.results.size != data.total
    }

    inner class ListCharactersHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(context: Context, position: Int) {
            if (isPagination() && position == data.results.size) {
                itemView.cellBodyListsDetail.visibility = View.GONE
                itemView.loadingCell.visibility = View.VISIBLE
            } else {
                itemView.cellBodyListsDetail.visibility = View.VISIBLE
                itemView.loadingCell.visibility = View.GONE
                Glide
                    .with(context)
                    .load(data.results[position].thumbnail.path.replace("http", "https") + "." + data.results[position].thumbnail.extension)
                    .centerCrop()
                    .placeholder(R.drawable.ic_marvellogo)
                    .into(itemView.imageViewCharacter)
                itemView.characterTitle.text = data.results[position].name
                itemView.setOnClickListener {
                    Toast.makeText(context, data.results[position].name, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

