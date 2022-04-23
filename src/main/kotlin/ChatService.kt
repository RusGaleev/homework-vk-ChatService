data class Chat(
    val id: Int,
    val users: List<Int>,
    val messages: List<Message>
)

data class Message(
    val id: Int,
    val ownerId: Int,
    var text: String,
    var isRead: Boolean
)

object ChatService {
    private val chats = mutableListOf<Chat>()
    val lastIndex = 0

    fun getUnreadChatsCount(ownerId: Int): Int {
        var unreadChatsNumber = 0
        chats.forEachIndexed { index, chat ->
            val lastIndex: Int = chat.messages.lastIndex
            if (chat.messages[lastIndex].ownerId != ownerId || !chat.messages[lastIndex].isRead) {
                unreadChatsNumber++
            }
        }
        return unreadChatsNumber
    }

    fun getReadMessages(id: Int): List<Message> {
        val chat = chats.firstOrNull { it.id == id } ?: return emptyList()
        val updateMessages = chat.messages.map { it.copy(isRead = true) }
        val updatedChat = chat.copy(messages = updateMessages)
        chats.removeIf { updatedChat.id == it.id }
        chats.add(updatedChat)
        return updateMessages
    }

    fun getChats(ownerId: Int): List<Chat> {
        val userChats  = chats.filter { chat ->
            chat.messages.isNotEmpty()
        }
        if (chats.isEmpty()) throw Exception("\"нет сообщений\"")
        return userChats
    }

    fun sendMessage(
        senderId: Int,
        recipientId: Int,
        text: String
    ) {
        val message = Message(
            id = 1,
            ownerId = senderId,
            text = text,
            isRead = false
        )
        val newChat = chats.filter { chat ->
            chat.users.containsAll(listOf(senderId, recipientId))
        }
            .firstOrNull()
            ?.let { chat ->
                chat.copy(messages = chat.messages + message)
            } ?: Chat(
            id = 1,
            users = listOf(senderId, recipientId),
            messages = listOf(message)
        )
        chats.forEachIndexed { index, chat ->
            if (newChat.id == chat.id) {
                chats[index] = newChat
                return@forEachIndexed
            }
        }
    }

    fun deleteMessage(chatId: Int, messageId:Int){
        chats.forEachIndexed { index, chat ->
            if (chat.id == chatId) {
                val updateMessages = chats[index].messages.filter { message ->
                    message.id != messageId
                }
                updateMessages.isEmpty()?: deleteChat(chatId)
                chat.copy(messages = updateMessages)
            }
        }
    }

    fun deleteChat(chatId: Int){
        chats.forEachIndexed { index, chat ->
            if (chat.id == chatId) chats.removeAt(index)
        }
    }
}