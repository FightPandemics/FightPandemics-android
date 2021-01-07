package com.fightpandemics.filter.dagger

interface FilterComponentProvider {
    fun provideFilterComponent(): FilterComponent
}