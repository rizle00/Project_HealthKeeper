package bluetooth;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.example.testapplication2.App;
import org.jetbrains.annotations.NotNull;

public class MainViewModel extends AndroidViewModel {

    private final NumberRepository repository;
    public MainViewModel(@NotNull Application application) {
        super(application);
        repository = new NumberRepository(
                ((App) application).main
                ((App) application).main
        );
    }

    public void longTask(RepositoryCallback<Integer> callback){
        repository.longTask(callback);
    }
}
