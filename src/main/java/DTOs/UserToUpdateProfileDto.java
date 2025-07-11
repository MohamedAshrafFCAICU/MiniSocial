package DTOs;

import jakarta.validation.constraints.*;
import java.io.Serializable;

public class UserToUpdateProfileDto implements Serializable
{
    private String fName;

    private String lName;

    @Email(message = "Invalid Email Format.")
    private String email;

    @Size(min = 8 ,max = 16 , message = "Password Length Between 8 - 16. ")
    private String password;

    @Size(min = 3, max = 100 , message = "Password Length Between 3 - 100.")
    private String bio;


    @Min(value = 12 , message = "Min Age is 12.")
    private int age;

    @Pattern(regexp = "01[0125][0-9]{8}", message = "Invalid Egyptian phone number")
    private String phoneNumber;


    public String getfName() {
        return fName;
    }

    public void setfName( String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio( String bio) {
        this.bio = bio;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public  String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
