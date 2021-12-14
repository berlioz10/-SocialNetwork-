package Domain;

import java.util.List;

public class MessageDTO {
    private String message;
    private String message_reply;
    private String user_from;
    private List<String> users_to;

    public MessageDTO(String message, String message_reply, String user_from, List<String> users_to) {
        this.message = message;
        this.message_reply = message_reply;
        this.user_from = user_from;
        this.users_to = users_to;
    }

    public MessageDTO(String message, String user_from, List<String> users_to) {
        this.message = message;
        this.user_from = user_from;
        this.users_to = users_to;
        message_reply = null;
    }

    public String getMessage_reply() {
        return message_reply;
    }

    public void setMessage_reply(String message_reply) {
        this.message_reply = message_reply;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_from() {
        return user_from;
    }

    public void setUser_from(String user_from) {
        this.user_from = user_from;
    }

    public List<String> getUsers_to() {
        return users_to;
    }

    public void setUsers_to(List<String> users_to) {
        this.users_to = users_to;
    }

    @Override
    public String toString() {
        String s = "Mesaj: \"" + message + "\" de la " + user_from + " la: " +
                users_to.stream().reduce("", (x,y) -> x + ", " + y);
        if(message_reply != null)
            return s + "cu reply la mesajul " + message_reply;
        else
            return s.substring(0, s.length() - 2);
    }
}
