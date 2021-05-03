package com.example.shortcut.ui.fragment.comics.cards.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.shortcut.R
import com.example.shortcut.ui.image.ImageLoader
import com.example.shortcut.ui.model.UiComicStrip
import com.example.shortcut.ui.view.FavouriteImageView


internal class CardsAdapter(
    private val imageHelper: ImageLoader
) : PagedListAdapter<UiComicStrip, CardsAdapter.ViewHolder>(DiffCallback()) {

    var comicActionListener: CardsAdapterActionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_comic_strip, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comicStrip = getItem(position)
        if (comicStrip != null) {
            holder.run {
                titleTextView.text = comicStrip.title
                subtitleTextView.text = comicStrip.altText
                infoTextView.text = comicStrip.subtitle
                favouriteImageView.apply {
                    setFavourite(comicStrip.isFavourite)
                    setOnClickListener {
                        val isFavourite = !comicStrip.isFavourite
                        comicStrip.isFavourite = isFavourite
                        setFavourite(isFavourite)
                        comicActionListener?.onItemFavouriteToggled(
                            comicStrip.number,
                            isFavourite
                        )
                    }
                }
                imageHelper.loadGlideIntoImageView(comicStrip.imageLink, holder.comicImageView)
                comicImageView.setOnClickListener { view ->
                    comicActionListener?.onImageClicked(view, comicStrip.imageLink)
                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.title_text)
        val subtitleTextView: TextView = view.findViewById(R.id.subtitle_text)
        val infoTextView: TextView = view.findViewById(R.id.info_text)
        val favouriteImageView: FavouriteImageView = view.findViewById(R.id.icon_favourite)
        val comicImageView: ImageView = view.findViewById(R.id.image_view)
    }

    class DiffCallback : DiffUtil.ItemCallback<UiComicStrip>() {
        override fun areItemsTheSame(oldItem: UiComicStrip, newItem: UiComicStrip): Boolean {
            return oldItem.number == newItem.number
        }

        override fun areContentsTheSame(oldItem: UiComicStrip, newItem: UiComicStrip): Boolean {
            return oldItem.title == newItem.title &&
                oldItem.subtitle == newItem.subtitle &&
                oldItem.imageLink == newItem.imageLink &&
                oldItem.shareLink == newItem.shareLink &&
                oldItem.altText == newItem.altText
        }
    }
}
