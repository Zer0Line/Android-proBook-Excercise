package ru.gurucode.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        //Наследники сами определят метод createFragment и он вернет
        //нужный Fragment
        if (fragment == null) {
            fragment = createFragment();
        /*Транзакции используются для добавления, удаления, присоединения,
        замены по списку.*/
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        }
    }
}
