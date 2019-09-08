package br.com.bit.chain.charts.di

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import br.com.bit.chain.android.di.ActivityScope
import br.com.bit.chain.android.di.ViewModelKey
import br.com.bit.chain.app.BitApplication
import br.com.bit.chain.charts.data.ChartDataRepositoryImpl

import br.com.bit.chain.charts.data.cache.ChartDataLocalCache
import br.com.bit.chain.charts.data.cache.ChartDataLocalCacheImpl
import br.com.bit.chain.charts.data.service.ChartDataService
import br.com.bit.chain.charts.domain.ChartRepository
import br.com.bit.chain.charts.domain.FetchChartUseCase
import br.com.bit.chain.charts.domain.FetchChartUseCaseImpl
import br.com.bit.chain.charts.presentation.ChartActivityViewModel
import br.com.bit.chain.charts.presentation.State
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.create

@Module(includes = [ViewModelModule::class])
internal class ChartActivityModule {
    @ActivityScope
    @Provides
    fun provideSharedPreferences(app: BitApplication): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(app)

    @ActivityScope
    @Provides
    fun provideRepository(impl: ChartDataRepositoryImpl): ChartRepository = impl

    @ActivityScope
    @Provides
    fun provideRemoteService(retrofit: Retrofit): ChartDataService = retrofit.create()

    @ActivityScope
    @Provides
    fun provideLocalCache(impl: ChartDataLocalCacheImpl): ChartDataLocalCache = impl

    @Provides
    @ActivityScope
    fun provideGson(): Gson {
        return Gson()
    }

    @ActivityScope
    @Provides
    fun provideStateLiveData(): MutableLiveData<State> = MutableLiveData()

    @ActivityScope
    @Provides
    fun provideDisposables(): CompositeDisposable = CompositeDisposable()

    @ActivityScope
    @Provides
    fun provideUseCase(impl: FetchChartUseCaseImpl): FetchChartUseCase = impl
}

@Module
internal abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ChartActivityViewModel::class)
    abstract fun bindsViewModel(viewModel: ChartActivityViewModel): ViewModel
}
