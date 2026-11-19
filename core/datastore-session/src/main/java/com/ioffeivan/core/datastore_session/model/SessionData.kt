package com.ioffeivan.core.datastore_session.model

import kotlinx.serialization.Serializable

@Serializable
data class SessionData(
    val accessToken: String = "",
    val refreshToken: String = "",
    val username: String? = null,
) {
    override fun toString(): String = "SessionData()"
}
