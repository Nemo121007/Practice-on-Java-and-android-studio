package com.example.converter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import currencyInformation.CurrencyInformation;

public class MainActivity extends AppCompatActivity {
    CurrencyInformation currencyInformation = new CurrencyInformation();

    Spinner leftShortSpinner, leftLongSpinner, rightShortSpinner, rightLongSpinner;
    ImageButton reverceUpButton, imageButton;
    TextView courseTextView, enterTextView, resultTextView, resultText;
    EditText enterField;

    private String leftShortNameValute, leftLongNameValute, rightShortNameValute, rightLongNameValute;
    private Double cource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        //region Инициализация элементов
        leftShortSpinner = findViewById(R.id.leftShortSpinner);
        leftLongSpinner = findViewById(R.id.leftLongSpinner);
        rightShortSpinner = findViewById(R.id.rightShortSpinner);
        rightLongSpinner = findViewById(R.id.rightLongSpinner);

        reverceUpButton = findViewById(R.id.reverceUpButton);
        imageButton = findViewById(R.id.imageButton);

        courseTextView = findViewById(R.id.courseTextView);
        enterTextView = findViewById(R.id.enterTextView);
        resultTextView = findViewById(R.id.resultTextView);
        resultText = findViewById(R.id.resultText);

        enterField = findViewById(R.id.enterField);
        //endregion

        //region Инициализация списков спиннеров
        leftShortSpinner.setAdapter(new ArrayAdapter<>
                        (this,
                        android.R.layout.simple_spinner_item,
                        CurrencyInformation.getListValute()));
        leftShortNameValute = "RUB";
        leftShortSpinner.setSelection(CurrencyInformation.getListValute().indexOf("RUB"));

        leftLongSpinner.setAdapter(new ArrayAdapter<>
                        (this,
                        android.R.layout.simple_spinner_item,
                        CurrencyInformation.getListFullName()));
        leftLongNameValute = CurrencyInformation.getFullNameValute(leftShortNameValute);
        leftLongSpinner.setSelection(leftShortSpinner.getSelectedItemPosition());

        rightShortSpinner.setAdapter(new ArrayAdapter<>
                        (this,
                        android.R.layout.simple_spinner_item,
                        CurrencyInformation.getListValute(leftShortNameValute)));
        rightShortNameValute = rightShortSpinner.getSelectedItem().toString();

        rightLongNameValute = CurrencyInformation.getFullNameValute(rightShortNameValute);
        rightLongSpinner.setAdapter(new ArrayAdapter<>
                        (this,
                        android.R.layout.simple_spinner_item,
                        CurrencyInformation.getListFullName(leftLongNameValute)));
        rightLongSpinner.setSelection(rightShortSpinner.getSelectedItemPosition());

        //Убираем элементы rightShortNameValue из leftShortSpinner и rightShortSpinner
        leftShortSpinner.setAdapter(new ArrayAdapter<>
                        (this,
                        android.R.layout.simple_spinner_item,
                        CurrencyInformation.getListValute(rightShortNameValute)));
        leftShortSpinner.setSelection(CurrencyInformation.getListValute(rightShortNameValute).indexOf(leftShortNameValute));
        leftLongSpinner.setAdapter(new ArrayAdapter<>
                (this,
                        android.R.layout.simple_spinner_item,
                        CurrencyInformation.getListFullName(rightLongNameValute)));
        leftLongSpinner.setSelection(leftShortSpinner.getSelectedItemPosition());
        enterTextView.setText(leftShortNameValute);
        resultTextView.setText(rightShortNameValute);
        cource = CurrencyInformation.getCource(leftShortNameValute, rightShortNameValute);
        courseTextView.setText(String.format("%.4f", cource));
        //endregion

        cource = CurrencyInformation.getCource(leftShortNameValute, rightShortNameValute);

