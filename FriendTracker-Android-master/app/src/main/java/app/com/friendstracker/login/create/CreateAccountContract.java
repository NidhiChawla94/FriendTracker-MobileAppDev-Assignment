package app.com.friendstracker.login.create;


import app.com.friendstracker.login.ProfileModel;

public class CreateAccountContract {
    public interface View {
        void onCreateSuccess(String username);
        void onCreateFailure();
        void navigatetoHome(String phone);
        void showNameError(int type);
        void showEmailError(int type);
        void showBirtdayError(int type);
        void showPhotoError(int type);
    }
    public interface Presenter {
        void createAccount(ProfileModel.Profile profile);
    }
}
