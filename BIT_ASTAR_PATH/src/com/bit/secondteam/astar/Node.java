package com.bit.secondteam.astar;

public class Node implements Comparable<Node>
{

	public Coord coord; // 좌표값
	public Node parent; // 부모 좌표값
	public int G; // G：(정확한값)，시작점과 현재점에 대한 비용
	public int H; // H：(추정치)，현재점과 도착점에 대한 비용

	public Node(int x, int y)
	{
		this.coord = new Coord(x, y);
	}

	public Node(Coord coord, Node parent, int g, int h)
	{
		this.coord = coord;
		this.parent = parent;
		G = g;
		H = h;
	}

	@Override
	public int compareTo(Node o)
	{
		if (o == null) return -1;
		if (G + H > o.G + o.H)
			return 1;
		else if (G + H < o.G + o.H) return -1;
		return 0;
	}
}
