package com.fightpandemics.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import com.fightpandemics.result.Result

/**
 * Executes business logic in its execute method and keep posting updates to the result as
 * [Result<R>].
 * Handling an exception (emit [Result.Error] to the result) is the subclasses's responsibility.
 */
abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    /*@ExperimentalCoroutinesApi
    operator fun invoke(parameters: P): Flow<R> {
        return execute(parameters)
            .catch { e -> *//*emit(Result.Error(Exception(e)))*//* }
            .flowOn(coroutineDispatcher)
    }*/

    abstract fun execute(parameters: P): R
}
