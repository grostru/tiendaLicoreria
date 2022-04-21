package com.grt.tiendalicoreria.data.firestore

import com.google.firebase.database.Exclude

data class MessageModelDB(@get:Exclude var id: String = "",
                   var message: String = "",
                   var sender: String = "",
                   @get:Exclude var myUid: String = ""){

    @Exclude
    fun isSentByMe(): Boolean = sender.equals(myUid)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageModelDB

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
