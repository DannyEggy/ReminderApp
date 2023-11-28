package tdtu.android.final_project;

public class Reminder {
    private int reminderID;
    private String reminderName;
    private String reminderContent;
    private String reminderTime;
    private String reminderDate;
    private String reminderPlace;
    private int reminderImportant;
    private int reminderDone;

    public Reminder() {
    }

    public Reminder(int reminderID, String reminderName, String reminderContent, String reminderTime, String reminderDate, String reminderPlace, int reminderImportant, int reminderDone) {
        this.reminderID = reminderID;
        this.reminderName = reminderName;
        this.reminderContent = reminderContent;
        this.reminderTime = reminderTime;
        this.reminderDate = reminderDate;
        this.reminderPlace = reminderPlace;
        this.reminderImportant = reminderImportant;
        this.reminderDone = reminderDone;
    }



    public int getReminderID() {
        return reminderID;
    }

    public void setReminderID(int reminderID) {
        this.reminderID = reminderID;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public String getReminderContent() {
        return reminderContent;
    }

    public void setReminderContent(String reminderContent) {
        this.reminderContent = reminderContent;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getReminderPlace() {
        return reminderPlace;
    }

    public void setReminderPlace(String reminderPlace) {
        this.reminderPlace = reminderPlace;
    }

    public int getReminderImportant() {
        return reminderImportant;
    }

    public void setReminderImportant(int reminderImportant) {
        this.reminderImportant = reminderImportant;
    }

    public int getReminderDone() {
        return reminderDone;
    }

    public void setReminderDone(int reminderDone) {
        this.reminderDone = reminderDone;
    }
}
