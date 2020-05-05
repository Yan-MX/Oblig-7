import java.io.File;
import java.util.List;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/*This maze runner can process maze without circulation
 * users can click 'Choose file' button to choose a file
 * and then click on any while cell to check one possible way from this start point
 * black cell is set to be not clickable 
 * if the user want to clear the maze and choose another starting point, the user can press 'reset' button
 * 
 * 
 * Have this error after running, but it is a system error in java. Platform.exit causes assertion error on macOS Catalina.
 * 
 * Java has been detached already, but someone is still trying to use it at -[GlassViewDelegate dealloc]:/HUDSON/jfx/workspace/8u-macosx-universal-1/8u231/rt/modules/graphics/src/main/native-glass/mac/GlassViewDelegate.m:198
0   libglass.dylib                      0x00000001163d9792 -[GlassViewDelegate dealloc] + 290
1   libobjc.A.dylib                     0x00007fff714cbeb3 _ZN11objc_object17sidetable_releaseEb + 229
2   libglass.dylib                      0x00000001163d7bdc -[GlassView3D dealloc] + 252
3   libobjc.A.dylib                     0x00007fff714e2054 _ZN19AutoreleasePoolPage12releaseUntilEPP11objc_object + 134
4   libobjc.A.dylib                     0x00007fff714c6dba objc_autoreleasePoolPop + 175
5   CoreFoundation                      0x00007fff388ca4f5 __CFRUNLOOP_IS_CALLING_OUT_TO_AN_OBSERVER_CALLBACK_FUNCTION__ + 23
6   CoreFoundation                      0x00007fff388ca427 __CFRunLoopDoObservers + 457
7   CoreFoundation                      0x00007fff388c99c5 __CFRunLoopRun + 874
8   CoreFoundation                      0x00007fff388c8ffe CFRunLoopRunSpecific + 462
9   java                                0x000000010c05246e CreateExecutionEnvironment + 871
10  java                                0x000000010c04e03c JLI_Launch + 1952
11  java                                0x000000010c0544cb main + 101
12  java                                0x000000010c04d894 start + 52
 * 
 * */



public class hovedprogram extends Application {
	public Text info;
	public Labyrint l;
	public Window primaryStage;
	public GridPane rutenett;

	public static void main() {
		launch("hovedprogram");
	}
// this is setting up the scene
	public void start(Stage teater) {

		Button stoppknapp = new Button("Quit");
		stoppknapp.setLayoutX(400);
		stoppknapp.setLayoutY(20);
		Stoppbehandler stopp = new Stoppbehandler();
		stoppknapp.setOnAction(stopp);

		Button openknapp = new Button("Choose file");
		openknapp.setLayoutX(20);
		openknapp.setLayoutY(20);
		OpenFilebehandler openfile = new OpenFilebehandler();
		openknapp.setOnAction(openfile);
		
		
		Button clearknapp = new Button("Reset");
		clearknapp.setLayoutX(140);
		clearknapp.setLayoutY(20);
		 Clearbehandler clear = new  Clearbehandler();
		clearknapp.setOnAction(clear);

		info = new Text("Please choose a file to begin with:");
		info.setFont(new Font(15));
		info.setX(20);
		info.setY(70);

		rutenett = new GridPane();
		rutenett.setGridLinesVisible(true);
		rutenett.setLayoutX(20);
		rutenett.setLayoutY(100);

		Pane kulisser = new Pane();
		kulisser.setPrefSize(460, 600);
		kulisser.getChildren().add(rutenett);
		kulisser.getChildren().add(stoppknapp);
		kulisser.getChildren().add(openknapp);
		kulisser.getChildren().add(info);
		kulisser.getChildren().add(clearknapp);

		Scene scene = new Scene(kulisser);

		teater.setTitle("Maze");
		teater.setScene(scene);
		teater.show();

	}
	
	//each event handler is a class
	class Clearbehandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent e) {
			updateGridPane(l);
		}
}

	class Stoppbehandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			Platform.exit();
		}
	}
// this is to open file and choose a file
	class OpenFilebehandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			FileChooser fileChooser = new FileChooser();
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			l = null;
			try{
				l = Labyrint.lesFraFil(selectedFile);
				//call this method, to update the Grids in gridpane
				updateGridPane(l);
			}catch(Exception e1) {
//				System.out.println(e1.getMessage());
			}
			}
			
	}
//this button returns the grid that the user clicked
	class Klikkbehandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {

			((Grid) e.getSource()).setStyle("-fx-background-color: #00A5BF	");
			
			ShowEscapeRoute((Grid)e.getSource());
		}
	}
	
// a method, using the position/coordinate to find its way out using codes from Oblig 5&6 
//using the given method losningStringTilTabell() to get a boolean list of list
//for each grid in the boolean list that is true, change its color to pink
	public void ShowEscapeRoute(Grid grid){
		List<String> utveier = l.finnUtveiFra(grid.col, grid.row);
		if (utveier.size() != 0) {
			info.setText("A possible way to escape is showed in pink color");
			boolean[][] escape=losningStringTilTabell(utveier.get(0),l.getColNum(),l.getRowNum());

			for (Node child : rutenett.getChildren()) {
			    int column = GridPane.getColumnIndex(child);
			    int row = GridPane.getRowIndex(child);
			    if (escape[row][column]) {
			    	child.setStyle("-fx-background-color: f47983");
			    }
			    	
			    }
			    
		}else {
			info.setText("There is no way out from this starting point.");
		}
	}
	
//updating grid's color based on the rute's char
//set klikkbutton to each white cell/rute
	public void updateGridPane(Labyrint l) {
		if(l!= null) {
		rutenett.getChildren().clear();
		Klikkbehandler klikk = new Klikkbehandler();
		for (int i = 0; i < l.getRowNum(); i++) {
			for (int o = 0; o < l.getColNum(); o++) {
				Grid a = new Grid();
				if(l.getColNum()>25 || l.getRowNum()>25) {
					a.setFont(new Font(5));
					a.setPrefSize(20, 20);
				}
				a.settMerke(Labyrint.getRute(o, i).getTegn());
				a.settRC(o, i);
				if (a.merke == '.') {
					//a.setStyle("-fx-background-color: #F3F3F2	");
					a.setOnAction(klikk);
					a.setStyle("-fx-padding: 1");
				} else {
					a.setStyle("-fx-background-color: #171412	");
				}
				rutenett.add(a, o, i);

			}
			info.setText("Please choose a white cell as a starting point:");

		}}
	}
//the given method return boolean
	public boolean[][] losningStringTilTabell(String losningString, int bredde, int hoyde) {
	    boolean[][] losning = new boolean[hoyde][bredde];
	    java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\(([0-9]+),([0-9]+)\\)");
	    java.util.regex.Matcher m = p.matcher(losningString.replaceAll("\\s",""));
	    while (m.find()) {
	        int x = Integer.parseInt(m.group(1));
	        int y = Integer.parseInt(m.group(2));
	        losning[y][x] = true;
	    }
	    return losning;
	}
//class Grid 
	class Grid extends Button {
		char merke = ' ';
		int row;
		int col;

		Grid() {
			super(" ");
			setFont(new Font(10));
			setPrefSize(30, 30);
		}

		void settMerke(char c) {
//	    setText(""+c);  
			merke = c;
		}
		void settRC(int c, int r) {
			row = r;
			col = c;
		}
	}
}