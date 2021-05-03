package com.example.shortcut.di
import android.app.Application
import com.example.shortcut.XkcdReaderApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuilderModule::class,
        AppModule::class,
        NetModule::class,
        DataModule::class,
        LoaderModule::class
    ]
)
interface AppComponent : AndroidInjector<XkcdReaderApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
