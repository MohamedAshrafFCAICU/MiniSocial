package DTOs;

import Enums.LikeType;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class LikeToReturnDto implements Serializable {

    private  int userId;
    private  String userName;
    private String likeType;



    // Getters and Setters

    public int getUserId() {return userId; }

    public String getUserName() {return userName;}

    public void setUserName(String userName) { this.userName = userName;}

    public void setUserId(int userId) {this.userId = userId;}

    public String getLikeType() {
        return likeType;
    }

    public void setLikeType(String likeType) {
        this.likeType = likeType;
    }
} 