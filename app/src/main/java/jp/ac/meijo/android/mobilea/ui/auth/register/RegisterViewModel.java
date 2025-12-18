package jp.ac.meijo.android.mobilea.ui.auth.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterViewModel extends ViewModel {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> success = new MutableLiveData<>();

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getSuccess() { return success; }

    public void register(String email, String password) {
        loading.setValue(true);
        errorMessage.setValue(null);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    loading.setValue(false);
                    success.setValue(true);
                })
                .addOnFailureListener(e -> {
                    loading.setValue(false);
                    errorMessage.setValue(e.getMessage());
                });
    }
}
