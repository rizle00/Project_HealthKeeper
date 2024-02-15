package com.example.healthkeeper.main;

import static android.content.Intent.getIntent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;


import com.example.healthkeeper.databinding.FragmentHomeBinding;
import com.example.healthkeeper.databinding.FragmentScheduleBinding;
import com.example.healthkeeper.member.GuardianJoinActivity;
import com.example.healthkeeper.member.LoginActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ScheduleFragment extends Fragment {
    FragmentScheduleBinding binding;
    public String fname=null;
    public String str=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        /*return binding.getRoot();*/
        View rootView = binding.getRoot();



        //회원가입 엑티비티에서 이름을 받아옴
        /* Fragment의 생명주기는 Activity의 생명주기에 종속되므로,
         Fragment가 종료되거나 연결이 끊어질 때 getActivity()가 null이 된다.
         이에 대한 예외처리를 통해 사용자에게 알리는 것이 좋다.*/
        if (getActivity() != null) {/* getActivity()가 null이 아닌 경우에만 작업 수행*/
             Intent intent = getActivity().getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("edt_user_name");
            final String userID = intent.getStringExtra("edt_user_id");
            binding.textView3.setText (name + "님의 일정관리" );


            binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    binding.diaryTextView.setVisibility(View.VISIBLE);
                    binding.saveBtn.setVisibility(View.VISIBLE);
                    binding.contextEditText.setVisibility(View.VISIBLE);
                    binding.textView2.setVisibility(View.INVISIBLE);
                    binding.chaBtn.setVisibility(View.INVISIBLE);
                    binding.delBtn.setVisibility(View.INVISIBLE);
                    binding.diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
                    binding.contextEditText.setText("");
                    checkDay(year, month, dayOfMonth, userID);
                }
            });

            binding.saveBtn.setOnClickListener(new View.OnClickListener() {/*저장*/
                @Override
                public void onClick(View view) {
                    saveDiary(fname);
                    str = binding.contextEditText.getText().toString();
                    binding.textView2.setText(str);
                    binding.saveBtn.setVisibility(View.INVISIBLE);
                    binding.chaBtn.setVisibility(View.VISIBLE);
                    binding.delBtn.setVisibility(View.VISIBLE);
                    binding.contextEditText.setVisibility(View.INVISIBLE);
                    binding.textView2.setVisibility(View.VISIBLE);

                }
            });
        } else {
            Toast.makeText(getContext(), "이름검색불가!", Toast.LENGTH_SHORT).show();
        }

        } else {
            // getActivity()가 null일 때 처리!!!
            Toast.makeText(getContext(), "Activity is null!", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }



    /*선택된 날짜에 해당하는 일정을 확인*/
        public void checkDay ( int cYear, int cMonth, int cDay, String userID){
            fname = "" + userID + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";//저장할 파일 이름설정
            FileInputStream fis = null;

            try {
                fis = getActivity().openFileInput(fname);

                byte[] fileData = new byte[fis.available()];
                fis.read(fileData);
                fis.close();

                str = new String(fileData);

                binding.contextEditText.setVisibility(View.INVISIBLE);
                binding.textView2.setVisibility(View.VISIBLE);
                binding.textView2.setText(str);

                binding.saveBtn.setVisibility(View.INVISIBLE);
                binding.chaBtn.setVisibility(View.VISIBLE);
                binding.delBtn.setVisibility(View.VISIBLE);

                binding.chaBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.contextEditText.setVisibility(View.VISIBLE);
                        binding.textView2.setVisibility(View.INVISIBLE);
                        binding.contextEditText.setText(str);

                        binding.saveBtn.setVisibility(View.VISIBLE);
                        binding.chaBtn.setVisibility(View.INVISIBLE);
                        binding.delBtn.setVisibility(View.INVISIBLE);
                        binding.textView2.setText(binding.contextEditText.getText());
                    }

                });
                binding.delBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.textView2.setVisibility(View.INVISIBLE);
                        binding.contextEditText.setText("");
                        binding.contextEditText.setVisibility(View.VISIBLE);
                        binding.saveBtn.setVisibility(View.VISIBLE);
                        binding.chaBtn.setVisibility(View.INVISIBLE);
                        binding.delBtn.setVisibility(View.INVISIBLE);
                        removeDiary(fname);
                    }
                });
                if (binding.textView2.getText() == null) {
                    binding.textView2.setVisibility(View.INVISIBLE);
                    binding.diaryTextView.setVisibility(View.VISIBLE);
                    binding.saveBtn.setVisibility(View.VISIBLE);
                    binding.chaBtn.setVisibility(View.INVISIBLE);
                    binding.delBtn.setVisibility(View.INVISIBLE);
                    binding.contextEditText.setVisibility(View.VISIBLE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /*일정을 삭제*/
        @SuppressLint("WrongConstant")
        public void removeDiary (String readDay){
            FileOutputStream fos = null;

            try {
                fos = getActivity().openFileOutput(readDay, Context.MODE_PRIVATE);
                String content = "";
                fos.write((content).getBytes());
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*일정을 저장*/
        @SuppressLint("WrongConstant")
        public void saveDiary (String readDay){
            FileOutputStream fos = null;

            try {
                fos = getActivity().openFileOutput(readDay, Context.MODE_PRIVATE);
                String content = binding.contextEditText.getText().toString();
                fos.write((content).getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
