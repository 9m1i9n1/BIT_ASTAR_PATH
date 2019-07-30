package com.bit.secondteam.astar;

public class MapInfo
{
	public int[][] maps; // 이차원 배열로 만든 맵
	public int width; // 맵의 가로
	public int hight; // 맵의 세로
	public Node start; // 시작지점
	public Node end; // 종료지점
	
	public MapInfo(int[][] maps, int width, int hight, Node start, Node end)
	{
		this.maps = maps;
		this.width = width;
		this.hight = hight;
		this.start = start;
		this.end = end;
	}
}
