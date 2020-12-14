package com.fightpandemics.createpost.data.model

import com.fightpandemics.core.data.model.posts.ExternalLinks

data class CreatePostRequest(
    var actorId: String?,
    var content: String?,
    var expireAt: Any?,
    var externalLinks: ExternalLinks?,
    var language: List<String>?,
    var objective: String?,
    var title: String?,
    var types: List<String>?,
    var visibility: String?
)