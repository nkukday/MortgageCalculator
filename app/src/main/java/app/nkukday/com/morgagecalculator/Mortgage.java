package app.nkukday.com.morgagecalculator;

import android.widget.EditText;

import java.util.Date;

/**
 * Created by neerajakukday on 9/21/15.
 */
public class Mortgage {

    private double housePrice;
    private double downPaymentAmount;
    private double annualInterestRate;
    private int lengthOfTerms;
    private double monthlyPayment;
    private double totalPayment;
    private double propertyTaxRate;
    private double totalPropertyTax;
    private double totalInterestPaid;
    private String payOffDate;


    public double getHousePrice() {
        return housePrice;
    }

    public void setHousePrice(double housePrice) {
        this.housePrice = housePrice;
    }

    public double getDownPaymentAmount() {
        return downPaymentAmount;
    }

    public void setDownPaymentAmount(double downPaymentAmount) {
        this.downPaymentAmount = downPaymentAmount;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public int getLengthOfTerms() {
        return lengthOfTerms;
    }

    public void setLengthOfTerms(int lengthOfTerms) {
        this.lengthOfTerms = lengthOfTerms;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public double getPropertyTaxRate() {
        return propertyTaxRate;
    }

    public void setPropertyTaxRate(double propertyTaxRate) {
        this.propertyTaxRate = propertyTaxRate;
    }

    public double getTotalPropertyTax() {
        return totalPropertyTax;
    }

    public void setTotalPropertyTax(double totalPropertyTax) {
        this.totalPropertyTax = totalPropertyTax;
    }

    public String getPayOffDate() {
        return payOffDate;
    }

    public void setPayOffDate(String payOffDate) {
        this.payOffDate = payOffDate;
    }

    public double getTotalInterestPaid() {
        return totalInterestPaid;
    }

    public void setTotalInterestPaid(double totalInterestPaid) {
        this.totalInterestPaid = totalInterestPaid;
    }
}