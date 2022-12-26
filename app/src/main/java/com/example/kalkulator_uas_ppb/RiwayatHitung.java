package com.example.kalkulator_uas_ppb;

import androidx.annotation.NonNull;

public class RiwayatHitung {

    private String riwayat;
    private String id;

    public String getRiwayat() {
        return riwayat;
    }

    public void setRiwayat(String riwayat) {
        this.riwayat = riwayat;
    }

    public String getId() {
        return id;
    }

    public RiwayatHitung(String id, String riwayat) {
        this.id = id;
        this.riwayat = riwayat;
    }

    @NonNull
    @Override
    public String toString() {
        return riwayat;
    }
}
