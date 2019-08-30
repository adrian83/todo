package com.github.adrian83.todo.security

import org.springframework.security.core.userdetails.UserDetails
import com.github.adrian83.todo.domain.user.model.User
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.GrantedAuthority

class TodoUserDetails(val user: User?): UserDetails {
	
	override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = mutableListOf(SimpleGrantedAuthority("USER"))
	

	override fun isEnabled(): Boolean = true

	override fun getUsername(): String? = "john"

	override fun isCredentialsNonExpired(): Boolean = true

	override fun getPassword(): String? = "secret"

	override fun isAccountNonExpired(): Boolean = true

	override fun isAccountNonLocked(): Boolean = true
}