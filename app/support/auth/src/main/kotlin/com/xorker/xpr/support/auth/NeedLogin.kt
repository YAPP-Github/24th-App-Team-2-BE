package com.xorker.xpr.support.auth

import org.springframework.security.access.prepost.PreAuthorize

@PreAuthorize("isAuthenticated()")
annotation class NeedLogin
