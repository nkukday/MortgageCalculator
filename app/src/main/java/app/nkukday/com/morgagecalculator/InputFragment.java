/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.nkukday.com.morgagecalculator;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InputFragment extends Fragment implements Validator.ValidationListener,AdapterView.OnItemSelectedListener {
    OnButtonClickListener mCallback;



    private Spinner spinner;
    private static final String[]paths = {"15", "20", "25","30","40"};

    Mortgage calculateMortgage = new Mortgage();

    String terms;
    @NotEmpty
    private EditText housePriceInputField =null;
    @NotEmpty
    private EditText downPaymentAmountInputField =null;
    @NotEmpty
    private EditText annualInterestRateInputField =null;

    @NotEmpty
    private EditText propertyTaxRateInputField =null;


   private Validator validator=null;

    Button btn_calculator=null;


    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnButtonClickListener {
        /** Called by HeadlinesFragment when a list item is selected
         * @param calculateMortgage*/
        public void onCalculateButtonClickListener(Mortgage calculateMortgage);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.input_fragment_ui, container, false);


        spinner = (Spinner) view.findViewById(R.id.terms_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        validator = new Validator(this);
        validator.setValidationListener(this);

        housePriceInputField = ((EditText) view.findViewById(R.id.housePriceInputField));
        downPaymentAmountInputField = ((EditText) view.findViewById(R.id.downPaymentAmountInputField));
        annualInterestRateInputField = ((EditText) view.findViewById(R.id.annualInterestRateInputField));
        spinner = (Spinner) view.findViewById(R.id.terms_spinner);
        propertyTaxRateInputField = ((EditText) view.findViewById(R.id.propertyTaxRateInputField));

        btn_calculator = (Button)view.findViewById(R.id.button_calculate);

        btn_calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validator.validate();
            }
        });
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return view;
    }


    private void calculateMortgage(){

        double totalPropertyTax; double monthlyPropertyTax;
        double monthlyIntRate = 0.0, loanAmount = 0.0, monthlyPayment = 0.0;
        int months = 0;


        calculateMortgage.setLengthOfTerms(Integer.parseInt(spinner.getSelectedItem().toString()));
        calculateMortgage.setHousePrice(Double.parseDouble(housePriceInputField.getText().toString()));
        calculateMortgage.setDownPaymentAmount(Double.parseDouble(downPaymentAmountInputField.getText().toString()));
        calculateMortgage.setAnnualInterestRate(Double.parseDouble(annualInterestRateInputField.getText().toString()));
        calculateMortgage.setPropertyTaxRate(Integer.parseInt(propertyTaxRateInputField.getText().toString()));


        monthlyIntRate = calculateMortgage.getAnnualInterestRate() / (12 * 100);
        months = calculateMortgage.getLengthOfTerms() * 12;
        loanAmount = calculateMortgage.getHousePrice() - calculateMortgage.getDownPaymentAmount();
        totalPropertyTax = calculateMortgage.getHousePrice() * (calculateMortgage.getPropertyTaxRate()/100) * calculateMortgage.getLengthOfTerms();
        monthlyPropertyTax = totalPropertyTax / (calculateMortgage.getLengthOfTerms() * 12);

        monthlyPayment = ((loanAmount * monthlyIntRate) / (1 - Math.pow(1 + monthlyIntRate, -months)));

        Log.e("total property tax", totalPropertyTax + "");
        Log.e("monthly property tax", monthlyPropertyTax + "");

        calculateMortgage.setMonthlyPayment(Math.floor((monthlyPayment + monthlyPropertyTax) * 100) / 100);
        calculateMortgage.setTotalPayment(Math.floor((monthlyPayment * months) * 100) / 100);
        calculateMortgage.setTotalInterestPaid(Math.floor(calculateMortgage.getTotalPayment() - loanAmount));
        calculateMortgage.setTotalPropertyTax(Math.floor((totalPropertyTax * 100) / 100));


        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, ((calculateMortgage.getLengthOfTerms() * 12) - 1));
        calculateMortgage.setPayOffDate(new StringBuilder().append(new SimpleDateFormat("MMM").format(cal.getTime())).append(" ").append(cal.get(Calendar.YEAR)).toString());

    }

    @Override
    public void onStart() {
        super.onStart();


        if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement interface");
        }
    }

    @Override
    public void onValidationSucceeded() {

        calculateMortgage();

        mCallback.onCalculateButtonClickListener(calculateMortgage);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void reset(){

        housePriceInputField.setText(String.format(""));
        downPaymentAmountInputField.setText("");
        annualInterestRateInputField.setText("");
        //lengthOfTermsInputField.setText("");
        propertyTaxRateInputField.setText("");
        spinner.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}