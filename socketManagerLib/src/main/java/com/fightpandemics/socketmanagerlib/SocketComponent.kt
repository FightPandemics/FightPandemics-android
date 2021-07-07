package com.fightpandemics.socketmanagerlib

import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Ack
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

/**
 * Read Carefully
 * 1. This class is the backend, that handle all realtime messaging and notification
 * 2. Frontend Implementation should listen to its events. and update WebSocketStateView accordings
 * 3. Make sure forceRoomUpdate is implemented or assigned a lambda.
 * */
class SocketComponentBackend {
    private var _socket : Socket
    private var _gson : Gson
    private var _eventBus : EventBus

    constructor(url : String){
        _socket = IO.socket(url)
        _gson = Gson()
        _eventBus = EventBus.getDefault()
    }

    constructor(socket: Socket, gson : Gson, eventBus : EventBus){
        _socket = socket
        _gson = gson
        _eventBus = eventBus
    }

    /**
     * This property is a function that is in charge of a situation if a user is blocked
     * */
    var forceRoomUpdate = fun(_: String){
        throw UninitializedPropertyAccessException("You must implement a forceUpdateListener. " +
                "<instance>.forceRoomUpdate = <your lambda function>")
    }
    fun stop() {
        _socket.close()
    }

    fun getEventBus() : EventBus{
        return _eventBus
    }
    fun getSocket() : Socket{
        return _socket
    }


    /**
     * This Function posts IDENTIFY_SUCCESS and IDENTIFY_ERROR Events,
     *  passes a nothing
     * respectively
     * */
    fun identify(organisationId : String, oldRoomId : String, cb : wsCallBack<Boolean>) {
        val jsonObject = JSONObject()
        jsonObject.put("organisationId", organisationId)
        _socket.emit(IDENTIFY, jsonObject, Ack{
            if(it.isEmpty()) return@Ack
            val responseJSONObject = it[0] as JSONObject
            val jsondata = responseJSONObject.toString()
            val response = _gson.fromJson(jsondata, Response::class.java)
            if(response.code == 200){
                getUserRooms()
                getNotifications()
                joinRoom(JoinRoom(oldRoomId))
                _eventBus.post(WSEvent(wsEventsTypes.IDENTIFY_SUCCESS))
                cb.onSuccess(true)
            } else {
                _eventBus.post(WSEvent(wsEventsTypes.IDENTIFY_ERROR))
                cb.onError(Error(response.message))
            }
        })
    }

    /**
     * This Functions sends messsage to Server. onSuccess is called and passed true or false
     * if the request is successful or error respectively
     * @param messageData
     * @param callBack
     *
     * */

    fun sendMessage(messageData : Message, callBack: wsCallBack<Boolean>) {
        val jsonedMssg = _gson.toJson(messageData)
        _socket.emit(SEND_MESSAGE, JSONObject(jsonedMssg), Ack{
            if(it.isEmpty()) return@Ack
            val responseJSONObject = it[0] as JSONObject
            val jsondata = responseJSONObject.toString()
            val response = _gson.fromJson(jsondata, Response::class.java)
            if(response.code == 200){
                callBack.onSuccess(true)
            } else {
                callBack.onSuccess(false)
            }
        })
    }

    fun deleteMessage(messageId : String){
        _socket.emit(DELETE_MESSAGE, messageId)
    }

    fun editMessage(data : Message){
        val jsonedMssg = _gson.toJson(data)
        _socket.emit(EDIT_MESSAGE, JSONObject(jsonedMssg))
    }

