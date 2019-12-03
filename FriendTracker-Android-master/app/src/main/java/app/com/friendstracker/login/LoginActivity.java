package app.com.friendstracker.login;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import app.com.friendstracker.R;
import app.com.friendstracker.home.HomeActivity;
import app.com.friendstracker.login.create.CreateAccountActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener {

    private EditText countryCodeInput;
    private EditText phoneInput;
    private TextInputLayout phoneInputLayout;
    private LoginPresenter loginPresenter;
    private TextInputLayout countryCodeInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        countryCodeInput = (EditText) findViewById(R.id.country_code);
        phoneInput = (EditText) findViewById(R.id.phone_input);
        phoneInputLayout = (TextInputLayout) findViewById(R.id.phone_input_layout);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        //countryCodeInputLayout = (TextInputLayout) findViewById(R.id.country_code_input_layout);
        ((Button)findViewById(R.id.login_button)).setOnClickListener(this);
        loginPresenter = new LoginPresenter(this, new ProfileDatabaseReader(this));
    }

    @Override
    public void authenticationSuccess(String phone) {

    }

    @Override
    public void authenticationFailure() {

    }
    @Override
    public void navigatetoCreateAccount() {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        intent.putExtra("phone_no",countryCodeInput.getText().toString()+phoneInput.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void navigatetoHome(String phone) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(ProfileModel.PHONE,phone);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        phoneInput.requestFocus();
    }


    @Override
    public void showPhoneNoError() {
        phoneInputLayout.setError(this.getResources().getString(R.string.error_phone_required));
    }

    @Override
    public void showCountryCodeError() {
        countryCodeInput.setError(this.getResources().getString(R.string.error_country_code_required));
    }

    @Override
    public void onClick(View v) {
        loginPresenter.login(countryCodeInput.getText().toString(),phoneInput.getText().toString());
    }
}
