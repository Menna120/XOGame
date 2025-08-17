package com.example.xo_game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.xo_game.databinding.FragmentPlayerSelectionBinding;

public class PlayerSelectionFragment extends Fragment {
    private FragmentPlayerSelectionBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlayerSelectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.xButton.setOnClickListener(v -> ((MainActivity) requireActivity()).startGame(true));
        binding.oButton.setOnClickListener(v -> ((MainActivity) requireActivity()).startGame(false));
    }
}