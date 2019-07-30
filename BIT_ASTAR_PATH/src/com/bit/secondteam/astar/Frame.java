package com.bit.secondteam.astar;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

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

	private int[][] map;
	private JButton btn[][];
	private ArrayList<JButton> colored;

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

	public void initFrame() {
		setTitle("2조 - A* 알고리즘 길 찾기");
		setSize(600, 600);
		setLayout(new GridLayout(row, col));
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initButton() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				btn[i][j] = new JButton("");
				btn[i][j].setBorder(new LineBorder(Color.LIGHT_GRAY));
				btn[i][j].addActionListener(btn_listner(i, j));
				btn[i][j].setFocusable(false);
				add(btn[i][j]);
			}
		}
	}

	public ActionListener btn_listner(int i, int j) {
		
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof JButton) {
					JButton event = (JButton) e.getSource();
					if (event.getText() == "■") {
						event.setBackground(null);
						event.setText("");
						map[i][j] = 0;
					} else {
						event.setBackground(new Color(42, 179, 231));
						event.setText("■");
						map[i][j] = 1;
					}
					
					solve();
				}
			}
		};

		return listener;
	}

	public void setView() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				setVisible(true);
			}
		});
	}
	
	public void solve() {
		info = new MapInfo(deepCopy(map), row, col, start, end);
		new AStar().start(info);
		System.out.println("----------------");
		printMap(info.maps);
		
		//벽 색깔 칠해주기
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (info.maps[i][j] == 2) {
					btn[i][j].setBackground(Color.green);
				}
				else if (info.maps[i][j] == 0){
					btn[i][j].setBackground(null);
				}
			}
		}
		
		btn[0][0].setBackground(Color.red);
		btn[0][0].setText("[출발지]");
		btn[0][0].setEnabled(false);
		btn[9][9].setBackground(Color.red);
		btn[9][9].setText("[도착지]");
		btn[9][9].setEnabled(false);
	}
	
	private static int[][] deepCopy(int[][] arr) {
        if(arr == null) return null;
        int[][] result = new int[arr.length][arr[0].length];
         
        for(int i=0; i<arr.length; i++){
            System.arraycopy(arr[i], 0, result[i], 0, arr[0].length);
        }
         
        return result;
    }

	public void printMap(int[][] maps) {
		for (int i = 0; i < maps.length; i++) {
			for (int j = 0; j < maps[i].length; j++) {
				System.out.print(maps[i][j] + " ");
			}
			System.out.println();
		}
	}
}
