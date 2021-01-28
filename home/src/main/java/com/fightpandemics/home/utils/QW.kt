package com.fightpandemics.home.utils

import androidx.lifecycle.Lifecycle.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class QW {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow() // read-only public view

    suspend fun postEvent(event: Event) {
        _events.emit(event) // suspends until subscribers receive it
    }
}
