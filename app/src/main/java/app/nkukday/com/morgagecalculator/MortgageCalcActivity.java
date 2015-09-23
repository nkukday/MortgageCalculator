package app.nkukday.com.morgagecalculator;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by neerajakukday on 9/21/15.
 */

public class MortgageCalcActivity extends Activity
        implements InputFragment.OnButtonClickListener {


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switch_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onCalculateButtonClickListener(Mortgage mortgage) {

        if(findViewById(R.id.frame_layout) != null) {
            OutputFragment outputFragment = new OutputFragment();
            // Create an instance of ExampleFragment
//
//            // In case this activity was started with special instructions from an Intent,
//            // pass the Intent's extras to the fragment as arguments
            outputFragment.setArguments(getIntent().getExtras());
            FragmentManager manager = getFragmentManager();//create an instance of fragment manager
            FragmentTransaction transaction = manager.beginTransaction();//create an instance of Fragment-transaction
            transaction.replace(R.id.frame_layout, outputFragment,"OUTPUT_FRAG");
            //can navigate back to previous fragment
            //transaction.add(R.id.frame_layout, outputFragment, "Frag_output_tag");
            transaction.addToBackStack(null);

            transaction.commit();

            outputFragment.setPayment(mortgage);
        }
        else{

            OutputFragment outputFragment = (OutputFragment) getFragmentManager().findFragmentById(R.id.output_fragment);
            outputFragment.setPayment(mortgage);
            outputFragment.updateView();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.reset) {
            if(findViewById(R.id.frame_layout) != null) {//mobile
                InputFragment inputFragment =(InputFragment) getFragmentManager().findFragmentById(R.id.input_fragment1);
                if(inputFragment!=null)
                {
                    inputFragment.reset();
                    Log.e("input id", id + "");
                }
                    OutputFragment outputFragment = (OutputFragment) getFragmentManager().findFragmentByTag("OUTPUT_FRAG");
                    Log.e("output id", id + "");
                    //outputFragment.reset();
                    if(outputFragment!=null)
                        getFragmentManager().beginTransaction().remove(outputFragment).commit();

            }
            else {//tablet
                InputFragment inputFragment =(InputFragment) getFragmentManager().findFragmentById(R.id.large_input_fragment);
                OutputFragment outputFragment = (OutputFragment) getFragmentManager().findFragmentById(R.id.output_fragment);
                inputFragment.reset();
                outputFragment.reset();



            }
        }

        return super.onOptionsItemSelected(item);
    }

}
