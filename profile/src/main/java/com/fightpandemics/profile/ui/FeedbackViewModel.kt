package com.fightpandemics.profile.ui

import android.content.res.ColorStateList
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.fightpandemics.dagger.scope.FeatureScope
import com.fightpandemics.profile.R
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.feedback_question1.view.*
import javax.inject.Inject

@FeatureScope
class FeedbackViewModel @Inject constructor() : ViewModel(){ }