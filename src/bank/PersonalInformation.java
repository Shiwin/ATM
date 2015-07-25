package bank;

import java.util.ArrayList;

public class PersonalInformation {

    private final String pasportSeriesAndNumber;
    private final String name,surname,patronym,gender,telephone,dateOfBorth;

    private ArrayList<String> accountsId;

    /**
     *
     * @param pasportSeriesAndNumber
     * @param name
     * @param surname
     * @param patronym
     * @param gender
     * @param telephone
     * @param dateOfBorth
     */
    public PersonalInformation(String pasportSeriesAndNumber, String name,
                               String surname, String patronym, String gender, String telephone, String dateOfBorth) {
        this.pasportSeriesAndNumber = pasportSeriesAndNumber;
        this.name = name;
        this.surname = surname;
        this.patronym = patronym;
        this.gender = gender;
        this.telephone = telephone;
        this.dateOfBorth = dateOfBorth;
        this.accountsId = new ArrayList();
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
    
    public void addAccountId(String login){
        accountsId.add(login);
    }

    public String getPasportSeriesAndNumber() {
        return pasportSeriesAndNumber;
    }

    public String getGender() {
        return gender;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getDateOfBorth() {
        return dateOfBorth;
    }
}
