package com.example.Security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claim: TokenClaim
    ):String

}