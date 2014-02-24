package es.javiergarbedo.addressbook.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.javiergarbedo.addressbook.PersonListActivity;
import es.javiergarbedo.addressbook.PersonListFragment;

public class AddressBookDownloader extends AsyncTask<String, Void, Void> {

    private final String NAMESPACE = null;
    private XmlPullParser parser = Xml.newPullParser();

    private static final String TAG_XML = "address_book";
    private static final String TAG_REGISTRO = "person";
    private ArrayList<Person> listaDatos;
    private Context context;
    private PersonListFragment personListFragment;

    public AddressBookDownloader(Context context, PersonListFragment senderoListFragment) {
        this.context = context;
        this.personListFragment = senderoListFragment;
    }

    private Person leerRegistro() {
        //Crear una variable para cada dato del objeto
        int id = -1;
        String name = "";
        String surnames = "";
        String alias = "";
        String email = "";
        String phoneNumber = "";
        String mobileNumber = "";
        String address = "";
        String postCode = "";
        String city = "";
        String province = "";
        String country = "";
        Date birthDate = null;
        String comments = "";
        String photoFileName = "";
        try {
            parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_REGISTRO);
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String tagName = parser.getName();
                if (tagName.equals("id")) {
                    id = Integer.valueOf(readText(tagName));
                } else if (tagName.equals("name")) {
                    name = readText(tagName);
                } else if (tagName.equals("surnames")) {
                    surnames = readText(tagName);
                } else if (tagName.equals("alias")) {
                    alias = readText(tagName);
                } else if (tagName.equals("email")) {
                    email = readText(tagName);
                } else if (tagName.equals("phone_number")) {
                    phoneNumber = readText(tagName);
                } else if (tagName.equals("mobile_number")) {
                    mobileNumber = readText(tagName);
                } else if (tagName.equals("address")) {
                    address = readText(tagName);
                } else if (tagName.equals("postCode")) {
                    postCode = readText(tagName);
                } else if (tagName.equals("city")) {
                    city = readText(tagName);
                } else if (tagName.equals("province")) {
                    province = readText(tagName);
                } else if (tagName.equals("country")) {
                    country = readText(tagName);
                } else if (tagName.equals("birth_date")) {
                    String strBirthDate = readText(tagName);
                    if(!strBirthDate.equals("null")) {
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            birthDate = dateFormat.parse(strBirthDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (tagName.equals("comments")) {
                    comments = readText(tagName);
                } else if (tagName.equals("photo_file_name")) {
                    photoFileName = readText(tagName);
                } else {
                    skip();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Person person = new Person(id, name, surnames, alias, email, phoneNumber, mobileNumber, address, postCode, city, province, country, birthDate, comments, photoFileName);
        return person;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d(AddressBookDownloader.class.getName(), "Descarga de datos finalizada. Iniciando procesamiento");
        Log.d(AddressBookDownloader.class.getName(), "Nº registros descargados: "+listaDatos.size());
        //Conectar con la BD y recorrer los elementos descargados desde el XML
        AddressBookDBManagerAndroid addressBookDBManagerAndroid = new AddressBookDBManagerAndroid(context);
        for (Person person : listaDatos) {
            //Comprobar si ya existe un elemento con la misma ID
            Log.d(AddressBookDownloader.class.getName(), "Comprobando si exite el id: "+person.getId());
            if (addressBookDBManagerAndroid.getPersonByID(person.getId()) == null) {
                Log.d(AddressBookDownloader.class.getName(), "Id no encontrado. Se insertará el registro");
                //Si no existe, se inserta
                addressBookDBManagerAndroid.insertPerson(person);
            }
        }
        //Mostrar la lista una vez finalizada la descarga
        personListFragment.showPersonList();
    }

    @Override
    protected Void doInBackground(String... urls) {
        Log.d(AddressBookDownloader.class.getName(), "Iniciando descarga de datos en segundo plano");
        InputStream stream = null;
        try {
            Log.d(AddressBookDownloader.class.getName(), "Dirección de descarga: "+urls[0]);
            stream = downloadUrl(urls[0]);
            listaDatos = xmlToList(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }


    private ArrayList xmlToList(InputStream stream) {
        Log.d(AddressBookDownloader.class.getName(), "Iniciando interpretación de datos XML");
        ArrayList list = new ArrayList();
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            parser.nextTag();
            Log.d(AddressBookDownloader.class.getName(), "Primera etiqueta encontrada: "+parser.getName());
            parser.require(XmlPullParser.START_TAG, NAMESPACE, TAG_XML);
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                Log.d(AddressBookDownloader.class.getName(), "Etiqueta encontrada: "+parser.getName());
                if (name.equals(TAG_REGISTRO)) {
                    list.add(leerRegistro());
                } else {
                    skip();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void skip() throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private String readText(String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, tag);
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, NAMESPACE, tag);
        return result;
    }


}
