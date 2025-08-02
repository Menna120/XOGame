package com.example.xo_game;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameFragment extends Fragment {

    private final int[][] boardState = new int[3][3];
    private boolean isPlayer1Turn = true;
    private int movesCount = 0;
    private boolean isPlayer1X;

    private TextView timerTextView;
    private TextView turnTextView;
    private final ImageButton[][] cells = new ImageButton[3][3];
    private LinearLayout winnerLayout;
    private TextView winnerText;

    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            isPlayer1X = arguments.getBoolean("isPlayer1X", true);
        }

        timerTextView = view.findViewById(R.id.timerTextView);
        turnTextView = view.findViewById(R.id.turnTextView);
        winnerLayout = view.findViewById(R.id.winner_layout);
        winnerText = view.findViewById(R.id.winnerText);
        View playAgainButton = view.findViewById(R.id.play_again_button);

        initializeBoard();
        setupCells(view);
        updateTurnText();
        startTimer();

        playAgainButton.setOnClickListener(v -> resetGame());

        return view;
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardState[i][j] = 0;
            }
        }
    }

    private void setupCells(View view) {
        cells[0][0] = view.findViewById(R.id.cell00);
        cells[0][1] = view.findViewById(R.id.cell01);
        cells[0][2] = view.findViewById(R.id.cell02);
        cells[1][0] = view.findViewById(R.id.cell10);
        cells[1][1] = view.findViewById(R.id.cell11);
        cells[1][2] = view.findViewById(R.id.cell12);
        cells[2][0] = view.findViewById(R.id.cell20);
        cells[2][1] = view.findViewById(R.id.cell21);
        cells[2][2] = view.findViewById(R.id.cell22);

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
        if (boardState[0][2] != 0 && boardState[0][2] == boardState[1][1] && boardState[1][1] == boardState[2][0]) {
            return true;
        }
        return false;
    }

    private void endGame(String message) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (winnerLayout != null) {
            winnerLayout.setVisibility(View.VISIBLE);
            winnerText.setText(message);
        }
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
        if (winnerLayout != null) {
            winnerLayout.setVisibility(View.GONE);
        }
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
        if (turnTextView != null) {
            turnTextView.setText(isPlayer1Turn ? "Player 1's Turn" : "Player 2's Turn");
        }
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
                if (timerTextView != null) {
                    timerTextView.setText(timeFormatted);
                }
            }

            public void onFinish() {
                endGame("Time's Up!");
            }
        }.start();
    }
}