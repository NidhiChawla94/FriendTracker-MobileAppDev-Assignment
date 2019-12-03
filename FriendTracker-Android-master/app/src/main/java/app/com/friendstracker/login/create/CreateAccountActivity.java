package app.com.friendstracker.login.create;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import app.com.friendstracker.R;
import app.com.friendstracker.home.HomeActivity;
import app.com.friendstracker.login.ProfileDatabaseReader;
import app.com.friendstracker.login.ProfileModel;
import app.com.friendstracker.util.ImagePicker;

public class CreateAccountActivity extends AppCompatActivity implements CreateAccountContract.View {

    private EditText birthdayInput;
    private Calendar myCalendar = Calendar.getInstance();
    private CreateAccountPresenter createAccountPresenter;
    private EditText nameInput;
    private EditText emailInput;
    private TextInputLayout nameInputLayout;
    private TextInputLayout birthdayInputLayout;
    private TextInputLayout emailInputLayout;
    private byte[] image = null;
    private static final int PICK_IMAGE_ID = 11;
    private TextView errorLabel;
    private Button chooseImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        nameInput = (EditText)findViewById(R.id.name_input);
        emailInput = (EditText)findViewById(R.id.email_input);
        birthdayInput = (EditText)findViewById(R.id.birthday_input);
        nameInputLayout = (TextInputLayout)findViewById(R.id.name_input_layout);
        emailInputLayout = (TextInputLayout)findViewById(R.id.email_input_layout);
        birthdayInputLayout = (TextInputLayout)findViewById(R.id.birthday_input_layout);
        errorLabel = (TextView)findViewById(R.id.error_label);
        chooseImageButton = (Button)findViewById(R.id.choose_photo);

        birthdayInput.setKeyListener(null);
        createAccountPresenter = new CreateAccountPresenter(this,new ProfileDatabaseReader(this));

        birthdayInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    return;
                }
                showDatePicker();
            }
        });
        birthdayInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        findViewById(R.id.create_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameInputLayout.setErrorEnabled(false);
                nameInputLayout.setError(null);
                emailInputLayout.setErrorEnabled(false);
                emailInputLayout.setError(null);
                birthdayInputLayout.setErrorEnabled(false);
                birthdayInputLayout.setError(null);
                errorLabel.setVisibility(View.INVISIBLE);

                ProfileModel.Profile profile = new ProfileModel.Profile();
                profile.setName(nameInput.getText().toString());
                profile.setEmail(emailInput.getText().toString());
                profile.setBirthday(birthdayInput.getText().toString());
                profile.setPhoto(image);
                profile.setPhone(getIntent().getStringExtra("phone_no"));
                createAccountPresenter.createAccount(profile);
            }
        });

    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(CreateAccountActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateBirthdateLabel();
        }

    };

    private void updateBirthdateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        birthdayInput.setText(sdf.format(myCalendar.getTime()));
    }

    public void onPickImage(View view) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                if(bitmap!=null){
                    this.image = ImagePicker.getBitmapAsByteArray(bitmap);
                    chooseImageButton.setText(getResources().getString(R.string.change_photo));
                    errorLabel.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
    @Override
    public void onCreateSuccess(String username) {

    }

    @Override
    public void onCreateFailure() {
        errorLabel.setText(getResources().getText(R.string.error_create_account));
        errorLabel.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigatetoHome(String phone) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(ProfileModel.PHONE,phone);
        startActivity(intent);
        finish();
    }

    @Override
    public void showNameError(int type) {
        String error = getResources().getString(type== ProfileModel.REQUIRED?R.string.error_name_required:R.string.error_name_invalid);
        nameInputLayout.setErrorEnabled(true);
        nameInputLayout.setError(error);
    }

    @Override
    public void showEmailError(int type) {
        String error = getResources().getString(type== ProfileModel.REQUIRED?R.string.error_email_required:R.string.error_email_invalid);
        emailInputLayout.setErrorEnabled(true);
        emailInputLayout.setError(error);
    }

    @Override
    public void showBirtdayError(int type) {
        String error = getResources().getString(type== ProfileModel.REQUIRED?R.string.error_birthday_required:R.string.error_birthday_invalid);
        birthdayInputLayout.setErrorEnabled(true);
        birthdayInputLayout.setError(error);
    }

    @Override
    public void showPhotoError(int type) {
        errorLabel.setText(getResources().getText(R.string.error_photo_required));
        errorLabel.setVisibility(View.VISIBLE);
    }
}
