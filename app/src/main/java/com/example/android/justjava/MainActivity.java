package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private final int MAX_VALUE = 10;
    private int quantity = 1;
    private int productPrice = 5;
    private int orderTotal = 0;
    private int whippedCreamPrice = 1;
    private int chocolatePrice = 2;
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
     * Displays the quantity of the order in the Quantity TextView
     *
     * @param number
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
        orderSummaryTextView = findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

    /**
     * Displays an input message in the Order Summary TextView
     *
     * @param message is the string message
     */
    private void displayMessage(String message) {
        TextView priceTextView;
        priceTextView = findViewById(R.id.order_summary_text_view);
        priceTextView.setText(message);
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
        String orderSummary = "Name: " + customerName + "\n";

        //add whipped cream if requested
        orderSummary += "Whipped Cream: ";

        if (hasWhippedCream) {
            orderSummary += "Yes\n";
        } else {
            orderSummary += "No\n";
        }

        //add chocolate if requested
        orderSummary += "Chocolate: ";

        if (hasChocolate) {
            orderSummary += "Yes\n";
        } else {
            orderSummary += "No\n";
        }

        // Add quantity and total price to the order message
        orderSummary += "Quantity: " + quantity + "\n";
        orderSummary += "Total: " + (NumberFormat.getCurrencyInstance()).format(orderTotal) + "\n";
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
            whippedCreamValue = checkedValue *= whippedCreamPrice;
        } else {
            chocolateValue = checkedValue *= chocolatePrice;
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
        return quantity * (productPrice + whippedCreamValue + chocolateValue);
    }

    /**
     * Validates an input customer name by checking to see if
     */
    private boolean isValidName(String inputName) {

        //strip out spaces and tabs
        inputName = inputName.replaceAll("\\s", "");

        //if the name is at least one letter
        if (inputName.matches("[a-zA-Z][a-zA-Z\\-]*")) {
            return true;
        }

        return false;
    }

    /**
     * Opens up an email app and composes and email with the input subject and message body
     * @param emailSubject is the subject of the email
     * @param emailBody is the message body of the email
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
            String toastMessage = getResources().getString(R.string.toast_name_error);
            (Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT)).show();
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
