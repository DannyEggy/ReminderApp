package tdtu.android.final_project;

public class User {
    private int userCount;
    private String userName;
    private String mail;
    private String phoneNumber;
    private String address;
    private String job;
    private String account;
    private String password;

    public User(int userCount, String userName, String mail, String phoneNumber, String account, String password) {
        this.userCount = userCount;
        this.userName = userName;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.account = account;
        this.password = password;
        this.address = "";
        this.job ="";
    }

    public User(String account, String password){
        this.userCount = 0;
        this.userName = "";
        this.mail = account;
        this.phoneNumber = "";
        this.account = account;
        this.password = password;
        this.address = "";
        this.job ="";
    }

    public User(int userCount, String userName, String mail, String phoneNumber, String address, String job, String account, String password) {
        this.userCount = userCount;
        this.userName = userName;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.job = job;
        this.account = account;
        this.password = password;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
