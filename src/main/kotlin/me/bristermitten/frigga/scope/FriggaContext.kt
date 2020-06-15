package me.bristermitten.frigga.scope

class FriggaContext {
    val stack = Stack()

    val scope = ArrayDeque<FriggaScope>().apply {
        add(GlobalScope)
    }
}
