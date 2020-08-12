package com.fightpandemics.ui.home.requests

interface RequestsContract {

    interface Presenter {
        fun prepareData()
    }

    interface View {
        fun setContent(text: String)
    }
}