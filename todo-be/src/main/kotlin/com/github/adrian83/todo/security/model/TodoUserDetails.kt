package com.github.adrian83.todo.security

import com.github.adrian83.todo.domain.user.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class TodoUserDetails(val user: User) : UserDetails {

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String? = user.email

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String? = user.passwordHash

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = mutableListOf(SimpleGrantedAuthority("USER"))
}
