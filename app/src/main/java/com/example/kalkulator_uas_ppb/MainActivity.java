package com.example.kalkulator_uas_ppb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editInput1, editInput2;
    Button btnHitung;
    TextView tvHasil;
    RadioGroup operasiHitung;
    String Operator;
    SharedPreferences preferences;

    private ArrayList<RiwayatHitung> listRiwayat;
    private RecyclerView rvRiwayat;

    RiwayatAdapter adapter;
    int temp=1, id=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
        showArray();
        setupListener();

        if (listRiwayat.size() == 0) {
            id = 1;
        } else {
            id = Integer.parseInt(listRiwayat.get(listRiwayat.size()-1).getId())+1;
        }
    }

    private void setupView() {
        editInput1 = findViewById(R.id.edittext_input1);
        operasiHitung = findViewById(R.id.operasi_hitung);
        editInput2 = findViewById(R.id.edittext_input2);
        btnHitung = findViewById(R.id.btn_hitung);
        tvHasil = findViewById(R.id.tv_hasil);

        preferences = this.getSharedPreferences("hisotry", Context.MODE_PRIVATE);
        rvRiwayat = findViewById(R.id.rv_riwayatHitung);

        listRiwayat = new ArrayList<>();
        temp = preferences.getAll().size()+1;

        adapter = new RiwayatAdapter(listRiwayat, this, preferences);
    }

    public void showArray() {
        Map<String, ?> entries = preferences.getAll();
        for (Map.Entry<String, ?> entry: entries.entrySet()) {
            getArray(entry.getKey(), entry.getValue().toString());
        }
    }

    public void getArray(String no, String rwyt) {
        try {
            rvRiwayat.setAdapter(new RiwayatAdapter(listRiwayat, this, preferences));
            rvRiwayat.setLayoutManager(new LinearLayoutManager(this));
            listRiwayat.add(new RiwayatHitung(no,String.valueOf(rwyt)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Gagal Menambah Array");
        }
    }

    private void setupListener() {
        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    double value1 = Double.parseDouble(editInput1.getText().toString());
                    double value2 = Double.parseDouble(editInput2.getText().toString());
                    tvHasil.setText(
                            value(value1, value2)
                    );
                } else {
                    showPesan("Masukkan Data Dengan Benar");
                }
            }
        });

        operasiHitung.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                Operator = radioButton.getText().toString();
                tvHasil.setText("Hasil");
            }
        });
    }

    private boolean validate() {
        if (editInput1.getText().toString().equals("") || editInput1.getText() == null) {
            return false;
        } else if (editInput2.getText().toString().equals("") || editInput2.getText() == null) {
            return false;
        } else if (Operator == null) {
            return false;
        }

        return true;
    }

    private String value(double value1, double value2) {
        double value = 0;
        String idRwyt = String.valueOf(id);

        if (Operator.equals("Tambah(+)")) {
            value = value1 + value2;
            String riwayat = Double.toString(value1)+ " + " +Double.toString(value2)+ " = " +Double.toString(value);
            saveToShared(idRwyt, riwayat);

        } else if (Operator.equals("Kurang(-)")) {
            value = value1 - value2;
            String riwayat = Double.toString(value1)+ " - " +Double.toString(value2)+ " = " +Double.toString(value);
            saveToShared(idRwyt, riwayat);

        } else if (Operator.equals("Kali(x)")) {
            value = value1 * value2;
            String riwayat = Double.toString(value1)+ " x " +Double.toString(value2)+ " = " +Double.toString(value);
            saveToShared(idRwyt, riwayat);

        } else if (Operator.equals("Bagi(:)")) {
            value = value1 / value2;
            String riwayat = Double.toString(value1)+ " : " +Double.toString(value2)+ " = " +Double.toString(value);
            saveToShared(idRwyt, riwayat);
        }
        id++;

        // Menampilkan Histori Dari Data Yang Paling Baru
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvRiwayat.setLayoutManager(linearLayoutManager);

        return String.valueOf(value);
    }

    private void showPesan(String pesan) {
        Toast.makeText(getApplicationContext(), pesan, Toast.LENGTH_SHORT).show();
    }

    public void saveToShared(String id, String hasil) {
        try {
            preferences.edit().putString(id, hasil).apply();
            String value = preferences.getString(id,"");
            getArray(id, value);
            temp++;
            this.id++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}