        //region Обработка нажатия на спиннер
        leftShortSpinner.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!leftShortNameValute.equals(leftShortSpinner.getSelectedItem().toString()))
                        {
                            SubstituteValues("leftShortSpinner");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Этот метод вызывается, если ничего не выбрано
                        // Вы можете оставить его пустым, если вам не нужно
                        // выполнять никаких действий в этом случае
                    }
                });

        leftLongSpinner.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!leftLongNameValute.equals(leftLongSpinner.getSelectedItem().toString())) {
                            SubstituteValues("leftLongSpinner");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Этот метод вызывается, если ничего не выбрано
                        // Вы можете оставить его пустым, если вам не нужно
                        // выполнять никаких действий в этом случае
                    }
                });

        rightShortSpinner.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!rightShortNameValute.equals(rightShortSpinner.getSelectedItem().toString())) {
                            SubstituteValues("rightShortSpinner");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Этот метод вызывается, если ничего не выбрано
                        // Вы можете оставить его пустым, если вам не нужно
                        // выполнять никаких действий в этом случае
                    }
                });

        rightLongSpinner.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!rightLongNameValute.equals(rightLongSpinner.getSelectedItem().toString())){
                            SubstituteValues("rightLongSpinner");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Этот метод вызывается, если ничего не выбрано
                        // Вы можете оставить его пустым, если вам не нужно
                        // выполнять никаких действий в этом случае
                    }
                });
        //endregion

        reverceUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReverceValute();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReverceValute();
            }
        });

        enterField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Код, который нужно выполнить перед изменением текста
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Код, который нужно выполнить во время изменения текста
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Код, который нужно выполнить после изменения текста
                CalculateSumm(Double.parseDouble(s.toString().replace(',', '.')));
            }
        });
    }

    private void ReverceValute(){
        String transNameValute = leftShortNameValute;
        leftShortNameValute = rightShortNameValute;
        rightShortNameValute = transNameValute;
        transNameValute = leftLongNameValute;
        leftLongNameValute = rightLongNameValute;
        rightLongNameValute = transNameValute;

        leftShortSpinner.setAdapter(new ArrayAdapter<>
                        (this,
                        android.R.layout.simple_spinner_item,
                        CurrencyInformation.getListValute(rightShortNameValute)));
        leftShortSpinner.setSelection(CurrencyInformation.getListValute(rightShortNameValute).indexOf(leftShortNameValute));

        leftLongSpinner.setAdapter(new ArrayAdapter<>
                (this,
                        android.R.layout.simple_spinner_item,
                        CurrencyInformation.getListFullName(rightLongNameValute)));
        leftLongSpinner.setSelection(CurrencyInformation.getListFullName(rightLongNameValute).indexOf(leftLongNameValute));

        rightShortSpinner.setAdapter(new ArrayAdapter<>
                (this,
                        android.R.layout.simple_spinner_item,
                        CurrencyInformation.getListValute(leftShortNameValute)));
        rightShortSpinner.setSelection(CurrencyInformation.getListValute(leftShortNameValute).indexOf(rightShortNameValute));

        rightLongSpinner.setAdapter(new ArrayAdapter<>
                (this,
                        android.R.layout.simple_spinner_item,
                        CurrencyInformation.getListFullName(leftLongNameValute)));
        rightLongSpinner.setSelection(CurrencyInformation.getListFullName(leftLongNameValute).indexOf(rightLongNameValute));

        cource = 1 / cource;
        courseTextView.setText(String.format("%.4f", cource));

        enterTextView.setText(leftShortNameValute);
        resultTextView.setText(rightShortNameValute);
        String timestr = resultText.getText().toString();
        if (!timestr.equals("")) {
            resultText.setText(enterField.getText());
            enterField.setText(timestr);
        }
    }

    private void SubstituteValues(String nameSpinner){
        switch (nameSpinner){
            case "leftShortSpinner":
            {
                leftShortNameValute = leftShortSpinner.getSelectedItem().toString();
                leftLongNameValute = CurrencyInformation.getFullNameValute(leftShortNameValute);
                leftLongSpinner.setSelection(leftShortSpinner.getSelectedItemPosition());

                rightShortSpinner.setAdapter(new ArrayAdapter<>
                                (this,
                                android.R.layout.simple_spinner_item,
                                CurrencyInformation.getListValute(leftShortNameValute)));
                rightLongSpinner.setAdapter(new ArrayAdapter<>
                                (this,
                                android.R.layout.simple_spinner_item,
                                CurrencyInformation.getListFullName(leftLongNameValute)));

                if (leftShortNameValute.equals(rightShortNameValute))
                {
                    rightShortNameValute = rightShortSpinner.getSelectedItem().toString();
                    rightLongNameValute = CurrencyInformation.getFullNameValute(rightShortNameValute);
                    rightLongSpinner.setSelection(rightShortSpinner.getSelectedItemPosition());
                }
                else {
                    rightShortSpinner.setSelection(CurrencyInformation.getListValute(leftShortNameValute).indexOf(rightShortNameValute));
                    rightLongSpinner.setSelection(rightShortSpinner.getSelectedItemPosition());
                }
                break;
            }
            case "leftLongSpinner":
            {
                leftLongNameValute = leftLongSpinner.getSelectedItem().toString();
                leftShortNameValute = CurrencyInformation.getFullNameValute(leftLongNameValute);
                leftShortSpinner.setSelection(leftLongSpinner.getSelectedItemPosition());

                rightShortSpinner.setAdapter(new ArrayAdapter<>
                        (this,
                                android.R.layout.simple_spinner_item,
                                CurrencyInformation.getListValute(leftShortNameValute)));
                rightLongSpinner.setAdapter(new ArrayAdapter<>
                        (this,
                                android.R.layout.simple_spinner_item,
                                CurrencyInformation.getListFullName(leftLongNameValute)));

                if (leftShortNameValute.equals(rightShortNameValute))
                {
                    rightShortNameValute = rightShortSpinner.getSelectedItem().toString();
                    rightLongNameValute = CurrencyInformation.getFullNameValute(rightShortNameValute);
                    rightLongSpinner.setSelection(rightShortSpinner.getSelectedItemPosition());
                }
                else {
                    rightShortSpinner.setSelection(CurrencyInformation.getListValute(leftShortNameValute).indexOf(rightShortNameValute));
                    rightLongSpinner.setSelection(rightShortSpinner.getSelectedItemPosition());
                }
                break;
            }
            case "rightShortSpinner":
            {
                rightShortNameValute = rightShortSpinner.getSelectedItem().toString();
                rightLongNameValute = CurrencyInformation.getFullNameValute(rightShortNameValute);
                rightLongSpinner.setSelection(rightShortSpinner.getSelectedItemPosition());

                leftShortSpinner.setAdapter(new ArrayAdapter<>
                                (this,
                                android.R.layout.simple_spinner_item,
                                CurrencyInformation.getListValute(rightShortNameValute)
                ));
                leftLongSpinner.setAdapter(new ArrayAdapter<>
                                (this,
                                android.R.layout.simple_spinner_item,
                                CurrencyInformation.getListValute(rightLongNameValute)));

                if (leftShortNameValute.equals(rightShortNameValute))
                {
                    leftShortNameValute = leftShortSpinner.getSelectedItem().toString();
                    leftLongNameValute = CurrencyInformation.getFullNameValute(leftShortNameValute);
                    leftLongSpinner.setSelection(leftShortSpinner.getSelectedItemPosition());
                }
                else {
                    leftShortSpinner.setSelection(CurrencyInformation.getListValute(rightShortNameValute).indexOf(leftShortNameValute));
                    leftLongSpinner.setSelection(leftShortSpinner.getSelectedItemPosition());
                }
                break;
            }
            case "rightLongSpinner":
            {
                rightLongNameValute = rightLongSpinner.getSelectedItem().toString();
                rightShortNameValute = CurrencyInformation.getFullNameValute(rightLongNameValute);
                rightShortSpinner.setSelection(rightLongSpinner.getSelectedItemPosition());

                leftShortSpinner.setAdapter(new ArrayAdapter<>
                        (this,
                                android.R.layout.simple_spinner_item,
                                CurrencyInformation.getListValute(rightShortNameValute)
                        ));
                leftLongSpinner.setAdapter(new ArrayAdapter<>
                        (this,
                                android.R.layout.simple_spinner_item,
                                CurrencyInformation.getListFullName(rightLongNameValute)));

                if (leftShortNameValute.equals(rightShortNameValute))
                {
                    leftShortNameValute = leftShortSpinner.getSelectedItem().toString();
                    leftLongNameValute = CurrencyInformation.getFullNameValute(leftShortNameValute);
                    leftLongSpinner.setSelection(leftShortSpinner.getSelectedItemPosition());
                }
                else {
                    leftShortSpinner.setSelection(CurrencyInformation.getListValute(rightShortNameValute).indexOf(leftShortNameValute));
                    leftLongSpinner.setSelection(leftShortSpinner.getSelectedItemPosition());
                }
                break;
            }

        }
        enterTextView.setText(leftShortNameValute);
        resultTextView.setText(rightShortNameValute);
        cource = CurrencyInformation.getCource(leftShortNameValute, rightShortNameValute);
        courseTextView.setText(String.format("%.4f", cource));
        CalculateSumm(Double.parseDouble(enterField.getText().toString().replace(',', '.')));
    }

    private void CalculateSumm(Double summ){
        resultText.setText(String.format("%.4f", summ / cource));
    }
}