    /**
     * This Function posts JOIN_ROOM_SUCCESS and JOIN_ROOM_ERROR Events,
     *  passes a payload of JsonElement type from Gson library and null
     * respectively
     * */
    fun joinRoom(data : JoinRoom, cb : wsCallBack<JsonElement?>? = null){
        val jsoned = _gson.toJson(data)
        _socket.emit(JOIN_ROOM, JSONObject(jsoned), Ack {
            if (it.isEmpty()) return@Ack // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val response = _gson.fromJson(jsonobj.toString(), Response::class.java)
            if(response.code == 200){
                _eventBus.post(WSEvent(wsEventsTypes.JOIN_ROOM_SUCCESS, response.data))
                cb?.onSuccess?.invoke(response.data)
            } else {
                _eventBus.post(WSEvent(wsEventsTypes.JOIN_ROOM_ERROR))
                cb?.onError?.invoke(Error(response.message))
            }
        })
    }

    fun leaveAllRooms(wsCallBack: wsCallBack<JsonElement?>? = null) {
        joinRoom(JoinRoom(), wsCallBack)
    }

    /**
     * This Function posts GET_ROOMS_SUCCESS and GET_ROOMS_ERROR Events,
     * passes a payload of JsonElement type from Gson library and null respectively
     * */
    fun getUserRooms(cb : wsCallBack<JsonElement?>? = null){
        _socket.emit(GET_USER_THREADS, null, Ack{
            if (it.isEmpty()) return@Ack // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val response = _gson.fromJson(jsonobj.toString(), Response::class.java)
            if(response.code == 200){
                _eventBus.post(WSEvent(wsEventsTypes.GET_ROOMS_SUCCESS, response.data))
                cb?.onSuccess?.invoke(response.data)
            } else {
                _eventBus.post(WSEvent(wsEventsTypes.GET_ROOMS_ERROR))
                cb?.onError?.invoke(Error(response.message))
            }
        })
    }

    /**
     * This Function posts GET_MESSAGE_HISTORY and GET_MESSAGE_HISTORY_ERROR Events,
     * passes a payload of JsonElement type from Gson library and null respectively
     * */
    fun getChatLog(data : GetChatLog, cb: wsCallBack<JsonElement?>? = null) {
        val jsonedMssg = _gson.toJson(data)
        _socket.emit(GET_CHAT_LOG, JSONObject(jsonedMssg), Ack {
            if (it.isEmpty()) return@Ack // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val response = _gson.fromJson(jsonobj.toString(), Response::class.java)
            if(response.code == 200){
                _eventBus.post(WSEvent(wsEventsTypes.GET_MESSAGES_HISTORY, response.data))
                cb?.onSuccess?.invoke(response.data)
            } else {
                _eventBus.post(WSEvent(wsEventsTypes.GET_MESSAGES_HISTORY_ERROR))
                cb?.onError?.invoke(Error(response.message))
            }
        })
    }

    /**
     * This Function posts GET_MORE_MESSAGE_HISTORY events
     * passes a payload of JsonElement type from Gson library
     * */
    fun loadMore(data : LoadMore, cb: wsCallBack<Boolean>? = null){
        val jsonedMssg = _gson.toJson(data)
        _socket.emit(GET_CHAT_LOG_MORE, JSONObject(jsonedMssg), Ack{
            if (it.isEmpty()) return@Ack // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val response = _gson.fromJson(jsonobj.toString(), Response::class.java)
            if(response.code == 200){
                _eventBus.post(WSEvent(wsEventsTypes.GET_MORE_MESSAGES_HISTORY, response.data))
                cb?.onSuccess?.invoke(true)
            } else cb?.onSuccess?.invoke(false)
        })
    }


    fun getUserStatus(userId : String, cb: wsCallBack<JsonElement?>? = null) {
        _socket.emit(GET_USER_STATUS, userId, Ack {
            if (it.isEmpty()) return@Ack // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val response = _gson.fromJson(jsonobj.toString(), Response::class.java)
            if(response.code == 200){
                cb?.onSuccess?.invoke(response.data)
            } else cb?.onError?.invoke(Error(response.message))
        })
    }

