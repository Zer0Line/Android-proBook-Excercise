package ru.gurucode.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.function.ToDoubleBiFunction;

/**
 * Created by Leon on 18.10.2017.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_crime_list,
                container,
                false);
        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);

        //Без LayoutManager RecyclerView не работает. LayoutManager управляет
        // позиционированием и определяет поведение прокуртки.
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));

        return view;
    }

    //todo: закончил тут. Страница 193.
    //ВХ - view для элемента
    private class CrimeHolder extends RecyclerView.ViewHolder{
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
        }
    }
}
