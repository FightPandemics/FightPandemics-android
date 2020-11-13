package com.fightpandemics.dagger.scope

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Scope for a feature module.
 */
@Scope
@MustBeDocumented
@Retention(RUNTIME)
annotation class FeatureScope
