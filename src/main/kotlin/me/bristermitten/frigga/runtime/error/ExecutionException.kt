package me.bristermitten.frigga.runtime.error

import me.bristermitten.frigga.runtime.data.Position

class ExecutionException(exception: Throwable, at: Position) :
    Exception("Exception at ${at.line}:${at.column}", exception) {
}
