package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.NumberFormat;

import javax.xml.datatype.Duration;

public class MainActivity extends AppCompatActivity {

    private final int MAX_VALUE = 20;
    private final int PRODUCT_PRICE = 5;
    private final int WHIPPED_CREAM_PRICE = 1;
    private final int CHOCOLATE_PRICE = 2;
    private int quantity = 1;
    private int orderTotal = 0;
    private int whippedCreamValue = 0;
    private int chocolateValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the quantity and order price
        displayQuantity(quantity);
        orderTotal = calculatePrice();
        displayPrice(orderTotal);
    }

    /**
     * Displays the input quantity of the order in the Quantity TextView
     *
     * @param number is the quantity of the order
     */
    private void displayQuantity(int number) {
        TextView quantityTextView;
        quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(number));
    }

    /**
     * Increments the quantity and order price and displays them on the screen
     * when the increment button is clicked.
     *
     * @param view is the increment button
     */
    public void incrementQuantity(View view) {

        //enable decrement button and order button if quantity is at 1
        if (quantity == 1) {
            View decrementView = findViewById(R.id.decrement_button);
            decrementView.setEnabled(true);
        }

        //update the quantity
        displayQuantity(++quantity);

        //disable the increment button if quantity is at maximum value
        if (quantity == MAX_VALUE) {
            View incrementView = findViewById(R.id.increment_button);
            incrementView.setEnabled(false);
        }

        //update the price of the order
        orderTotal = calculatePrice();
        displayPrice(orderTotal);
    }

    /**
     * Decrements the quantity and order price and displays them on the screen
     * when the decrement button is clicked.
     *
     * @param view is the minus button
     */
    public void decrementQuantity(View view) {

        //enable increment button if quantity is less than the maximum quantity for an order
        if (quantity == MAX_VALUE) {
            View incrementView = findViewById(R.id.increment_button);
            incrementView.setEnabled(true);
        }

        displayQuantity(--quantity);

        //disable decrement button and order button if the quantity is 1
        if (quantity == 1) {
            view.setEnabled(false);
        }

        //update the price of the order
        orderTotal = calculatePrice();
        displayPrice(orderTotal);
    }

    /**
     * Formats an input price and displays it in the Order Summary TextView
     *
     * @param number is the price
     */
    private void displayPrice(int number) {
        TextView orderSummaryTextView;
        orderSummaryTextView = findViewById(R.id.total_price_text_view);
        orderSummaryTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    /**
     * Creates a summary of the customer's order, including the name, quantity, total,
     * and message for the order.
     *
     * @param customerName    is the name of the customer
     * @param hasWhippedCream is whether the order has whipped cream
     * @param hasChocolate    is whether the order has chocolate
     * @return is the order summary message
     */
    private String createOrderSummary(String customerName, boolean hasWhippedCream, boolean hasChocolate) {

        //Add the customer name
        String orderSummary = getResources().getString(R.string.name_text) + ": " + customerName + "\n";

        //add whipped cream if requested
        orderSummary += getResources().getString(R.string.whipped_cream_text) + ": ";

        if (hasWhippedCream) {
            orderSummary += getResources().getString(R.string.topping_yes) + "\n";
        } else {
            orderSummary += getResources().getString(R.string.topping_no) + "\n";
        }

        //add chocolate if requested
        orderSummary += getResources().getString(R.string.chocolate__text) + ": ";

        if (hasChocolate) {
            orderSummary += getResources().getString(R.string.topping_yes) + "\n";
        } else {
            orderSummary += getResources().getString(R.string.topping_no) + "\n";
        }

        // Add quantity and total price to the order message
        orderSummary += getResources().getString(R.string.quantity_text) + ": " + quantity + "\n";
        orderSummary += getResources().getString(R.string.total_text) + ": " + (NumberFormat.getCurrencyInstance()).format(orderTotal) + "\n";
        orderSummary += getResources().getString(R.string.order_message);

        return orderSummary;
    }

    /**
     * This method adjusts the price of the order when a topping checkbox is checked
     *
     * @param view is the clicked checkbox
     */
    public void checkboxClicked(View view) {

        CheckBox checkBox = findViewById(view.getId());
        int checkedValue = 1;

        // Check to see if the value is unchecked to determine whether a value should be
        // added or subtracted from the
        if (!checkBox.isChecked()) {
            checkedValue = 0;
        }

        // add respective topping values
        if (checkBox.getId() == R.id.whipped_cream_checkbox) {
            whippedCreamValue = checkedValue * WHIPPED_CREAM_PRICE;
        } else {
            chocolateValue = checkedValue * CHOCOLATE_PRICE;
        }
        orderTotal = calculatePrice();
        displayPrice(orderTotal);
    }

    /**
     * Calculates the total price of the order
     *
     * @return total order price
     */
    private int calculatePrice() {
        return quantity * (PRODUCT_PRICE + whippedCreamValue + chocolateValue);
    }

    /**
     * Validates an input customer name by checking to see if
     */
    private boolean isValidName(String inputName) {

        //strip out spaces and tabs
        inputName = inputName.replaceAll("\\p{Blank}", "");

        //if the string was empty or comprised of blanks spaces, return
        if (inputName.isEmpty()) {
            return false;
        }

        //if the name is at least one unicode letter (excluding punctuation)
        return inputName.matches("[\\p{L}]+");
    }

    /**
     * Opens up an email app and composes and email with the input subject and message body
     *
     * @param emailSubject is the subject of the email
     * @param emailBody    is the message body of the email
     */
    private void sendEmail(String emailSubject, String emailBody) {

        //initialize the email Intent
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        //if the data can be applied to an app then run it
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }

    /**
     * Retrieves the order data, calculates the price of the order and displays it
     *
     * @param view is the clicked submit button
     */
    public void submitOrder(View view) {

        //Get customer's name
        EditText customerNameEditText = findViewById(R.id.customer_name_edit_text);
        String customerName = (customerNameEditText.getText()).toString();

        //Show error Toast if a customer's name isn't valid
        if (!isValidName(customerName)) {
            //Initialize the custom Toast layout
            View customToastLayout = getLayoutInflater().inflate(R.layout.custom_toast,
                                        (ViewGroup) findViewById(R.id.custom_toast_container));
            String toastMessage = getResources().getString(R.string.toast_name_error);
            ((TextView) customToastLayout.findViewById(R.id.toast_text)).setText(toastMessage);

            //Show the custom Toast
            Toast customToast = new Toast(this);
            //customToast.setGravity(Gravity.CENTER_VERTICAL,0,0);
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(customToastLayout);
            customToast.show();


            return;
        }

        //Get Checkbox data
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        //Email the order summary message
        String emailSubject = getResources().getString(R.string.email_subject);
        String emailBody = createOrderSummary(customerName, hasWhippedCream, hasChocolate);
        sendEmail(emailSubject, emailBody);
    }
}
