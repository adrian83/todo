package com.github.adrian83.todo.configuration

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.MessageSource
import org.springframework.test.context.junit4.SpringRunner
import java.util.Locale

@RunWith(SpringRunner::class)
@SpringBootTest
class BaseConfigTest() {

    @Autowired private lateinit var messageSource: MessageSource

    @Test
    fun shouldCreateMessageSource() {
        // given
        var key = "validation.login.email.empty"
        var params: Array<Any> = emptyArray()
        var locale = Locale.ENGLISH

        var expectedMsg = "Email cannot be empty"

        // when
        var msg = messageSource.getMessage(key, params, locale)

        // then
        Assert.assertEquals(expectedMsg, msg)
    }
}
