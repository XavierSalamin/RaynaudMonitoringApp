package fr.hes.raynaudmonitoring.model;

public class UserRequest {

    private String firstname;
    private String lastname;
    private String birthdate;
    private boolean isActivated;

   public UserRequest(String firstname, String lastname, String birthdate){
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.isActivated=false;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public boolean isActivated(){
       return isActivated;
    }
}
