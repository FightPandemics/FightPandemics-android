package com.fightpandemics.socketmanagerlib

import com.google.gson.JsonElement

//TODO("fill up the Object properties")
data class Message(val content : String? = null, val authorId : String? = null){
    val _id : String? = null
}

data class wsCallBack<T>(val onSuccess : (data : T) -> Unit, val onError : (Error?) -> Unit)
data class Notification(val _id : Long,
                        val action : String,
                        val contentText : String,
                        val isCleared : Boolean = false)
data class Response(val code : Int = 0, val data : JsonElement? = null, val message : String? = null)
data class Room(val topic : String? = null,
                val lastMessage: Message? = null){
    val _id : String? = null
}
enum class wsEventsTypes{
    IDENTIFY_SUCCESS,
    IDENTIFY_ERROR,
    JOIN_ROOM_SUCCESS,
    JOIN_ROOM_ERROR,
    LEAVE_ALL_ROOMS,
    GET_ROOMS_SUCCESS,
    GET_ROOMS_ERROR,
    MESSAGE_RECEIVED,
    SET_LAST_MESSAGE,
    MESSAGE_DELETED,
    MESSAGE_EDITED,
    GET_MESSAGES_HISTORY,
    GET_MESSAGES_HISTORY_ERROR,
    GET_MORE_MESSAGES_HISTORY,
    USER_STATUS_UPDATE,
    NEW_NOTIFICATION,
    GET_NOTIFICATIONS_SUCCESS,
    LOCAL_NOTIFICATIONS_MARK_AS_READ,
    LOCAL_NOTIFICATION_MARK_AS_CLEARED,
    CLEAR_ALL_LOCAL_NOTIFICATIONS
}
data class WSEvent(val action : wsEventsTypes, val payload : Any? = null)
data class LoadMore(val threadId: String? = null, val skip : Int = 0)
data class GetChatLog(val threadId : String? = null)
data class Participant(val id : String)
data class Thread(val participants : List<Participant>){
    val _id : String? = null
}

data class PostShared(val postId : String, val sharedVia : String)
data class UserStatusUpdate(val id : String? = null, val status : String? = null)
data class JoinRoom(val threadId : String? = null, val receiverId : String? = null)
data class WebSocketStateView(
    val room : Room? = null,
    val rooms : List<Room> = emptyList(),
    val notification: List<Notification> = emptyList(),
    val chatLog: List<Message> = emptyList(),
    val isIdentified : Boolean = false
)
