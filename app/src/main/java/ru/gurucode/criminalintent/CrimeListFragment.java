package ru.gurucode.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.function.ToDoubleBiFunction;

/**
 * Created by Leon on 18.10.2017.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list,
                container, false);

        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);

        //Без LayoutManager RecyclerView не работает. LayoutManager управляет
        // позиционированием и определяет поведение прокуртки.
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));

        updateUI();

        return view;
    }

    private void updateUI() {
        //ссылка на синглтон CrimeLab с данными
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        //передадим данные адаптеру.
        mAdapter = new CrimeAdapter(crimes);
        //Подключим адаптер к mCrimeRecyclerView.
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    //ВХ - view для элемента. Заполняет поля значениями.
    private class CrimeHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mCrimeSolvedImageView;
        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mCrimeSolvedImageView =
                    (ImageView) itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getmTitle());
            mDateTextView.setText(mCrime.getmDate().toString());
            mCrimeSolvedImageView.setVisibility((mCrime.ismSolved() ?
                    View.VISIBLE : View.GONE));
        }

        //Реализуем View.OnClickListener
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), mCrime.getmTitle(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Адаптер, содержащий VH CrimeHolder
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        /*Вызывается mCrimeRecyclerView, когда нужно новое View*/
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //layoutInflater - используется для создания View
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        //Связь VH и конкретных данных.
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime); //передадим данные в VH
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
