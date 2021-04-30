/*
 * Developed by Michail Fotiadis.
 * Copyright (c) 2018.
 * All rights reserved.
 */

package com.example.shortcut.data.di

import android.app.Application
import com.example.shortcut.XkcdReaderApplication

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
interface AppComponent :  AndroidInjector<XkcdReaderApplication> {


}
