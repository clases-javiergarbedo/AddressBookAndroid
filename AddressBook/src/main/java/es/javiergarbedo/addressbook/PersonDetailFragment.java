package es.javiergarbedo.addressbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import es.javiergarbedo.addressbook.data.Person;
import es.javiergarbedo.addressbook.data.PersonsList;

public class PersonDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

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
        }

        return rootView;
    }
}
