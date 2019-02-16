package eu.ozeman.whatsappclone.Message;

public class Message {
    private String authorName, text, messageId;

    public Message(String authorName, String text, String messageId) {
        this.authorName = authorName;
        this.text = text;
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public String getAuthorName() {
        return authorName;
    }


    public String getMessageId() {
        return messageId;
    }
}
