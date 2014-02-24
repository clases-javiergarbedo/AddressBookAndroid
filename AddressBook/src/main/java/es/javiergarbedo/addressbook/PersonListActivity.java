package es.javiergarbedo.addressbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PersonListActivity extends FragmentActivity
        implements PersonListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        if (findViewById(R.id.person_detail_container) != null) {
            mTwoPane = true;

            ((PersonListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.person_list))
                    .setActivateOnItemClick(true);
        }

    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(PersonDetailFragment.ARG_ITEM_ID, id);
            PersonDetailFragment fragment = new PersonDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.person_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, PersonDetailActivity.class);
            detailIntent.putExtra(PersonDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
