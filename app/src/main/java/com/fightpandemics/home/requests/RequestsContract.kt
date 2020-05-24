package com.fightpandemics.home.requests

interface RequestsContract {

    interface Presenter {
        fun prepareData()
    }

    interface View {
        fun setContent(text: String)
    }
}