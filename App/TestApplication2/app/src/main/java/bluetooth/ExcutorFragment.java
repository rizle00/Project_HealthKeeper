package bluetooth;

import android.nfc.Tag;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.testapplication2.R;
import com.example.testapplication2.databinding.FragmentExcutorBinding;
import org.jetbrains.annotations.NotNull;

public class ExcutorFragment extends Fragment {
    private static final String Tag = ExcutorFragment.class.getSimpleName();

private MainViewModel viewModel;
private FragmentExcutorBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_excutor, container, false);
        binding = FragmentExcutorBinding.bind(view);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()))
                .get(MainViewModel.class);
//        viewModel.progressLiveData.observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//                binding.progressBar.setProgress(integer);
//            }
//        });
        viewModel.progressLiveData.observe(getViewLifecycleOwner(), progress->{
            binding.progressBar.setProgress(progress);
                });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.longTask(
//                        result -> {
//                    if(result instanceof Result.Success){
//                        binding.progressBar.setProgress(((Result.Success<Integer>)result).data);
//                    } else{
////                        Toast.makeText(Tag, ((Result.Error<Integer>)result).exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    }}
                );
            }
        });
    }
}