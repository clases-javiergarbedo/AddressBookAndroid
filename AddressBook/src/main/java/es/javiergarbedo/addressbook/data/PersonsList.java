package es.javiergarbedo.addressbook.data;

import java.util.ArrayList;

public class PersonsList {

    private static ArrayList<Person> personsList;

    public static ArrayList<Person> getPersonsList() {
        return personsList;
    }

    public static void setPersonsList(ArrayList<Person> personsList) {
        PersonsList.personsList = personsList;
    }

}
