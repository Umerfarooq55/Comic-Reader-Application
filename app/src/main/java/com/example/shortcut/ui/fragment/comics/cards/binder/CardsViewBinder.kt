package com.example.shortcut.ui.fragment.comics.cards.binder

import android.view.View
import android.widget.ViewFlipper
import androidx.paging.PagedList
import com.example.shortcut.R
import com.example.shortcut.ui.error.UiError
import com.example.shortcut.ui.fragment.comics.cards.adapter.CardsAdapter
import com.example.shortcut.ui.fragment.comics.cards.adapter.CardsAdapterActionListener
import com.example.shortcut.ui.image.ImageLoader
import com.example.shortcut.ui.model.UiComicStrip
import com.example.shortcut.ui.view.base.BaseFragmentViewBinder
import com.example.shortcut.ui.view.base.BaseFragmentViewHolder

import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import es.dmoral.toasty.Toasty

private const val TRANSLATION_INTERVAL = 8.0f
private const val INDEX_PROGRESS = 0
private const val INDEX_CONTENT = 1

internal class CardsViewBinder(
    view: View,
    private val imageHelper: ImageLoader
) : BaseFragmentViewBinder<CardsViewBinder.ViewHolder>(view) {

    class ViewHolder(view: View) : BaseFragmentViewHolder(view) {
        val viewFlipper: ViewFlipper = view.findViewById(R.id.cards_view_flipper)
        val cardStackView: CardStackView = view.findViewById(R.id.cards_card_stack_view)
    }

    override val viewHolder = ViewHolder(view)

    private val comicStripAdapter: CardsAdapter by lazy {
        CardsAdapter(imageHelper)
    }
    private val comicStripListener: CardStackListener by lazy {
        ComicsCardStackListener()
    }
    private val comicStripLayoutManager by lazy {
        CardStackLayoutManager(context, comicStripListener).apply {
            setStackFrom(StackFrom.Top)
            setTranslationInterval(TRANSLATION_INTERVAL)
        }
    }

    var callbacks: ViewActionCallbacks? = null

    inner class ComicsCardStackListener : CardStackListener {
        override fun onCardDisappeared(view: View?, position: Int) {
            val item = comicStripAdapter.currentList?.get(position)
            if (item?.number == 1) {
                Toasty.info(context, R.string.message_all_caught_up).show()
            }
        }

        override fun onCardDragging(direction: Direction?, ratio: Float) {}

        override fun onCardSwiped(direction: Direction?) {}

        override fun onCardCanceled() {}

        override fun onCardAppeared(view: View?, position: Int) {
            comicStripAdapter.currentList?.get(position)?.let { comicStrip ->
                callbacks?.onItemAppeared(comicStrip)
            }
        }

        override fun onCardRewound() {}
    }

    fun initialiseCardAdapter() {
        showProgress()
        comicStripAdapter.comicActionListener = object : CardsAdapterActionListener {
            override fun onImageClicked(view: View, imageLink: String) {
                imageHelper.showFrescoImage(view, imageLink, R.dimen.margin_16dp)
            }

            override fun onItemFavouriteToggled(comicStripId: Int, isFavourite: Boolean) {
                callbacks?.toggleFavourite(comicStripId, isFavourite)
            }
        }
        viewHolder.cardStackView.apply {
            adapter = comicStripAdapter
            layoutManager = comicStripLayoutManager
        }
    }

    fun setAdapterContent(pagedList: PagedList<UiComicStrip>) {
        showContent()
        comicStripAdapter.submitList(pagedList)
        if (pagedList.size == 1) {
            callbacks?.onItemAppeared(pagedList.first()!!)
        }
    }

    fun showContent() {
        viewHolder.viewFlipper.displayedChild = INDEX_CONTENT
    }

    private fun showProgress() {
        viewHolder.viewFlipper.displayedChild = INDEX_PROGRESS
    }

    fun onError(uiError: UiError?) {
        if (uiError != null) {
            showContent()
            Toasty.error(context, context.getString(uiError.messageResId)).show()
            callbacks?.onErrorShown()
        }
    }

    fun showGettingLatest() {
        Toasty.info(context, context.getString(R.string.message_reset_data)).show()
    }
}
