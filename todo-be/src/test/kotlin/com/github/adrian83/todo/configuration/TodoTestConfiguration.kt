package com.github.adrian83.todo.configuration

import org.springframework.boot.test.context.TestConfiguration
import com.github.adrian83.todo.domain.user.model.User
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetailsService
import com.github.adrian83.todo.security.TodoUserDetails
import org.springframework.security.provisioning.InMemoryUserDetailsManager


@TestConfiguration
class TodoTestConfiguration {
	
	companion object {
		const val JOHNS_EMAIL = "johndoe@somedomain.com"
		const val SANDRAS_EMAIL = "sandrakowalsky@somedomain.com"
		
		const val USER_DETAILS_BEAN = "userDetailsService"
    }
	
	@Bean(name=[USER_DETAILS_BEAN])
    @Primary
    fun  userDetailsService(): UserDetailsService {
				
		val john = User(1L, TodoTestConfiguration.JOHNS_EMAIL, "fgsgterwg")
		val johnsDetails = TodoUserDetails(john)
		
		val sandra = User(1L, TodoTestConfiguration.SANDRAS_EMAIL, "ertg34ger")
		val sandrasDetails = TodoUserDetails(sandra)

        return InMemoryUserDetailsManager(listOf(johnsDetails, sandrasDetails));
    }

}