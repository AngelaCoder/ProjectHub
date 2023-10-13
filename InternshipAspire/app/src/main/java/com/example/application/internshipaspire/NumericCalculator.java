package com.example.application.internshipaspire;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NumericCalculator extends AppCompatActivity {

    boolean isFirstInput = true;
    int resultNumber = 0;
    char operator = '+';

    final String CLEAR_INPUT_TEXT = "0";

    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numeric_calculator);
        resultText = findViewById(R.id.result_text);
    }

    public void buttonClick(View view) {
        Button getButton = findViewById(view.getId());

        if (view.getId() == R.id.all_clear_button) {
            isFirstInput = true;
            resultNumber = 0;
            operator = '+';
            resultText.setTextColor(0xFF666666);
            resultText.setText(CLEAR_INPUT_TEXT);
        }

        if (view.getId() == R.id.clear_entry_button) {
            isFirstInput = true;
            resultText.setTextColor(0xFF666666);
            resultText.setText(CLEAR_INPUT_TEXT);
        }

        if (view.getId() == R.id.back_space_button) {
            if (resultText.getText().toString().length() > 1) {
                String getResultText = resultText.getText().toString();
                String subString = getResultText.substring(0, getResultText.length() - 1);
                resultText.setText(subString);
            } else {
                resultText.setTextColor(0xFF666666);
                resultText.setText(CLEAR_INPUT_TEXT);
                isFirstInput = true;
            }
        }

        if (view.getId() == R.id.decimal_button) {
            Log.e("buttonClick" ,  getButton.getText().toString()+" is clicked");

        }

        if (view.getId() == R.id.num_0_button ||
                view.getId() == R.id.num_1_button ||
                view.getId() == R.id.num_2_button ||
                view.getId() == R.id.num_3_button ||
                view.getId() == R.id.num_4_button ||
                view.getId() == R.id.num_5_button ||
                view.getId() == R.id.num_6_button ||
                view.getId() == R.id.num_7_button ||
                view.getId() == R.id.num_8_button ||
                view.getId() == R.id.num_9_button) {

            if (isFirstInput) {
                resultText.setTextColor(0xFF000000);
                resultText.setText(getButton.getText().toString());
                isFirstInput = false;
            } else {
                if (resultText.getText().toString().equals("0")) {
                    Toast.makeText(getApplicationContext(), "No integer starts with 0. Please press AC to try again", Toast.LENGTH_SHORT).show();
                    resultText.setText(CLEAR_INPUT_TEXT);
                    return;
                } else {
                    resultText.append(getButton.getText().toString());
                }
            }
        }


        if (view.getId() == R.id.addition_button || view.getId() == R.id.subtraction_button ||
                view.getId() == R.id.division_button || view.getId() == R.id.multiply_button) {

            if (!isFirstInput) {

                calculateResult();
            }

            operator = getButton.getText().toString().charAt(0); // Update operator
            isFirstInput = true;
        }

        if (view.getId() == R.id.result_button) {
            calculateResult();
        }
    }

    private void calculateResult() {

        /*
        The calculateResult method was added to handle the calculation of results
        based on the current operator and user input.

         */

        try {
            int lastNum = Integer.parseInt(resultText.getText().toString());
            if (operator == '+') {
                resultNumber = resultNumber + lastNum;
            } else if (operator == '-') {
                resultNumber = resultNumber - lastNum;
            } else if (operator == '/') {
                resultNumber = resultNumber / lastNum;
            } else if (operator == '*') {
                resultNumber = resultNumber * lastNum;
            }
            resultText.setText(String.valueOf(resultNumber));
            isFirstInput = true;
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
    }
}