package com.example.minesweeper;

import static com.example.minesweeper.MainActivity.buttons;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public class BlockButton extends Button {
    private int x, y;
    private boolean mine;
    private boolean flag;
    private int neighborMines;
    private static int flags;
    private static int blocks;
    private Drawable originalBackground;

    /*블록의 좌표*/
    public BlockButton(Context context, int x, int y) {
        super(context);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f
        );

        blocks++;   //남은 블록 수 증가

        this.x = x;
        this.y = y;

        mine = false;
        flag = false;
        neighborMines = 0;

        originalBackground = getBackground();
    }

    /*깃발 설치 및 제거 토글 메소드*/
    public void toggleFlag() {
        if (!flag) {    //깃발 없을 경우 깃발 설치
            flags++;
            blocks--;
            setText("🚩");
            updateFlags();
        } else {        //깃발 있을 경우 깃발 제거
            flags--;
            blocks++;
            setText("");
            updateFlags();
        }
        flag = !flag;   //깃발 상태 토글
        updateBlocks();

        if (blocks == 0 && flags == 10 && (buttons[x][y].isMine() == buttons[x][y].isFlag())) {
            GameWin();
        }
    }

    /*블록 오픈 메소드*/
    public boolean breakBlock(View view) {
        setClickable(false);
        if(!isFlag()) {
            blocks--;   //남은 블록 수 감소

            //블록이 지뢰일 경우 게임오버
            if (mine) {
                openAllMines();     //모든 지뢰 오픈
                GameOver();         //게임오버
            }
            //블록이 지뢰가 아닐 경우
            else {
                setBackgroundColor(Color.WHITE);
                if (neighborMines == 0) {
                    openNeighbors();    //이웃 블록 오픈 재귀함수
                } else {
                    setText(String.valueOf(neighborMines));
                }
                updateBlocks();
            }

            // 모든 블록을 오픈했을 경우 승리
            if (blocks == 0 && flags == 10 && (buttons[x][y].isMine() == buttons[x][y].isFlag())) {
                GameWin();
            }
        }
        return mine;
    }

    /* 이웃 블록 오픈 메소드(재귀 호출) */
    public void openNeighbors() {
        setClickable(false);
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < 9 && j >= 0 && j < 9) {
                    if (!buttons[i][j].isClickable()) {     //열려있는 블록 제외
                        continue;
                    }
                    buttons[i][j].setClickable(false);
                    if(!buttons[i][j].isFlag()) {
                        buttons[i][j].setBackgroundColor(Color.WHITE);
                        if (buttons[i][j].neighborMines == 0) {
                            buttons[i][j].openNeighbors();
                        }
                        else{
                            buttons[i][j].setText(String.valueOf(buttons[i][j].neighborMines));
                        }
                        blocks--;
                    }
                }
            }
        }
    }

    /*지뢰 클릭시 모든 지뢰 오픈 메소드*/
    public void openAllMines() {
        setClickable(false);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (buttons[i][j].isMine()) {
                    buttons[i][j].setText("💣");
//                    if(buttons[i][j].isFlag()) {    //깃발을 설치한 곳이 지뢰일 경우
//                        buttons[i][j].setText("⭐!");
//                    }
                }
            }
        }
    }

    /*게임 오버 메소드*/
    public void GameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage("지뢰를 밟았습니다!")
                .setTitle("💥 Game Over")
                .setPositiveButton("RESET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) getContext()).resetGame();
                    }
                })
                .show();
    }

    /*게임 승리 메소드*/
    public void GameWin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("승리하였습니다!!")
                .setTitle("👑 You Win")
                .setPositiveButton("RESET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) getContext()).resetGame();
                    }
                })
                .show();
    }

    /*게임 초기화 메소드*/
    public void GameReset() {
        setClickable(true);
        setText("");
        setVisibility(VISIBLE);
        setBackground(originalBackground);
        mine = false;
        flag = false;

        neighborMines = 0;
        blocks = 81;
        flags = 0;
        updateBlocks();
        updateFlags();
    }

    /*남은 블록 수 메소드*/
    public void updateBlocks() {
        TextView textView2 = ((Activity)getContext()).findViewById(R.id.textView2);
        textView2.setText("남은 블록 수: " + blocks);
    }

    /*남은 지뢰(깃발) 수 메소드*/
    public void updateFlags() {
        int Mines = 10 - flags;
        TextView textView = ((Activity)getContext()).findViewById(R.id.textView);
        textView.setText("MINES: " + Mines);
    }

    public boolean isFlag() {
        return flag;
    }
    public boolean isMine() {
        return mine;
    }
    public void setMine(boolean mine) {
        this.mine = mine;
    }
    public void getNeighborMines(int mines) {
        neighborMines = mines;
    }
}
