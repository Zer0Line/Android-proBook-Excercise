package ru.gurucode.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Leon on 18.10.2017.
 */

//Базовый класс для других Activity с фрагментами
public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        initFragment();
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(
                R.id.fragment_container);

        //Наследники сами определят метод createFragment и он вернет
        //нужный Fragment
        if (fragment == null) {
            fragment = createFragment();
        }

        /*Транзакции используются для добавления, удаления, присоединения,
        замены по списку.*/
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }
}
