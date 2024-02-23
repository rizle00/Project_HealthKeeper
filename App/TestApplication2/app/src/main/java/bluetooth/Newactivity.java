package bluetooth;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.testapplication2.R;
import com.example.testapplication2.databinding.ActivityNewactivityBinding;

public class Newactivity extends AppCompatActivity {
    private static final String TAG = Newactivity.class.getSimpleName();
    private ActivityNewactivityBinding binding;
   public MyService myService;
    boolean mBound = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//
//        binding.buttonEx.setOnClickListener(v->navController.navigate(R.id.excutorFragment));
//        binding.buttonFore.setOnClickListener(v->navController.navigate(R.id.foregrounServiceFragment));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MyService.class);
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mServiceConnection);
        mBound = false;
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            myService = ((MyService.LocalBinder) service).getService();
            mBound = true;

            ForegrounServiceFragment myFragment = new ForegrounServiceFragment();

            // 프래그먼트 트랜잭션 시작
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // 프래그먼트를 프레임 레이아웃에 추가
            transaction.replace(R.id.fragment_container, myFragment);

            // 트랜잭션 커밋
            transaction.commit();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myService = null;
            mBound = false;
        }
    };
}