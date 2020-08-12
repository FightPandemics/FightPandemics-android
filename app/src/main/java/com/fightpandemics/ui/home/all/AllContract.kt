package com.fightpandemics.ui.home.all

interface AllContract {
    interface Presenter {
        fun prepareData()
    }

    interface View {
        fun setContent(text: String)
    }
}