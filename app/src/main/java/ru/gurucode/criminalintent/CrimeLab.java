package ru.gurucode.criminalintent;

import android.content.Context;

import java.util.List;
import java.util.UUID;

/**
 * Created by Leon on 17.10.2017.
 */

class CrimeLab {
    private static CrimeLab sCrimeLab = null;

    private List<Crime> mCrimes;

    public static CrimeLab getInstance(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {

    }


    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for (Crime crime: mCrimes){
            if(crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }
}
