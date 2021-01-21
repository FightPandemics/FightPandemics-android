package com.fightpandemics.inbox.dagger

import androidx.lifecycle.ViewModel
import com.fightpandemics.core.utils.ViewModelKey
import com.fightpandemics.inbox.ui.InboxViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class InboxViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(InboxViewModel::class)
    abstract fun bindInboxViewModel(inboxViewModel: InboxViewModel): ViewModel
}
