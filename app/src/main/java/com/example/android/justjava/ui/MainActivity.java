package com.example.android.justjava.ui;
/**
 * IMPORTANT: Make sure you are using the correct package name.
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match teh package name found
 * in the project's AndroidManifest.xml file.
 **/

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.justjava.R;
import com.example.android.justjava.constants.AppCodes;
import com.example.android.justjava.constants.AppConstants;

import java.util.Locale;

import static com.example.android.justjava.constants.AppConstants.ADD_ON_CHOCOLATE;
import static com.example.android.justjava.constants.AppConstants.ADD_ON_WHIPPED_CREAM;
import static com.example.android.justjava.constants.AppConstants.BASE_PRICE_COFFEE;
import static com.example.android.justjava.constants.AppConstants.INIT_COFFEE;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, TextWatcher, View.OnTouchListener, View.OnClickListener {

    private int quantity = INIT_COFFEE;
    private CheckBox cbWhippedCream;
    private CheckBox cbChocolate;
    private TextInputEditText tietName;
    private Button btnIncrement;
    private boolean autoIncrement;
    private boolean autoDecrement;
    private Button btnDecrement;
    private Handler updateHandler = new Handler();
    private TextInputLayout tilName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewsByIds();
        setListeners();
    }

    /**
     * XML Parsing
     * <p>
     * Finds and binds views of XML layout using their Ids
     * <p>
     * Being used inside {@link #onCreate(Bundle)}
     *
     * @since 1.0
     */
    private void findViewsByIds() {
        btnIncrement = findViewById(R.id.btn_increment);
        btnDecrement = findViewById(R.id.btn_decrement);
        cbWhippedCream = findViewById(R.id.cb_whipped_cream);
        cbChocolate = findViewById(R.id.cb_chocolate);
        tilName = findViewById(R.id.til_name);
        tietName = findViewById(R.id.tiet_name);
    }

    private void setListeners() {
        tilName.setErrorEnabled(true);
        btnIncrement.setOnTouchListener(this);
        btnDecrement.setOnTouchListener(this);
        btnIncrement.setOnClickListener(this);
        btnDecrement.setOnClickListener(this);
        cbWhippedCream.setOnCheckedChangeListener(this);
        cbChocolate.setOnCheckedChangeListener(this);
        tietName.addTextChangedListener(this);
    }

    /**
     * This method is called when the plus button is clicked.
     *
     * @since 1.0
     */
    public void increment() {
        if (isMaxCoffeeLimit()) {
            return;
        }
        quantity = quantity + 1;
        display(quantity);
        displayOrderSummary(createOrderSummary());
    }

    /**
     * This method is called when the minus button is clicked.
     *
     * @since 1.0
     */
    public void decrement() {
        if (isMinCoffeeLimit()) {
            return;
        }
        quantity = quantity - 1;
        display(quantity);
        displayOrderSummary(createOrderSummary());
    }

    /**
     * Updates order summary as soon as state of any checkbox gets changed
     * Calls {@link #submitOrder(View)}
     *
     * @since 1.0
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        displayOrderSummary(createOrderSummary());
    }

    /**
     * This method displays the given text on the screen.
     *
     * @since 1.0
     */
    private void displayOrderSummary(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.tv_order_summary);
        orderSummaryTextView.setText(message);
    }

    /**
     * Creates order summary that will include name of customer, total quantities and total price
     * along with thank you tag.
     * Used by {@link #submitOrder(View)} from where this method is being called}
     *
     * @return String An order summary that will be used within {@link #submitOrder(View)}
     * @since 1.0
     */
    public String createOrderSummary() {
        return getString(R.string.label_name)
                + ": "
                + tietName.getText().toString()
                + "\n"
                + getString(R.string.string_added_whipped_cream)
                + " "
                + (cbWhippedCream.isChecked() ? getString(R.string.string_true) : getString(R.string.string_false)) //Localisation support
                + "\n"
                + getString(R.string.string_added_chocolate)
                + " "
                + (cbChocolate.isChecked() ? getString(R.string.string_true) : getString(R.string.string_false)) //Localisation support
                + "\n"
                + getString(R.string.label_quantity_title).substring(0, 1).toUpperCase()
                + getString(R.string.label_quantity_title).substring(1, getString(R.string.label_quantity_title).length())
                + ": "
                + quantity
                + "\n"
                + getString(R.string.string_total_dollar)
                + calculatePrice()
                + "\n"
                + getString(R.string.string_thank_you);
    }

    /**
     * Calculates the price of the order.
     *
     * @return An int presenting total price of the order
     * @since 1.0
     */
    private int calculatePrice() {
        //Base price for one cup of coffee
        int basePrice = BASE_PRICE_COFFEE;

        //Add $1 to base price if the user wants whipped cream
        if (cbWhippedCream.isChecked()) {
            basePrice += ADD_ON_WHIPPED_CREAM;
        }

        //Add $2 to base price if the user wants chocolate
        if (cbChocolate.isChecked()) {
            basePrice += ADD_ON_CHOCOLATE;
        }

        //Calculate the total price by multiplying quantity/ies with base price
        return quantity * basePrice;
    }

    /**
     * This method is called when the order button is clicked.
     *
     * @since 1.0
     */
    public void submitOrder(View view) {
        if (!hasUserName()) {
            showEmptyUserName();
            return;
        }
        composeEmail();
    }

    /**
     * Checks if the name has been entered or not
     * Being used inside {@link #submitOrder(View)}
     *
     * @return true if the edit text field is not empty, otherwise false
     * @since 1.0
     */
    private boolean hasUserName() {
        return !tietName.getText().toString().isEmpty();
    }

    /**
     * Shows toast message to user for empty user name
     * {@link #submitOrder(View)}
     *
     * @since 1.0
     */
    private void showEmptyUserName() {
        tietName.requestFocus();
        tilName.setError(getString(R.string.error_msg_empty_name));
        Toast.makeText(this, getString(R.string.error_msg_empty_name), Toast.LENGTH_SHORT).show();
    }

    /**
     * Opens an application that can handle email and the email will have predefined subject and body from here
     * Being used in {@link #submitOrder(View)}
     *
     * @since 1.0
     */
    public void composeEmail() {
        //An intent having action set for the email
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        //Email related dataUri scope. Only email apps should handle this
        intent.setData(Uri.parse("mailto:"));

        //Subject for email
        intent.putExtra(Intent.EXTRA_SUBJECT,
                String.format("%s %s", getString(R.string.label_msg_email_subject), tietName.getText().toString()));

        //Email body
        intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary());

        //Check if there is any application that can handle email
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, AppCodes.CODE_EMAIL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AppCodes.CODE_EMAIL) {
                resetData();
            }
        }
    }

    /**
     * Clears all input of user and resets data
     * <p>
     * This method will be called after composing email.
     * <p>
     * Being used inside of {@link #onActivityResult(int, int, Intent)}
     *
     * @since 1.0
     */
    private void resetData() {
        quantity = 1;
        display(1);
        displayOrderSummary(createOrderSummary());
    }

    /**
     * This method displays the given quantity value on the screen.
     *
     * @since 1.0
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.format(Locale.US, "%d", number));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() > 0){
            tilName.setError(null);
            tilName.setErrorEnabled(false);
        } else if (s.toString().length() == 0){
            tilName.setErrorEnabled(true);
            tilName.setError(getString(R.string.error_msg_empty_name));
        }
        displayOrderSummary(createOrderSummary());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.btn_increment:
                //We do not want to continue if the quantity has reached to maximum limit.
                if (!isMaxCoffeeLimit()) {
                    //Identifies that the user has just touched the btn_increment
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        btnIncrement.performClick();
                        autoIncrement = true;
                        updateHandler.postDelayed(new QuantityModifier(), AppConstants.DELAY);
                    } else {
                        autoIncrement = false;
                        btnIncrement.setPressed(false);
                    }
                }
                break;

            case R.id.btn_decrement:
                //We do not want to continue if the quantity has reached to minimum limit.
                if (!isMinCoffeeLimit()) {
                    //Identifies that the user has just touched the btn_decrement
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        btnDecrement.performClick();
                        autoDecrement = true;
                        updateHandler.postDelayed(new QuantityModifier(), AppConstants.DELAY);
                    } else {
                        autoDecrement = false;
                        btnDecrement.setPressed(false);
                    }
                }
                break;
        }
        return true;
    }

    /**
     * Using to check whether quantity is below maximum limit before modifying it
     * Being used in {@link #onTouch(View, MotionEvent)} and in {@link #increment()}
     *
     * @return true if the quantity is valid, otherwise false
     * @see QuantityModifier for the additional usage
     * @since 1.0
     */
    public boolean isMaxCoffeeLimit() {
        if (quantity == AppConstants.LIMIT_MAX_COFFEE) {
            autoIncrement = false;
            Toast.makeText(this, getString(R.string.error_msg_limit_max_coffee_reached), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Using to check whether quantity is above minimum limit before modifying it
     * Being used in {@link #onTouch(View, MotionEvent)} and in {@link #decrement()}
     *
     * @return true if the quantity is valid, otherwise false
     * @see QuantityModifier for the additional usage
     * @since 1.0
     */
    public boolean isMinCoffeeLimit() {
        if (quantity == AppConstants.LIMIT_MIN_COFFEE) {
            autoDecrement = false;
            Toast.makeText(this, getString(R.string.error_msg_limit_min_coffee_reached), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_increment:
                increment();
                break;

            case R.id.btn_decrement:
                decrement();
                break;
        }
    }

    /**
     * Causes the Runnable (QuantityModifier) to be added to the message queue, to be run after the specified amount of time elapses.
     * The runnable will be run on the thread to which this handler is attached.
     *
     * @see QuantityModifier for the usage
     * @since 1.0
     */

    public void executeRunnableLoop() {
        updateHandler.postDelayed(new QuantityModifier(), AppConstants.DELAY);
    }

    /*
    * Dedicated thread to update ui
    */
    public class QuantityModifier implements Runnable {

        @Override
        public void run() {
            if (autoIncrement) {
                //We do not want to continue the loop if the quantity has reached to maximum limit.
                if (!isMaxCoffeeLimit()) {
                    increment();
                    executeRunnableLoop();
                }
            } else if (autoDecrement) {
                //We do not want to continue the loop if the quantity has reached to minimum limit.
                if (!isMinCoffeeLimit()) {
                    decrement();
                    executeRunnableLoop();
                }
            }
        }
    }
}
