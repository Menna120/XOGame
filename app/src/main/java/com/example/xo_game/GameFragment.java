package com.example.xo_game;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.xo_game.databinding.FragmentGameBinding;

public class GameFragment extends Fragment {

    private FragmentGameBinding binding;
    private final int[][] boardState = new int[3][3];
    private boolean isPlayer1Turn = true;
    private int movesCount = 0;
    private boolean isPlayer1X;
    private final ImageButton[][] cells = new ImageButton[3][3];
    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            isPlayer1X = arguments.getBoolean(getString(R.string.isplayer1x), true);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeBoard();
        setupCells();
        updateTurnText();
        startTimer();

        binding.playAgainButton.setOnClickListener(v -> resetGame());
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardState[i][j] = 0;
            }
        }
    }

    private void setupCells() {
        cells[0][0] = binding.cell00;
        cells[0][1] = binding.cell01;
        cells[0][2] = binding.cell02;
        cells[1][0] = binding.cell10;
        cells[1][1] = binding.cell11;
        cells[1][2] = binding.cell12;
        cells[2][0] = binding.cell20;
        cells[2][1] = binding.cell21;
        cells[2][2] = binding.cell22;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                cells[i][j].setOnClickListener(v -> onCellClick(row, col));
            }
        }
    }

    private void onCellClick(int row, int col) {
        if (boardState[row][col] == 0) {
            boardState[row][col] = isPlayer1Turn ? 1 : 2;
            int drawableId = isPlayer1Turn ? (isPlayer1X ? R.drawable.ic_x : R.drawable.ic_o) : (isPlayer1X ? R.drawable.ic_o : R.drawable.ic_x);
            cells[row][col].setImageResource(drawableId);
            movesCount++;

            if (checkForWin()) {
                endGame(isPlayer1Turn ? "Player 1 Wins!" : "Player 2 Wins!");
            } else if (movesCount == 9) {
                endGame("It's a Draw!");
            } else {
                isPlayer1Turn = !isPlayer1Turn;
                updateTurnText();
            }
        }
    }

    private boolean checkForWin() {
        for (int i = 0; i < 3; i++) {
            if (boardState[i][0] != 0 && boardState[i][0] == boardState[i][1] && boardState[i][1] == boardState[i][2]) {
                return true;
            }
            if (boardState[0][i] != 0 && boardState[0][i] == boardState[1][i] && boardState[1][i] == boardState[2][i]) {
                return true;
            }
        }
        if (boardState[0][0] != 0 && boardState[0][0] == boardState[1][1] && boardState[1][1] == boardState[2][2]) {
            return true;
        }
        return boardState[0][2] != 0 && boardState[0][2] == boardState[1][1] && boardState[1][1] == boardState[2][0];
    }

    private void endGame(String message) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        binding.winnerLayout.setVisibility(View.VISIBLE);
        binding.winnerText.setText(message);
        setBoardEnabled(false);
    }

    private void resetGame() {
        initializeBoard();
        isPlayer1Turn = true;
        movesCount = 0;
        updateTurnText();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setImageResource(0);
            }
        }
        binding.winnerLayout.setVisibility(View.GONE);
        setBoardEnabled(true);
        startTimer();
    }

    private void setBoardEnabled(boolean enabled) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setEnabled(enabled);
            }
        }
    }

    private void updateTurnText() {
        binding.turnTextView.setText(isPlayer1Turn ? "Player 1's Turn" : "Player 2's Turn");
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                binding.timerTextView.setText(timeFormatted);
            }

            public void onFinish() {
                endGame("Time's Up!");
            }
        }.start();
    }
}