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

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OutputFragment extends Fragment {
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;

    private TextView totalInterest=null;
    private TextView monthlyPayment=null;
    private TextView totalTax = null;
    private TextView payOffDate = null;
    private Mortgage payment=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        View view = inflater.inflate(R.layout.output_fragment_ui, container, false);

        totalInterest = (TextView)view.findViewById(R.id.totalPaymentInputField);
        monthlyPayment = (TextView)view.findViewById(R.id.monthlyPaymentInputField);
        totalTax = (TextView)view.findViewById(R.id.totalPropertyTaxInputField);
        payOffDate = (TextView)view.findViewById(R.id.payOffDateInputField);


        if(payment!=null){
            updateView();
        }
        // Inflate the layout for this fragment
        return view;
    }

    public void updateView(){

        totalInterest.setText(payment.getTotalInterestPaid() + "");
        monthlyPayment.setText(payment.getMonthlyPayment() + "");
        totalTax.setText(payment.getTotalPropertyTax() + "");
        payOffDate.setText(payment.getPayOffDate());

    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateArticleView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateArticleView(mCurrentPosition);
        }
    }

    public void updateArticleView(int position) {
//        TextView article = (TextView) getActivity().findViewById(R.id.article);
//        article.setText(Ipsum.Articles[position]);
//        mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }

    public void setPayment(Mortgage payment) {
        this.payment = payment;
    }

    public void reset(){

        totalInterest.setText(String.format(""));
        monthlyPayment.setText("");
        totalTax.setText("");
        payOffDate.setText("");
    }
}