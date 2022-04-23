
import org.junit.Test

import org.junit.Assert.*

class ChatServiceTest {

    @Test
    fun getUnreadChatsCount() {
        ChatService
        val senderId = 1
        val recipientId = 2
        val text = "text"
        ChatService.sendMessage(senderId, recipientId, text)
        assertEquals(1, ChatService.getUnreadChatsCount(recipientId))
    }

    @Test
    fun getReadMessages() {
        ChatService
        val senderId = 111
        val recipientId = 112
        ChatService.sendMessage(senderId, recipientId, "")
        val result = ChatService.getReadMessages(senderId)
        assertTrue(result[1].isRead)
    }

    @Test
    fun getChats() {
        ChatService
        val ownerId = 1
        val chats = mutableListOf<Chat>(Chat(111, listOf(1), listOf(Message(12,1,"",false))))
        assertEquals(chats, ChatService.getChats(ownerId))
    }

    @Test
    fun sendMessage() {
        ChatService
        val senderId = 1
        val recipientId = 2
        val text = "text"
        ChatService.sendMessage(senderId, recipientId, text)
        assertEquals(1, ChatService.getChats(senderId).size)
    }

    @Test
    fun deleteMessage() {
        ChatService
        val senderId = 1
        val recipientId = 2
        val text = "text"
        val chatId = 1
        val messageId = 1
        ChatService.sendMessage(senderId,recipientId,text)
        ChatService.deleteMessage(chatId, messageId)
        assertTrue(ChatService.getReadMessages(senderId).isEmpty())
    }

    @Test
    fun deleteChat() {
        ChatService
        val senderId = 1
        val recipientId = 2
        val text = "text"
        val chatId = 1
        ChatService.sendMessage(senderId, recipientId, text)
        ChatService.deleteChat(chatId)
        assertTrue(ChatService.getChats(chatId).isEmpty())
    }
}