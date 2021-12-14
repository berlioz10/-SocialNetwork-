package Domain;

import java.util.ArrayList;
import java.util.List;

public class ConversationDTO {
    private List<MessageDTO> messageList;

    public List<MessageDTO> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageDTO> messageList) {
        this.messageList = messageList;
    }

    public ConversationDTO() {
        messageList = new ArrayList<>();
    }

    public void add(MessageDTO messageDTO) {
        messageList.add(messageDTO);
    }

    @Override
    public String toString() {
        return messageList.stream().map(MessageDTO::toString)
                .reduce("", (x, y) -> x + "\n" + y);
    }
}
