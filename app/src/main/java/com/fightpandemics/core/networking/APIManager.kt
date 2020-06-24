package com.fightpandemics.core.networking

import android.content.Context
import com.fightpandemics.core.Variables
import com.fightpandemics.di.component.DaggerNetworkComponent
import com.fightpandemics.di.module.NetworkModule
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import rx.Single
import rx.schedulers.Schedulers
import javax.inject.Inject


class APIManager(context: Context?) {

//    @Inject
//    var apiService: APIService? = null

    /* This constructor is provided so we can set up a SharedPrefsManager with mocks from unit test.
     * At the moment this is not possible to do with Dagger because the Gradle APT plugin doesn't
     * work for the unit test variant, plus Dagger 2 doesn't provide a nice way of overriding
     * modules */
    fun injectDependencies(context: Context?) {
        DaggerNetworkComponent.builder()
            .networkModule(NetworkModule())
            .build()
            .inject(this)
    }

    //Todo - Use the below module pattern for testing
    /*
    open fun authenticate(request: AuthenticationRequest?): Single<Test?>? {
        return apiService.authenticate("ApiKey " + Variables.APP_CLIENT_ID, request)
            .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
    }
     */
    init {
        injectDependencies(context)
    }
}
