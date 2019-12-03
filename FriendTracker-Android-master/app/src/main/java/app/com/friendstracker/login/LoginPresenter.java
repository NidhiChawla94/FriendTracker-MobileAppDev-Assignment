package app.com.friendstracker.login;

public class LoginPresenter  implements LoginContract.Presenter, ProfileModel.AuthListener{
    LoginContract.View loginView;
    ProfileModel profileModel;
    public LoginPresenter(LoginContract.View loginView, ProfileDatabaseReader databaseReader) {
        this.loginView = loginView;
        profileModel = new ProfileModel(databaseReader);
    }
    @Override
    public void onAuthSuccess(String phone) {
        loginView.navigatetoHome(phone);
    }

    @Override
    public void onAuthFailure(int error) {
        if(error == ProfileModel.ACCOUNT_NOT_FOUND){
            loginView.navigatetoCreateAccount();
        }
    }

    @Override
    public void login(String countryCode, String phoneNo) {
        Boolean valid = true;
        if(phoneNo.isEmpty()){
            loginView.showPhoneNoError();
            valid = false;
        }
        if(countryCode.isEmpty()){
            loginView.showCountryCodeError();
            valid = false;
        }
        if(!valid){
            return;
        }
        profileModel.authenticate(countryCode + phoneNo, this);
    }
}