    fun blockThread(threadId : String, cb: wsCallBack<JsonElement?>? = null){
        val jsondata = JSONObject()
        jsondata.put("threadId", threadId)
        jsondata.put("newStatus", "blocked")
        _socket.emit(UPDATE_THREAD_STATUS, jsondata, Ack {
            if (it.isEmpty()) return@Ack // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val response = _gson.fromJson(jsonobj.toString(), Response::class.java)
            if(response.code == 200){
                joinRoom(JoinRoom(threadId), cb)
            } else cb?.onError?.invoke(Error(response.message))
        })
    }

    fun archiveThread(threadId : String, cb: wsCallBack<JsonElement?>? = null) {
        val jsondata = JSONObject()
        jsondata.put("threadId", threadId)
        jsondata.put("newStatus", "archived")
        _socket.emit(UPDATE_THREAD_STATUS, jsondata, Ack {
            if (it.isEmpty()) return@Ack // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val response = _gson.fromJson(jsonobj.toString(), Response::class.java)
            if(response.code == 200){
                joinRoom(JoinRoom(threadId))
                leaveAllRooms(cb)
            } else cb?.onError?.invoke(Error(response.message))
        })
    }

    fun ignoreThread(threadId : String, cb: wsCallBack<JsonElement?>? = null){
        val jsondata = JSONObject()
        jsondata.put("threadId", threadId)
        jsondata.put("newStatus", "ignored")
        _socket.emit(UPDATE_THREAD_STATUS, jsondata, Ack {
            if (it.isEmpty()) return@Ack // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val response = _gson.fromJson(jsonobj.toString(), Response::class.java)
            if(response.code == 200){
                joinRoom(JoinRoom(threadId))
                leaveAllRooms(cb)
            } else cb?.onError?.invoke(Error(response.message))
        })
    }

    fun postShared(data : PostShared){
        val jsonedMssg = _gson.toJson(data)
        _socket.emit(POST_SHARED, JSONObject(jsonedMssg))
    }

    fun getNotifications() {
        _socket.emit(GET_NOTIFICATIONS, null, Ack{
            if (it.isEmpty()) return@Ack // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val response = _gson.fromJson(jsonobj.toString(), Response::class.java)
            if(response.code == 200){
                _eventBus.post(WSEvent(wsEventsTypes.GET_NOTIFICATIONS_SUCCESS, response.data))
            }
        })
    }

    fun markNotificationsAsRead(notificationId : String) {
        val json = JSONObject()
        json.put("notificationId", notificationId)
        _socket.emit(MARK_NOTIFICATIONS_AS_READ, json)
    }

    fun clearNotification(){
        _socket.emit(CLEAR_NOTIFICATION)
    }

    fun clearAllNotification(){
        _socket.emit(CLEAR_ALL_NOTIFICATIONS)
    }

    fun activateAllListeners() {
        _socket.on(MESSAGE_RECEIVED, Emitter.Listener {
            if (it.isEmpty()) return@Listener // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val message = _gson.fromJson(jsonobj.toString(), Message::class.java)
            _eventBus.post(WSEvent(wsEventsTypes.MESSAGE_RECEIVED, message))
            _eventBus.post(WSEvent(wsEventsTypes.SET_LAST_MESSAGE, message))
        })

        _socket.on(MESSAGE_DELETED, Emitter.Listener {
            if (it.isEmpty()) return@Listener // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val message = _gson.fromJson(jsonobj.toString(), Message::class.java)
            _eventBus.post(WSEvent(wsEventsTypes.MESSAGE_DELETED, message))
        })

        _socket.on(MESSAGE_EDITED, Emitter.Listener {
            if (it.isEmpty()) return@Listener // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val message = _gson.fromJson(jsonobj.toString(), Message::class.java)
            _eventBus.post(WSEvent(wsEventsTypes.MESSAGE_EDITED, message))
        })

        _socket.on(NEW_MESSAGE_NOTIFICATION, Emitter.Listener {
            if (it.isEmpty()) return@Listener // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val message = _gson.fromJson(jsonobj.toString(), Message::class.java)
            _eventBus.post(WSEvent(wsEventsTypes.MESSAGE_RECEIVED, arrayOf(message, true)))
        })

        _socket.on(USER_STATUS_UPDATE, Emitter.Listener {
            if (it.isEmpty()) return@Listener // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val data = _gson.fromJson(jsonobj.toString(), UserStatusUpdate::class.java)
            _eventBus.post(WSEvent(wsEventsTypes.MESSAGE_RECEIVED, data))
        })

        _socket.on(FORCE_ROOM_UPDATE, Emitter.Listener {
            if (it.isEmpty()) return@Listener
            forceRoomUpdate(it[0] as String)
            getUserRooms()
        })

        _socket.on(NEW_NOTIFICATION, Emitter.Listener {
            if (it.isEmpty()) return@Listener // I hate nonsense!!
            val jsonobj = it[0] as JSONObject
            val data = _gson.fromJson(jsonobj.toString(), Notification::class.java)
            _eventBus.post(WSEvent(wsEventsTypes.NEW_NOTIFICATION, data))
        })
    }


