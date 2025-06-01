package bewis09.bewisclient.util

class Result<T, E : Throwable> private constructor(private val value: T? = null, private val error: E? = null) {
    companion object {
        fun <T> success(value: T): Result<T, Nothing> {
            return Result<T, Nothing>(value = value)
        }

        fun <E : Throwable> failure(error: E): Result<Nothing, E> {
            return Result<Nothing, E>(error = error)
        }

        fun <T> catch(block: () -> T): Result<T, Throwable> {
            return try {
                success(block())
            } catch (e: Throwable) {
                failure(e)
            } as Result<T, Throwable>
        }
    }

    override fun toString(): String {
        return if (value != null) "Success(value=$value)" else "Failure(error=$error)"
    }

    fun unwrap(): T {
        if (value == null) {
            throw error ?: Throwable("Unknown error occurred")
        }

        return value
    }

    fun unwrapError(): E {
        if (error == null) {
            throw Throwable("No error to unwrap")
        }

        return error
    }

    fun unwrapOrNull(): T? {
        return value
    }

    fun unwrapErrorOrNull(): E? {
        return error
    }

    fun unwrapOrElse(defaultValue: T): T {
        return value ?: defaultValue
    }

    fun unwrapErrorOrElse(defaultError: E): E {
        return error ?: defaultError
    }

    fun printErrorThenNullable(message: (E) -> String): T? {
        if (value == null) {
            println(message)
        }

        return value
    }

    fun isSuccess(): Boolean {
        return value != null
    }

    fun isFailure(): Boolean {
        return error != null
    }

    fun <R> map(transform: (T) -> R): Result<R, E> {
        return (if (isSuccess()) {
            success(transform(value!!))
        } else {
            failure(error!!)
        }) as Result<R, E>
    }
}