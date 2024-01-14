package core.util

import platform.Foundation.NSRunLoop
import platform.Foundation.NSThread
import platform.Foundation.performBlock

inline fun <T1> mainContinuation(
    noinline block: (T1) -> Unit,
): (T1) -> Unit = { arg1 ->
    if (NSThread.isMainThread()) {
        block.invoke(arg1)
    } else {
        NSRunLoop.mainRunLoop.performBlock {
            block.invoke(arg1)
        }
    }
}

inline fun <T1, T2> mainContinuation(
    noinline block: (T1, T2) -> Unit,
): (T1, T2) -> Unit = { arg1, arg2 ->
    if (NSThread.isMainThread()) {
        block.invoke(arg1, arg2)
    } else {
        NSRunLoop.mainRunLoop.performBlock {
            block.invoke(arg1, arg2)
        }
    }
}
