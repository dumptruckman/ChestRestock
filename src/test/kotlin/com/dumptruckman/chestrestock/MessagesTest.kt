package com.dumptruckman.chestrestock

import org.junit.Assert.*
import org.junit.Test

class MessagesTest {

    @Test
    fun testBasicMessage() {
        assertEquals("You may only use this command in game!", Messages.IN_GAME_ONLY())
    }
}