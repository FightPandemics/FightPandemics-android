package com.fightpandemics.profile.ui

import android.view.View
import androidx.lifecycle.ViewModel
import com.fightpandemics.dagger.scope.FeatureScope
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.feedback_question1.view.*
import javax.inject.Inject

@FeatureScope
class FeedbackViewModel @Inject constructor() : ViewModel(){

    fun displayActiveBorder(materialCard: MaterialCardView, hasFocus: Boolean){
        if (hasFocus){
            materialCard.vertical_accent.visibility = View.VISIBLE
        }else{
            materialCard.vertical_accent.visibility = View.GONE
        }
    }

}
