package com.hugoltsp.spring.boot.starter.jwt.filter.dsl

import com.hugoltsp.spring.boot.starter.jwt.filter.JwtAuthenticationSettings
import com.hugoltsp.spring.boot.starter.jwt.filter.PublicResource
import org.springframework.http.HttpMethod

@DslMarker
private annotation class JwtSettingsDsl

fun settings(block: JwtAuthenticationSettingsBuilder.() -> Unit) = JwtAuthenticationSettingsBuilder()
        .apply(block)
        .build()

@JwtSettingsDsl
class JwtAuthenticationSettingsBuilder {

    var secretKey: String? = null
    private val publicResources = mutableListOf<PublicResource>()

    fun publicResources(block: PublicResources.() -> Unit) = publicResources.addAll(PublicResources().apply(block))

    fun build() = JwtAuthenticationSettings().apply {
        secretKey = this@JwtAuthenticationSettingsBuilder.secretKey
        publicResources.addAll(this@JwtAuthenticationSettingsBuilder.publicResources)
    }

}

@JwtSettingsDsl
class PublicResources : ArrayList<PublicResource>() {

    fun publicResource(block: PublicResourceBuilder.() -> Unit) = add(PublicResourceBuilder().apply(block).build())

}

@JwtSettingsDsl
class PublicResourceBuilder {

    var method: String? = null
    lateinit var urls: String

    fun build() = PublicResource().apply {
        method = this@PublicResourceBuilder.method.let(HttpMethod::resolve)
        urls = this@PublicResourceBuilder.urls.split(",").map(String::trim)
    }

}