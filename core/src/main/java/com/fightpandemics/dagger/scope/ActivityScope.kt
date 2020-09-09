package com.fightpandemics.dagger.scope

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Scope for an Activity runtime.
 */
@Scope
@MustBeDocumented
@Retention(RUNTIME)
annotation class ActivityScope
