package com.fightpandemics.core.data.model.post

import com.fightpandemics.core.data.model.posts.ExternalLinks

data class PostRequest(
    val content: String?,
    val expireAt: Any?,
    val externalLinks: ExternalLinks?,
    val language: List<String>?,
    val objective: String?,
    val title: String?,
    val types: List<String>?,
    val visibility: String?
)

/*
* {
  "content": "string",
  "expireAt": "day",
  "externalLinks": {
    "appStore": "string",
    "email": "user@example.com",
    "playStore": "string",
    "website": "string"
  },
  "language": [
    "string"
  ],
  "objective": "request",
  "title": "string",
  "types": [
    "Business"
  ],
  "visibility": "city"
}
* */
