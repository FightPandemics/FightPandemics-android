package com.fightpandemics.utils.webviewer

import java.net.MalformedURLException
import java.net.URL
import kotlin.jvm.Throws

object UrlUtils {
    @JvmStatic
    @Throws(MalformedURLException::class)
    fun getHost(url: String?): String {
        return URL(url).host
    }
}
