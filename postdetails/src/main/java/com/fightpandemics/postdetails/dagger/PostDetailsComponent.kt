package com.fightpandemics.postdetails.dagger

import com.fightpandemics.core.dagger.scope.ActivityScope
import com.fightpandemics.postdetails.ui.PostDetailsFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [PostDetailsModule::class
        /*, CreatePostViewModelModule::class*/
    ]
)
interface PostDetailsComponent {

    // Factory that is used to create instances of the PostDetailsComponent subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): PostDetailsComponent
    }

    fun inject(postDetailsFragment: PostDetailsFragment)
}