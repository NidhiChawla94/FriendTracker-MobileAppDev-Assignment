package app.com.friendstracker.login;


public class LoginContract {
    public interface View {
        void authenticationSuccess(String phone);
        void authenticationFailure();
        void navigatetoCreateAccount();
        void navigatetoHome(String phone);
        void showPhoneNoError();
        void showCountryCodeError();
    }
    public interface Presenter {
        void login(String countryCode, String phoneNo);
    }
}
