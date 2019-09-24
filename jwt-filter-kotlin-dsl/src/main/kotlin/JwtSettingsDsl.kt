package com.hugoltsp.spring.boot.starter.jwt.filter.dsl

import com.hugoltsp.spring.boot.starter.jwt.filter.JwtAuthenticationSettings
import com.hugoltsp.spring.boot.starter.jwt.filter.PublicResource
import org.springframework.http.HttpMethod

@DslMarker
private annotation class JwtSettingsDsl

fun settings(block: JwtAuthenticationSettingsBuilder.() -> Unit):
        JwtAuthenticationSettings = JwtAuthenticationSettingsBuilder().apply(block).build()

@JwtSettingsDsl
class JwtAuthenticationSettingsBuilder {

    var secretKey: String = ""
    private val publicResources = mutableListOf<PublicResource>()

    fun publicResources(block: PublicResources.() -> Unit) {
        publicResources.addAll(PublicResources().apply(block))
    }

    fun build(): JwtAuthenticationSettings {
        val jwtAuthenticationSettings = JwtAuthenticationSettings()
        jwtAuthenticationSettings.secretKey = secretKey
        jwtAuthenticationSettings.publicResources.addAll(publicResources)
        return jwtAuthenticationSettings
    }

}

@JwtSettingsDsl
class PublicResources : ArrayList<PublicResource>() {

    fun publicResource(block: PublicResourceBuilder.() -> Unit) {
        add(PublicResourceBuilder().apply(block).build())
    }

}

@JwtSettingsDsl
class PublicResourceBuilder {

    var method: String? = ""

    var urls: String = ""

    fun build(): PublicResource {

        val publicResource = PublicResource()
        publicResource.method = HttpMethod.resolve(method)
        publicResource.urls = urls.split(",").map { it.trim() }
        return publicResource
    }

}