    companion object {
        fun buider() : SocketComponentBackendBuilder{
            return SocketComponentBackendBuilder()
        }
        const val SEND_MESSAGE = "SEND_MESSAGE"
        const val DELETE_MESSAGE = "DELETE_MESSAGE"
        const val EDIT_MESSAGE = "EDIT_MESSAGE"
        const val JOIN_ROOM = "JOIN_ROOM"
        const val GET_USER_THREADS = "GET_USER_THREADS"
        const val GET_CHAT_LOG = "GET_CHAT_LOG"
        const val GET_CHAT_LOG_MORE = "GET_CHAT_LOG_MORE"
        const val GET_USER_STATUS = "GET_USER_STATUS"
        const val UPDATE_THREAD_STATUS = "UPDATE_THREAD_STATUS"
        const val POST_SHARED = "POST_SHARED"
        const val GET_NOTIFICATIONS = "GET_NOTIFICATIONS"
        const val MARK_NOTIFICATIONS_AS_READ = "MARK_NOTIFICATIONS_AS_READ"
        const val CLEAR_NOTIFICATION = "CLEAR_NOTIFICATION"
        const val CLEAR_ALL_NOTIFICATIONS = "CLEAR_ALL_NOTIFICATIONS"
        const val MESSAGE_RECEIVED = "MESSAGE_RECEIVED"
        const val MESSAGE_DELETED = "MESSAGE_DELETED"
        const val MESSAGE_EDITED = "MESSAGE_EDITED"
        const val NEW_MESSAGE_NOTIFICATION = "NEW_MESSAGE_NOTIFICATION"
        const val USER_STATUS_UPDATE = "USER_STATUS_UPDATE"
        const val FORCE_ROOM_UPDATE = "FORCE_ROOM_UPDATE"
        const val NEW_NOTIFICATION = "NEW_NOTIFICATION"
        const val IDENTIFY = "IDENTIFY"
    }
}

class SocketComponentBackendBuilder {
    private var _url : String = ""
    private var _socket = IO.socket(_url)
    private var _gson = Gson()
    private var _eventBus = EventBus.getDefault()
    private var _forceRoomUpdate : ((String) -> Unit)? = null

    fun setHTTPUrl(url : String) : SocketComponentBackendBuilder{
        _url = url
        return this
    }

    fun setSocket(s : Socket) : SocketComponentBackendBuilder{
        _socket = s
        return this
    }

    fun setGson(g : Gson) : SocketComponentBackendBuilder{
        _gson = g
        return this
    }

    fun setEventBus(ev : EventBus) : SocketComponentBackendBuilder{
        _eventBus = ev
        return this
    }

    fun createForceRoomUpdate(cb : (String) -> Unit){
        _forceRoomUpdate = cb
    }

    fun build() : SocketComponentBackend{
        return SocketComponentBackend(_socket, _gson, _eventBus).apply {
            if(_forceRoomUpdate != null){
                this.forceRoomUpdate = _forceRoomUpdate!!
            }
        }
    }

}