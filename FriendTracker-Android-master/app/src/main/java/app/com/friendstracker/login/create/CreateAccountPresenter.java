package app.com.friendstracker.login.create;

import android.util.Patterns;

import java.util.regex.Pattern;

import app.com.friendstracker.login.LoginContract;
import app.com.friendstracker.login.ProfileDatabaseReader;
import app.com.friendstracker.login.ProfileModel;

/**
 * Created by Anand on 06-08-2017.
 */

public class CreateAccountPresenter implements CreateAccountContract.Presenter, ProfileModel.RegistrationListener {

    CreateAccountContract.View createAccountView;
    ProfileModel profileModel;
    public CreateAccountPresenter(CreateAccountContract.View createAccountView, ProfileDatabaseReader databaseReader) {
        this.createAccountView = createAccountView;
        profileModel = new ProfileModel(databaseReader);
    }

    @Override
    public void createAccount(ProfileModel.Profile profile) {
        Boolean valid = true;
        if(profile.getName().isEmpty()){
            createAccountView.showNameError(ProfileModel.REQUIRED);
            valid = false;
        } else if (!(Pattern.matches("^[\\p{L} .'-]+$", profile.getName()))) {
            createAccountView.showNameError(ProfileModel.INVALID);
            valid = false;
        }

        if(profile.getEmail().isEmpty()){
            createAccountView.showEmailError(ProfileModel.REQUIRED);
            valid = false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(profile.getEmail()).matches()){
            createAccountView.showEmailError(ProfileModel.INVALID);
            valid = false;
        }

        if(profile.getBirthday().isEmpty()){
            createAccountView.showBirtdayError(ProfileModel.REQUIRED);
            valid = false;
        }
        if(profile.getPhoto() == null){
            createAccountView.showPhotoError(ProfileModel.REQUIRED);
            valid = false;
        }
        if(!valid){
            return;
        }
        profileModel.createAccount(profile, this);
    }

    @Override
    public void onCreateFailure(long type) {
        createAccountView.onCreateFailure();
    }

    @Override
    public void onCreateSuccess(String phone) {
        createAccountView.navigatetoHome(phone);
    }
}
