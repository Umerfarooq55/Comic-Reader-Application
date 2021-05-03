package com.example.shortcut.di

import com.example.shortcut.ui.MainActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [FragmentBuilderModule::class])
    abstract fun mainActivity(): MainActivity
}
