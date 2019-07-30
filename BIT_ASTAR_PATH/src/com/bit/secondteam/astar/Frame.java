package com.bit.secondteam.astar;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class Frame extends JFrame {

	private int row;
	private int col;
	private Node start;
	private Node end;
	private MapInfo info;

	//=================
	
	private int[][] map;
	private JButton btn[][];

	//=================
	
	public Frame(int row, int col, Node start, Node end) {

		this.row = row;
		this.col = col;
		this.start = start;
		this.end = end;

		btn = new JButton[row][col];
		map = new int[row][col];

		initFrame();
		initButton();
		setView();
	}

	// Frame 생성
	public void initFrame() {
		setTitle("2조 - A* 알고리즘 길 찾기");
		setSize(600, 600);
		setLayout(new GridLayout(row, col));
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// Layout에 버튼 배치
	public void initButton() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				btn[i][j] = new JButton("");
				btn[i][j].setBackground(Color.white);
				btn[i][j].setBorder(new LineBorder(Color.LIGHT_GRAY));
				btn[i][j].addActionListener(btn_listner(i, j));
				btn[i][j].setFocusable(false);
				add(btn[i][j]);
			}
		}
		pointButtonColored();
	}

	// 버튼 액션리스너
	public ActionListener btn_listner(int i, int j) {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof JButton) {
					JButton event = (JButton) e.getSource();
					// 벽을 만드는 이벤트일 경우
					if (event.getText() == " ") {
						event.setBackground(Color.white);
						event.setText("");
						map[i][j] = 0;
					}
					// 벽을 해제하는 이벤트일 경우
					else {
						event.setBackground(Color.LIGHT_GRAY);
						event.setText(" ");
						map[i][j] = 1;
					}
					
					solve();
				}
			}
		};

		return listener;
	}

	// invokeLater로 제일 마지막에 setVisible 설정
	public void setView() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) { }
		
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				setVisible(true);
			}
		});
	}
	
	// 알고리즘 실행 구문
	public void solve() {
		info = new MapInfo(deepCopy(map), row, col, start, end);
		new AStar().start(info);
		buttonColored();
	}
	
	// 경로 버튼 색칠
	private void buttonColored() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (info.maps[i][j] == 2) {
					btn[i][j].setBackground(new Color(153, 255, 51));
				}
				else if (info.maps[i][j] == 0){
					btn[i][j].setBackground(Color.white);
				}
			}
		}
		
		pointButtonColored();
	}
	
	// 출발지, 도착지 색칠
	private void pointButtonColored() {
		btn[0][0].setBackground(new Color(204, 0, 0));
		btn[0][0].setText("[출발지]");
		btn[0][0].setEnabled(false);
		btn[9][9].setBackground(new Color(76, 153, 0));
		btn[9][9].setText("[도착지]");
		btn[9][9].setEnabled(false);
	}
	
	// info.maps와 frame 내의 map 깊은 복사 전용
	private int[][] deepCopy(int[][] arr) {
        if(arr == null) return null;
        int[][] result = new int[arr.length][arr[0].length];
         
        for(int i=0; i<arr.length; i++){
            System.arraycopy(arr[i], 0, result[i], 0, arr[0].length);
        }
         
        return result;
    }
}
