package DTOs;

public class FriendToSuggestDto
{
    private int friendSuggestId;

    private String friendSuggestName;

    private String friendSuggestBio;

    private String friendWith;

    public int getFriendSuggestId() {
        return friendSuggestId;
    }

    public void setFriendSuggestId(int friendSuggestId) {
        this.friendSuggestId = friendSuggestId;
    }

    public String getFriendSuggestName() {
        return friendSuggestName;
    }

    public void setFriendSuggestName(String friendSuggestName) {
        this.friendSuggestName = friendSuggestName;
    }

    public String getFriendSuggestBio() {
        return friendSuggestBio;
    }

    public void setFriendSuggestBio(String friendSuggestBio) {
        this.friendSuggestBio = friendSuggestBio;
    }

    public String getFriendWith() {
        return friendWith;
    }

    public void setFriendWith(String friendWith) {
        this.friendWith = friendWith;
    }
}
