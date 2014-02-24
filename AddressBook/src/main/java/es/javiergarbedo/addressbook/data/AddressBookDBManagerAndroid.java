package es.javiergarbedo.addressbook.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddressBookDBManagerAndroid {

    private static final String NOMBRE_BD = "db_Address_book";
    private static final int VERSION_BD = 1;
    private SQLiteDatabase dbAddressBook;
    private Context mContext;

    public AddressBookDBManagerAndroid(Context context) {
        mContext = context;
        AddressBookDBOpenHelper addressBookDBOpenHelper = new AddressBookDBOpenHelper(context, NOMBRE_BD, null, VERSION_BD);
        dbAddressBook = addressBookDBOpenHelper.getWritableDatabase();
    }

    public void insertPerson(Person person) {
        //Formato para campos de tipo fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("''yyyy-MM-dd''");
        Date birthDate = person.getBirthDate();
        String birthDateSql = null;
        if(birthDate!=null) {
            birthDateSql = dateFormat.format(birthDate);
        }

        String sql = "INSERT INTO person "
                + "(id, name, surnames, alias, email, phone_number, mobile_number, address, "
                + "post_code, city, province, country, birth_date, comments, photo_file_name) "
                + "VALUES ("
                + "'" + person.getId()+ "', "
                + "'" + person.getName()+ "', "
                + "'" + person.getSurnames()+ "', "
                + "'" + person.getAlias()+ "', "
                + "'" + person.getEmail() + "', "
                + "'" + person.getPhoneNumber()+ "', "
                + "'" + person.getMobileNumber()+ "', "
                + "'" + person.getAddress()+ "', "
                + "'" + person.getPostCode()+ "', "
                + "'" + person.getCity()+ "', "
                + "'" + person.getProvince()+ "', "
                + "'" + person.getCountry()+ "', "
                + birthDateSql + ", "
                + "'" + person.getComments()+ "', "
                + "'" + person.getPhotoFileName()+ "')";
        Log.d(AddressBookDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        dbAddressBook.execSQL(sql);
    }
    
    public ArrayList<Person> getPersonsList() {
        ArrayList<Person> personsList = new ArrayList();
        String sql = "SELECT * FROM person";
        Log.d(AddressBookDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        Cursor rs = dbAddressBook.rawQuery(sql, null);
        while(rs.moveToNext()) {
            int id = rs.getInt(0);
            String name = rs.getString(1);
            String surnames = rs.getString(2);
            String alias = rs.getString(3);
            String email = rs.getString(4);
            String phoneNumber = rs.getString(5);
            String mobileNumber = rs.getString(6);
            String address = rs.getString(7);
            String postCode = rs.getString(8);
            String city = rs.getString(9);
            String province = rs.getString(10);
            String country = rs.getString(11);
            //En SQLite no hay datos de tipo DATE
            Date birthDate = null;
            String strBirthDate = rs.getString(12);
            if(strBirthDate!=null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    birthDate = dateFormat.parse(strBirthDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String comments = rs.getString(13);
            String photoFileName = rs.getString(14);
            Person person = new Person(id, name, surnames, alias, email, phoneNumber, mobileNumber, address, postCode, city, province, country, birthDate, comments, photoFileName);
            personsList.add(person);
        }
        return personsList;
    }
    
    public Person getPersonByID(int personId) {
        Person person = null;

        String sql = "SELECT * FROM person WHERE id="+personId;
        Log.d(AddressBookDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        Cursor rs = dbAddressBook.rawQuery(sql, null);

        if(rs.moveToNext()) {
            int id = rs.getInt(0);
            String name = rs.getString(1);
            String surnames = rs.getString(2);
            String alias = rs.getString(3);
            String email = rs.getString(4);
            String phoneNumber = rs.getString(5);
            String mobileNumber = rs.getString(6);
            String address = rs.getString(7);
            String postCode = rs.getString(8);
            String city = rs.getString(9);
            String province = rs.getString(10);
            String country = rs.getString(11);
            //En SQLite no hay datos de tipo DATE
            Date birthDate = null;
            String strBirthDate = rs.getString(12);
            if(strBirthDate!=null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    birthDate = dateFormat.parse(strBirthDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String comments = rs.getString(13);
            String photoFileName = rs.getString(14);
            person = new Person(id, name, surnames, alias, email, phoneNumber, mobileNumber, address, postCode, city, province, country, birthDate, comments, photoFileName);
        }
        return person;
    }

    public void updatePerson(Person person) {
        //Formato para campos de tipo fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("''yyyy-MM-dd''");
        Date birthDate = person.getBirthDate();
        String birthDateSql = null;
        if(birthDate!=null) {
            birthDateSql = dateFormat.format(birthDate);
        }

        String sql = "UPDATE person SET "
                + "name='" + person.getName()+ "', "
                + "surnames='" + person.getSurnames()+ "', "
                + "alias='" + person.getAlias()+ "', "
                + "email='" + person.getEmail() + "', "
                + "phone_number='" + person.getPhoneNumber()+ "', "
                + "mobile_number='" + person.getMobileNumber()+ "', "
                + "address='" + person.getAddress()+ "', "
                + "post_code='" + person.getPostCode()+ "', "
                + "city='" + person.getCity()+ "', "
                + "province='" + person.getProvince()+ "', "
                + "country='" + person.getCountry()+ "', "
                + "birth_date=" + birthDateSql + ", "
                + "comments='" + person.getComments()+ "', "
                + "photo_file_name='" + person.getPhotoFileName()+ "' "
                + "WHERE id="+person.getId();
        Log.d(AddressBookDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        dbAddressBook.execSQL(sql);
    }
    
    public void deletePersonById(String id) {
        String sql = "DELETE FROM person WHERE id="+id;
        Log.d(AddressBookDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
        dbAddressBook.execSQL(sql);
    }

    public class AddressBookDBOpenHelper extends SQLiteOpenHelper {

        public AddressBookDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE IF NOT EXISTS person ("
                    + "id INT PRIMARY KEY, "
                    + "name VARCHAR(50), "
                    + "surnames VARCHAR(100), "
                    + "alias VARCHAR(50), "
                    + "email VARCHAR(50), "
                    + "phone_number VARCHAR(50), "
                    + "mobile_number VARCHAR(50), "
                    + "address VARCHAR(255), "
                    + "post_code VARCHAR(10), "
                    + "city VARCHAR(50), "
                    + "province VARCHAR(50), "
                    + "country VARCHAR(50), "
                    + "birth_date DATE, "
                    + "comments TEXT, "
                    + "photo_file_name VARCHAR(50))";
            Log.d(AddressBookDBManagerAndroid.class.getName(), "Executing SQL statement: " + sql);
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }


    }
    
}
