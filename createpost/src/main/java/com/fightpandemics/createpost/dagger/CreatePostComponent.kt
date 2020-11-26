package com.fightpandemics.createpost.dagger

import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.createpost.ui.CreatePostFragment
import dagger.Subcomponent

/**
 * created by Osaigbovo Odiase
 *
 * Component binding injections for the [:createpost] module.
 */
@ActivityScope
@Subcomponent(
    modules = [CreatePostModule::class, CreatePostViewModelModule::class]
)
interface CreatePostComponent {

    // Factory that is used to create instances of the createPostComponent subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): CreatePostComponent
    }

    fun inject(createPostFragment: CreatePostFragment)
}