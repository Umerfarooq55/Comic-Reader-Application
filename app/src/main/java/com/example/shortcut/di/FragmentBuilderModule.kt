package com.example.shortcut.di

import com.example.shortcut.ui.fragment.comics.cards.CardsFragment
import com.example.shortcut.ui.fragment.comics.comics.ComicsFragment
import com.example.shortcut.ui.fragment.favourites.FavouritesFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract fun cardsFragment(): CardsFragment

    @ContributesAndroidInjector
    abstract fun comicsFragment(): ComicsFragment

    @ContributesAndroidInjector
    abstract fun favouritesFragment(): FavouritesFragment
}
