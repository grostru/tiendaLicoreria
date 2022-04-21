package com.grt.tiendalicoreria.ui.chat

import com.grt.tiendalicoreria.domain.model.MessageModel

interface OnChatListener {
    fun deleteMessage(messageModel: MessageModel)
}