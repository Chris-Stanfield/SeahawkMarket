package edu.uncw.seahawkmarket;

public class Users {
    private String profileImageFile;

    public Users(){

    }

    public Users(String profileImageFile){
        this.profileImageFile = profileImageFile;
    }

    public String getProfileImageFile() {
        return profileImageFile;
    }

    public void setProfileImageFile(String profileImageFile) {
        this.profileImageFile = profileImageFile;
    }
}
