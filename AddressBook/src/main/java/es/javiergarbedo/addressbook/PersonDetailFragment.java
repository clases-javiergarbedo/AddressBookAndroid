package es.javiergarbedo.addressbook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.javiergarbedo.addressbook.data.Person;
import es.javiergarbedo.addressbook.data.PersonsList;

public class PersonDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private ImageView imageViewPhoto;

//    private DummyContent.DummyItem mItem;
    private Person person;

    public PersonDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            //Obtener la persona que se encuentre en la posición que se ha seleccionado dentro de la lista
            int personIndex = Integer.valueOf(getArguments().getString(ARG_ITEM_ID));
            person = PersonsList.getPersonsList().get(personIndex);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_person_detail, container, false);

//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.person_detail)).setText(mItem.content);
//        }
        if(person != null) {
            ((EditText) rootView.findViewById(R.id.editTextName)).setText(person.getName());
            ((EditText) rootView.findViewById(R.id.editTextSurnames)).setText(person.getSurnames());
            ((EditText) rootView.findViewById(R.id.editTextAlias)).setText(person.getAlias());
            ((EditText) rootView.findViewById(R.id.editTextEmail)).setText(person.getEmail());
            ((EditText) rootView.findViewById(R.id.editTextPhoneNumber)).setText(person.getPhoneNumber());
            ((EditText) rootView.findViewById(R.id.editTextMobileNumber)).setText(person.getMobileNumber());
            ((EditText) rootView.findViewById(R.id.editTextAddress)).setText(person.getAddress());
            ((EditText) rootView.findViewById(R.id.editTextPostCode)).setText(person.getPostCode());
            ((EditText) rootView.findViewById(R.id.editTextCity)).setText(person.getCity());
            ((EditText) rootView.findViewById(R.id.editTextProvince)).setText(person.getProvince());
            ((EditText) rootView.findViewById(R.id.editTextCountry)).setText(person.getCountry());
            ((EditText) rootView.findViewById(R.id.editTextComments)).setText(person.getComments());
            //Dar formato a la fecha para mostrarla
            String strBirthDate = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if(person.getBirthDate()!=null) {
                strBirthDate = dateFormat.format(person.getBirthDate());
            }
            ((EditText) rootView.findViewById(R.id.editTextBirthDate)).setText(strBirthDate);
            //Descargar foto y mostrarla al finalizar la descarga, sólo si hay un nombre de archivo para la imagen
            if(!person.getPhotoFileName().trim().isEmpty() || person.getPhotoFileName()!=null) {
                imageViewPhoto = ((ImageView)rootView.findViewById(R.id.imageViewPhoto));
                ImageDownloader imageDownloader = new ImageDownloader();
                imageDownloader.execute(PersonListFragment.URL_IMAGES + person.getPhotoFileName());
            }
        }

        return rootView;
    }



    private class ImageDownloader extends AsyncTask<String, Void, Void> {

        private Bitmap bitmap;

        @Override
        protected Void doInBackground(String... strings) {
            String urlImage = strings[0];
            bitmap = getImageBitmap(urlImage);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            imageViewPhoto.setImageBitmap(bitmap);
        }

        private Bitmap getImageBitmap(String url) {
            Bitmap bm = null;
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e(ImageDownloader.class.getName(), "Error getting bitmap", e);
            }
            return bm;
        }
    }

}
