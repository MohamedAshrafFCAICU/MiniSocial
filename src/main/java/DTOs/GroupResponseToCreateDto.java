package DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class GroupResponseToCreateDto
{

    @NotNull(message = "Group Request id is required.")
    private int groupRequestId;

    @Pattern(regexp = "(?i)^(approved|rejected)$", message = "Status must be either 'approved' or 'rejected' (case insensitive)")
    private String responseStatus;


    @NotNull(message = "Group Request id is required.")
    public int getGroupRequestId() {
        return groupRequestId;
    }

    public void setGroupRequestId(@NotNull(message = "Group Request id is required.") int groupRequestId) {
        this.groupRequestId = groupRequestId;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }


}
