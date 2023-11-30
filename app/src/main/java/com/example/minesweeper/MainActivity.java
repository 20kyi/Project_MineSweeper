package com.example.minesweeper;




import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static BlockButton[][] buttons = new BlockButton[9][9];
    TableLayout table;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        table = findViewById(R.id.tableLayout);
        textView = findViewById(R.id.textView);

        for (int i = 0; i < 9; i++) {   //TableRows 9개 추가
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            TableRow.LayoutParams layoutParams =
                    new TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1.0f);

            for (int j = 0; j < 9; j++) {   //BlockButton 9개 추가
                buttons[i][j] = new BlockButton(this, i, j);
                buttons[i][j].setLayoutParams(layoutParams);
                tableRow.addView(buttons[i][j]);

                //BlockButton 클릭
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view instanceof BlockButton) {
                            BlockButton blockButton = (BlockButton) view;

                            ToggleButton toggleButton = findViewById(R.id.toggleButton);
                            if (toggleButton.isChecked()) {    //FLAG 일 때
                                blockButton.toggleFlag();
                            }
                            else {  //BREAK BUTTON 일 때
                                blockButton.breakBlock(view);
                            }
                        }
                    }
                });
            }
        }
        placeMines();   //지뢰 배치
        countNeighborMines();     //주변 지뢰 개수
    }

    /*지뢰 배치 메소드*/
    public void placeMines() {
        int totalMines = 10;    //전체 지뢰 개수

        List<BlockButton> Mines = new ArrayList<>();   //지뢰 리스트 생성
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Mines.add(buttons[i][j]);
            }
        }
        Collections.shuffle(Mines);    // 지뢰 리스트 랜덤으로 섞기
        for (int i = 0; i < totalMines; i++) {
            BlockButton button = Mines.get(i);
            button.setMine(true);
            //button.setText("*");    // 지뢰 위치 테스트용
        }
    }

    /* 주변 지뢰 개수 표시 */
    public void countNeighborMines() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int neighborMines = 0;

                /*현재 블록 주변 지뢰 개수
                * (-1,-1) (-1,0) (-1,1)
                * (0, -1)        (0, 1)
                * (1, -1) (1, 0) (1, 1)
                */
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        int x = i + m;
                        int y = j + n;
                        if (x >= 0 && x < 9 && y >= 0 && y < 9 && buttons[x][y].isMine()) {
                            neighborMines++;
                        }
                    }
                }
                buttons[i][j].getNeighborMines(neighborMines);
            }
        }
    }

    /*게임 초기화*/
    public void resetGame() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                buttons[i][j].GameReset();  // Add a reset method in BlockButton class
            }
        }
        placeMines();
        countNeighborMines();
    }
}