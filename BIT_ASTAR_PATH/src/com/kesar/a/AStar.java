package com.kesar.a;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 
 * ClassName: AStar 
 * @Description: A星算法
 * @author kesar
 */
public class AStar
{
	public final static int BAR = 1; // 벽 값
	public final static int PATH = 2; // 경로
	public final static int DIRECT_VALUE = 10; // 직선 비용
	public final static int OBLIQUE_VALUE = 14; // 대각선 비용
	
	Queue<Node> openList = new PriorityQueue<Node>(); // 우선순위큐(오름차순)
	List<Node> closeList = new ArrayList<Node>();
	
	/**
	 * 시작 알고리즘
	 */
	public void start(MapInfo mapInfo)
	{
		if(mapInfo==null) return;
		// clean
		openList.clear();
		closeList.clear();
		// 검색시작
		openList.add(mapInfo.start);
		moveNodes(mapInfo);
	}

	/**
	 * 노드 이동
	 */
	private void moveNodes(MapInfo mapInfo)
	{
		while (!openList.isEmpty())
		{
			// 만약 CloseList에 EndPoint가 있나?
			if (isCoordInClose(mapInfo.end.coord))
			{
				//있으면 맵 출력하고 탈출함.
				drawPath(mapInfo.maps, mapInfo.end);
				break;
			}
			
			for (Node node : openList) {
				System.out.println("x : " + node.coord.x + " y : " + node.coord.y);
			}
			System.out.println("================================");
			
			//도착지가 아니면, current에 현재 Node 위치를 가져온다.
			//G값을 기준으로 작은값 부터 처리해나간다.
			Node current = openList.poll();
			
			System.out.println("Current x : " + current.coord.x + " y : " + current.coord.y);
			
			//현재노드는 지금기준으로 사용하므로 CloseList에 넣는다.
			closeList.add(current);
			//인전노드에 대해서, 현재 가중치 g(x),휴리스틱 추정값 h(x)을 구한다.
 			addNeighborNodeInOpen(mapInfo,current);
		}
	}
	
	/**
	 * 2차원 배열에서 경로 그리기
	 */
	private void drawPath(int[][] maps, Node end)
	{
		if(end==null||maps==null) return;
		while (end != null)
		{
			Coord c = end.coord;
			maps[c.y][c.x] = PATH;
			System.out.println(end.coord.x + " " + end.coord.y);
			end = end.parent;
		}
	}

	/**
	 * 인접한 노드 모두 Open에 추가
	 */
	private void addNeighborNodeInOpen(MapInfo mapInfo,Node current)
	{
		int x = current.coord.x;
		int y = current.coord.y;
		// 좌측
		addNeighborNodeInOpen(mapInfo,current, x - 1, y, DIRECT_VALUE);
		// 아래
		addNeighborNodeInOpen(mapInfo,current, x, y - 1, DIRECT_VALUE);
		// 우측
		addNeighborNodeInOpen(mapInfo,current, x + 1, y, DIRECT_VALUE);
		// 위
		addNeighborNodeInOpen(mapInfo,current, x, y + 1, DIRECT_VALUE);
		// 좌측하단
		addNeighborNodeInOpen(mapInfo,current, x - 1, y - 1, OBLIQUE_VALUE);
		// 우측하단
		addNeighborNodeInOpen(mapInfo,current, x + 1, y - 1, OBLIQUE_VALUE);
		// 우측상단
		addNeighborNodeInOpen(mapInfo,current, x + 1, y + 1, OBLIQUE_VALUE);
		// 좌측상단
		addNeighborNodeInOpen(mapInfo,current, x - 1, y + 1, OBLIQUE_VALUE);
	}

	/**
	 * 인접한 노드 모두 Open에 추가
	 */
	private void addNeighborNodeInOpen(MapInfo mapInfo,Node current, int x, int y, int value)
	{
		//Node가 추가될 수 있는지 확인.
		if (canAddNodeToOpen(mapInfo,x, y))
		{
			Node end=mapInfo.end;
			Coord coord = new Coord(x, y);
			int G = current.G + value; // 인접 접점의 G값 계산
			//제일 첫 시작시엔 인접노드들이 모두 OpneList에 없으므로 null이 Return한다.
			Node child = findNodeInOpen(coord);
			if (child == null)
			{
				//휴리스틱 추정치 h(x) 계산
				int H = calcH(end.coord,coord);
				//EndPoint일 경우.
				if(isEndNode(end.coord,coord))
				{
					child=end;
					child.parent=current;
					child.G=G;
					child.H=H;
				}
				//EndPoint가 아닐 경우 해당 Node에 G와 H값을 저장.
				else
				{
					child = new Node(coord, current, G, H);
				}
				//새로 생성된 Node객체는 OpenList에 추가.
				openList.add(child);
			}
			//이미 OpenList에 있는 Node 중 작은 값의 G는 불필요하므로 생략
			//가중치 G값을 현재 부모노드의 기준으로 다시 초기화.
			//이상하게 여기의 조건문은 안 들어간다.
			else if (child.G > G)
			{
				child.G = G;
				child.parent = current;
				openList.add(child);
			}
		}
	}

	/**
	 * 좌표가 Open에 있는지 판단
	 */
	private Node findNodeInOpen(Coord coord)
	{
		if (coord == null || openList.isEmpty()) return null;
		for (Node node : openList)
		{
			if (node.coord.equals(coord))
			{
				return node;
			}
		}
		return null;
	}


	/**
	 * H값 계산：“맨하탄”방식，좌표별 추가
	 */
	private int calcH(Coord end,Coord coord)
	{
		return Math.abs(end.x - coord.x)
				+ Math.abs(end.y - coord.y);
	}
	
	/**
	 * 마지막 노트인지 판단
	 */
	private boolean isEndNode(Coord end,Coord coord)
	{
		return coord != null && end.equals(coord);
	}

	/**
	 * 노드를 Open에 넣을 수 있는지 판단
	 */
	private boolean canAddNodeToOpen(MapInfo mapInfo,int x, int y)
	{
		// 맵 에 있는지 판단
		if (x < 0 || x >= mapInfo.width || y < 0 || y >= mapInfo.hight) return false;
		// 벽인지 판단
		if (mapInfo.maps[y][x] == BAR) return false;
		// 닫혀있는지 판단
		if (isCoordInClose(x, y)) return false;

		return true;
	}

	/**
	 * 좌표가 Close에 있는지 판단
	 */
	private boolean isCoordInClose(Coord coord)
	{
		return coord!=null&&isCoordInClose(coord.x, coord.y);
	}

	/**
	 * 좌표가 Close에 있는지 판단
	 */
	private boolean isCoordInClose(int x, int y)
	{
		if (closeList.isEmpty()) return false;
		for (Node node : closeList)
		{
			if (node.coord.x == x && node.coord.y == y)
			{
				return true;
			}
		}
		return false;
	}
}
