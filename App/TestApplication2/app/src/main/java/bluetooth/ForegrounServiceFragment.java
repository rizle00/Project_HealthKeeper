package bluetooth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.testapplication2.R;
import com.example.testapplication2.databinding.FragmentForegrounServiceBinding;
import org.jetbrains.annotations.NotNull;

public class ForegrounServiceFragment extends Fragment {
    private static final String TAG = ForegrounServiceFragment.class.getSimpleName();
    private FragmentForegrounServiceBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foregroun_service, container, false);
        binding = FragmentForegrounServiceBinding.bind(view);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: "+((Newactivity)requireActivity()).myService);
        ((Newactivity)requireActivity()).myService
                .progressLiveData
                .observe(getViewLifecycleOwner(),
                progress->{
            binding.progressBar.setProgress(progress);
                    Log.d(TAG, "onViewCreated: "+progress);
                });
//        binding.button.setOnClickListener(v->{
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
//                requireActivity().startForegroundService(new Intent(requireContext(), MyService.class));
//            }
                ContextCompat.startForegroundService(requireContext(),
                        new Intent(requireContext(), MyService.class));
//        });
    }
}