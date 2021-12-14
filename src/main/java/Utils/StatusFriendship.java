package Utils;

public enum StatusFriendship {
        ACCEPT("Accepted"),
        DECLINE("Declined"),
        WAIT("Waiting...");
private final String status;
private StatusFriendship(String status) {
        this.status = status;
        }

public String getStatus() {
        return status;
        }
}