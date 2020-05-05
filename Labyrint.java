import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;

public class Labyrint {
	private static Rute[][]maze;
	private int rowNum, colNum;
	private static Labyrint copy;
	private Labyrint(Rute[][]a, int b, int c){
		setMaze(a);
		rowNum = b;
		colNum =c ;
		
	}
	// call the given 'rute' to find its way out
	public static List<String> finnUtveiFra(int k, int r){	
		List<String> list = new LinkedList<String>();
		if(Labyrint.getRute(k,r).tilTegn()=='.') {
			list = Labyrint.getRute(k, r).finnUtvei(list);
		}else {
			System.out.println("Current start point is '#', choose again");
		}
		return list ;
	}
	// reading from the file
	public static Labyrint lesFraFil(File fil) {
		Scanner s1 = null;
		int r = 0,c =0;
		Labyrint ly1 = null;
		try {
			s1 = new Scanner(fil);
			System.out.println("Reading File " + fil);
		} catch (FileNotFoundException e) {
			// System.out.println(System.getProperty("user.dir"));
			System.out.println("Fant ikke filen");			
		}
		if(s1.hasNext()) {
			String l1 = s1.nextLine();
			String [] xy = l1.split(" ");
			if(xy.length >=2) {
			r= Integer.parseInt(xy[0]);
			c = Integer.parseInt(xy[1]);
			}else {
				System.out.println("File format Wrong");
			}
			int rowcount= 0;
			setMaze(new Rute[r][c]);
			ly1= new Labyrint(getMaze(),r,c);
			while (s1.hasNextLine()&& r>1 && c>1) {
				l1 = s1.nextLine();		
				for(int o = 0;o<c;o++) {
//						System.out.println(l1.charAt(o));
						switch (l1.charAt(o)) {
						case '.':
							HvitRute n= new HvitRute(o,rowcount,'.',ly1);
							getMaze()[rowcount][o]=n;
							break;
						case '#':
							SortRute n2= new SortRute(o,rowcount,'#',ly1);
							getMaze()[rowcount][o]=n2;
							break;
						default:
							System.out.println("not valid input");
							break;
						}
				}
				rowcount++;			
			}
		}
		//update all the four neighbours 
		ly1 = updateNeighbour(ly1);
		copy= ly1;
		return 	ly1;
	}
	
	//method for updating neighbours
	public static Labyrint updateNeighbour(Labyrint l1) {
		for(int i= 0; i<l1.getRowNum();i++) {
			for(int o= 0;o<l1.getColNum();o++) {	
				Rute r=Labyrint.getRute(o, i);
				
				//this is the code from last time that has a bug, i think it has sth to do with the upcasting or downcasting
				//also the 'aapning' class in this program is kind of not playing any important role
//				if(edge( r)&&r.getTegn()=='.') {
//					r=aapning(r);	
//				}
				if(edge(r)) {
					if(i-1>=0) {
						r.setNord(Labyrint.getRute(o,i-1 ));
					}else {
						r.setNord(null);
					}
					if(i+1<=l1.getRowNum()-1) {
						r.setSyd(Labyrint.getRute(o,i+1));
					}else {
						r.setSyd(null);
					}
					if(o-1>=0) {
						r.setVest(Labyrint.getRute( o-1,i));
					}else {
						r.setVest(null);
					}
					if(o+1<=l1.getColNum()-1) {
						r.setOst(Labyrint.getRute(o+1,i));
					}else {
						r.setOst(null);
					}
					
				}else {
					r.setNord(Labyrint.getRute(o,i-1));
					r.setSyd(Labyrint.getRute(o,i+1));
					r.setOst(Labyrint.getRute( o+1,i));
					r.setVest(Labyrint.getRute( o-1,i));
				}
			}
			
		}
		
		return l1;
		
	}
	

	// check if a 'Rute' is on the edge, yes return true
	public static boolean edge(Rute r) {
		if(r.getX()==0 || r.getY()==0||r.getX()==r.getLabyrint().getColNum()-1|| r.getY()== r.getLabyrint().getRowNum()-1) {
			return true;
		}else {
			return false;
		}
	}
	// some getters an setters
	public static  Rute[][] getMaze() {
		return maze;
	}
	public static void setMaze(Rute[][] maze) {
		Labyrint.maze = maze;
	}
	public static Rute getRute(int a, int b) {
		return Labyrint.getMaze()[b][a];
		
	}
	public String toString() {
		String f="";
		for(int i= 0; i<getRowNum();i++) {
			for(int o=0; o<getColNum();o++) {
				f+=getMaze()[i][o].getTegn();
			}
			f+="\n";
		}
		return f;
	}
	public void printMazeC() {
		for(int i = 0; i<getRowNum();i++) {
			for(int o= 0; o<getColNum();o++) {
				System.out.print("["+o+","+i+"]");
			}
			System.out.println();
		}
	}
	public int getColNum() {
		return colNum;
	}
	public int getRowNum() {
		return rowNum;
	}
}
