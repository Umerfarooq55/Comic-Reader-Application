package com.example.shortcut.ui.fragment.comics.comics

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.shortcut.R
import com.example.shortcut.fragment.comics.comics.binder.ComicsViewBinder
import com.example.shortcut.model.AppDialog
import com.example.shortcut.ui.dialog.DialogFactory
import com.example.shortcut.ui.fragment.comics.comics.binder.ViewActionCallbacks
import com.example.shortcut.ui.fragment.comics.comics.viewmodel.ComicsViewModel
import com.example.shortcut.ui.fragment.comics.comics.viewmodel.ComicsViewModelFactory
import com.example.shortcut.ui.fragment.comics.model.ComicAction
import com.example.shortcut.ui.image.ImageLoader
import com.example.shortcut.ui.intent.IntentDispatcher
import com.example.shortcut.ui.view.base.extensionTintMenuItems

import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

internal class ComicsFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = ComicsFragment()
    }

    @Inject
    lateinit var viewModelFactory: ComicsViewModelFactory
    @Inject
    lateinit var intentDispatcher: IntentDispatcher
    @Inject
    lateinit var imageHelper: ImageLoader
    @Inject
    lateinit var dialogFactory: DialogFactory

    private val viewModel: ComicsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ComicsViewModel::class.java)
    }

    private lateinit var binder: ComicsViewBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_comics, menu)
        menu.extensionTintMenuItems(requireContext())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_refresh -> {
                resetAndLoadData()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comics, container, false)
        binder = ComicsViewBinder(view, imageHelper)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binder.callbacks = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binder.callbacks = object : ViewActionCallbacks {
            override fun onErrorShown() {
                viewModel.clearError()
            }

            override fun toggleFavourite(comicStripId: Int, isFavourite: Boolean) {
                viewModel.toggleFavourite(comicStripId, isFavourite)
            }
        }
        binder.initialiseRecyclerAdapter()
        setupObservers()
        viewModel.loadComic(0)
    }

    private fun setupObservers() {
        viewModel.run {
            pagedItems.observe(viewLifecycleOwner, Observer(binder::setAdapterContent))
            actionLiveData.observe(viewLifecycleOwner, Observer(this@ComicsFragment::processAction))
            lastLoadedIndex.observe(viewLifecycleOwner, Observer(viewModel::loadComic))
        }
    }

    private fun processAction(action: ComicAction) {
        when (action) {
            is ComicAction.ShowContent -> binder.showContent()
            is ComicAction.ShowError -> binder.onError(action.uiError)
            is ComicAction.ShowDialog -> showDialog(action.appDialog)
            else -> {
                // NOOP
            }
        }
    }

    private fun showDialog(appDialog: AppDialog?) {
        when (appDialog) {

            else -> {
                // NOOP
            }
        }
    }

    private fun resetAndLoadData() {
        binder.resetPosition()
        viewModel.refresh()
    }
}
