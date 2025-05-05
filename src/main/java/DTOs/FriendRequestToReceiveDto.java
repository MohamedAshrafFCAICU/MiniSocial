package DTOs;

public class FriendRequestToReceiveDto
{
    private int friendRequestId;

    private String requestStatus;

    private int senderId;

    private String senderName;

    private String senderBio;



    public int getFriendRequestId() {
        return friendRequestId;
    }

    public void setFriendRequestId(int friendRequestId) {
        this.friendRequestId = friendRequestId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderBio() {
        return senderBio;
    }

    public void setSenderBio(String senderBio) {
        this.senderBio = senderBio;
    }
}
