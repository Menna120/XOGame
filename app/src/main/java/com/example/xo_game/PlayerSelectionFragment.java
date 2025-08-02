package com.example.xo_game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlayerSelectionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_selection, container, false);

        ImageView xButton = view.findViewById(R.id.xButton);
        ImageView oButton = view.findViewById(R.id.oButton);

        xButton.setOnClickListener(v -> ((MainActivity) requireActivity()).startGame(true));
        oButton.setOnClickListener(v -> ((MainActivity) requireActivity()).startGame(false));

        return view;
    }
}