package app.nkukday.com.morgagecalculator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    public static final String HOUSE_PRICE = "HOUSE_PRICE";
    public static final String DOWN_PAYMENT_AMOUNT = "DOWN_PAYMENT_AMOUNT";
    public static final String ANNUAL_INTEREST_RATE = "ANNUAL_INTEREST_RATE";
    public static final String MORTGAGE_LOAN_LENGTH = "MORTGAGE_LOAN_LENGTH";
    public static final String PROPERTY_TAX_RATE = "PROPERTY_TAX_RATE";

    public double housePrice;
    public double downPayment;
    public double annualInterestRate;
    public int lengthOfTerm;
    public double propertyTaxRate;

    private EditText housePriceInputField;
    private EditText downPaymentAmountInputField;
    private EditText annualInterestRateInputField;
    private EditText lengthOfMortgageLoanInputField;
    private EditText monthlyPaymentInputField;
    private EditText totalPaymentInputField;
    private EditText propertyTaxRateInputField;
    private EditText totalPropertyTaxInputField;
    private EditText payOffDateInputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            housePrice = 0.0;
            downPayment = 0.0;
            annualInterestRate = 0.0;
            lengthOfTerm = 0;
            propertyTaxRate = 0.0;
        }
        else
        {
            housePrice = savedInstanceState.getDouble(HOUSE_PRICE);
            downPayment = savedInstanceState.getDouble(DOWN_PAYMENT_AMOUNT);
            annualInterestRate = savedInstanceState.getDouble(ANNUAL_INTEREST_RATE);
            lengthOfTerm = savedInstanceState.getInt(MORTGAGE_LOAN_LENGTH);
            propertyTaxRate = savedInstanceState.getDouble(PROPERTY_TAX_RATE);

        }

        housePriceInputField = (EditText)findViewById(R.id.housePriceInputField);
        downPaymentAmountInputField = (EditText)findViewById(R.id.downPaymentAmountInputField);
        annualInterestRateInputField = (EditText)findViewById(R.id.annualInterestRateInputField);
        lengthOfMortgageLoanInputField = (EditText)findViewById(R.id.lengthOfTermsInputField);
        monthlyPaymentInputField = (EditText)findViewById(R.id.monthlyPaymentInputField);
        totalPaymentInputField =(EditText)findViewById(R.id.totalPaymentInputField);
        propertyTaxRateInputField = (EditText)findViewById(R.id.propertyTaxRateInputField);
        totalPropertyTaxInputField = (EditText)findViewById(R.id.totalPropertyTaxInputField);
        payOffDateInputField = (EditText)findViewById(R.id.payOffDateInputField);

        Button calculateButton = (Button)findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(calculateButtonListener);

    }

    public View.OnClickListener calculateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ((InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    housePriceInputField.getWindowToken(), 0);

            calculateMortgage();
        }
    };

    private void calculateMortgage(){

        double totalPropertyTax; double monthlyPropertyTax;
        double monthlyIntRate = 0.0, loanAmount = 0.0, monthlyPayment = 0.0, totalPayment = 0.0;
        int months = 0;

        housePrice = Double.parseDouble(housePriceInputField.getText().toString());
        downPayment = Double.parseDouble(downPaymentAmountInputField.getText().toString());
        annualInterestRate = Double.parseDouble(annualInterestRateInputField.getText().toString());
        lengthOfTerm = Integer.parseInt(lengthOfMortgageLoanInputField.getText().toString());
        propertyTaxRate = Double.parseDouble(propertyTaxRateInputField.getText().toString());

        //put checks for 0 or  out of range values
        if(housePrice != 0.0 && downPayment != 0.0 && annualInterestRate != 0.0 && lengthOfTerm !=0) {

            monthlyIntRate = annualInterestRate / (12 * 100);
            months = lengthOfTerm * 12;
            loanAmount = housePrice - downPayment;
            totalPropertyTax = housePrice * (propertyTaxRate/100) * lengthOfTerm;
            monthlyPropertyTax = totalPropertyTax / (lengthOfTerm * 12);

            //temp = Math.pow(1+ monthlyIntRate,lengthOfTerm*12);
            monthlyPayment = ((loanAmount * monthlyIntRate) / (1 - Math.pow(1 + monthlyIntRate, -months)));
            //monthlyPayment = ((loanAmount * monthlyIntRate * temp)/(temp - 1));

            Log.e("total property tax",totalPropertyTax+"");
            Log.e("monthly property tax",monthlyPropertyTax+"");


            monthlyPayment = monthlyPayment + monthlyPropertyTax;
            monthlyPaymentInputField.setText(String.format("%.02f", monthlyPayment));

            totalPayment = monthlyPayment * months;
            totalPaymentInputField.setText(String.format("%.02f", totalPayment));

            totalPropertyTaxInputField.setText(String.format("%.02f", totalPropertyTax));

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, ((lengthOfTerm * 12) - 1));
            payOffDateInputField.setText(new StringBuilder().append(new SimpleDateFormat("MMM").format(cal.getTime())).append(" ").append(cal.get(Calendar.YEAR)));

        }
        else
        {
            //alert
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(MainActivity.this);

            builder.setTitle(R.string.missingEntries); // title bar string

            // provide an OK button that simply dismisses the dialog
            builder.setPositiveButton(R.string.OK, null);

            // set the message to display
            builder.setMessage(R.string.provideEntries);

            // create AlertDialog from the AlertDialog.Builder
            AlertDialog errorDialog = builder.create();
            errorDialog.show();
        }


    }

    public View.OnClickListener cancelButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // hide the soft keyboard
            ((InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    housePriceInputField.getWindowToken(), 0);

            reset();
        }
    };


    private void reset(){

        housePriceInputField.setText(String.format("0.0"));
        downPaymentAmountInputField.setText("0.0");
        annualInterestRateInputField.setText("0.0");
        lengthOfMortgageLoanInputField.setText("0.0");
        monthlyPaymentInputField.setText("0.0");
        totalPaymentInputField.setText("0.0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putDouble(HOUSE_PRICE, housePrice);
        outState.putDouble(DOWN_PAYMENT_AMOUNT, downPayment);
        outState.putDouble(ANNUAL_INTEREST_RATE, annualInterestRate);
        outState.putInt(MORTGAGE_LOAN_LENGTH, lengthOfTerm);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.reset) {
            reset();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
