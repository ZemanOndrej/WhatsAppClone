package eu.ozeman.whatsappclone.Chat;

import java.util.Objects;

public class Chat {
    private String chatId;

    public Chat(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(chatId, chat.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }
}
