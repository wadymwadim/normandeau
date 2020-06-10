package billiards.viewer;

import billiards.codeseq.CodePair;
import billiards.codeseq.CodeSequence;
import billiards.codeseq.CodeType;
import billiards.codeseq.InitialAngles;
import billiards.codeseq.TriplePair;
import billiards.geometry.ConvexPolygon;
import billiards.geometry.LineSegment;
import billiards.geometry.Location;
import billiards.geometry.Rectangle;
import billiards.geometry.Point;
import billiards.math.CosEquation;
import billiards.math.CoverSquare;
import billiards.math.Equation;
import billiards.math.SinEquation;
import billiards.math.XYEta;
import billiards.math.XYZ;
import billiards.wrapper.CodeInfo;
import billiards.wrapper.Wrapper;

import javaslang.Tuple2;

import org.apache.commons.math3.util.FastMath;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;

import java.awt.MouseInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.DoubleUnaryOperator;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Affine;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public final class Viewer {

    // the time the tool tips take to open and close, in seconds
    private static final double TipOpenDelay = 2;
    private static final double TipCloseDelay = 20;

    private static final int SIZE = (700);

    // IMPORTANT: This is the color of the 90 and 80 lines and any additional lines
    private static final Color lineColor = Color.MAGENTA;//MAGENTA george

    final Color clickColor = Color.GOLD;
    final String textBoxColor = Utils.hex(Color.MISTYROSE);
    final Color plusColor = Color.LIGHTGREEN;
    final Color minusColor = Color.LIGHTCORAL;
    final Color panColor = Color.MAROON;
    final Color polyBoundColor = Color.LIME;
    final Color fillBoundColor = Color.YELLOW;
    final Color coverPolyBoundColor0 = Color.LIME;
    final Color coverPolyBoundColor1 = Color.AQUA;
    final Color coverAreaColor = Color.DARKORANGE;

    // All the JavaFX gui components are global, but the arrays and maps and such
    // that we use to help them out are created in the constructor and then passed
    // to the handlers as necessary

    Optional<String> currentCover = Optional.empty();

    final PixelRadianMap map = new PixelRadianMap(SIZE);

    // The map has the default viewing rectangle, and that is the beginning place to go
    final BackwardForward<Rectangle> viewRectangleBF = BackwardForward.create(map.getViewRectangle());

    // these are the code sequences that are currently on screen
    // we want to remember the ordering everything gets

    // No files by default
    ArrayList<CodeSequence> fileCodeSequences = new ArrayList<>();

    HashTriple coverRects = new HashTriple();

    Optional<ConvexPolygon> coverArea = Optional.empty();

    // GUI components

    // This gets passed in the constructor, and we initialize it there
    final Stage mainWindow;

    // main window
    final GridPane codeSequencesGPane = new GridPane();

    // the viewer image
    final StackPane imageStack = new StackPane();

    // static method, since the background is just white
    final ImageView backgroundImageView = renderColor(Color.WHITE);
    final ImageView guideLinesImageView = new ImageView();
    final ImageView regionsImageView = new ImageView();
    final ImageView boundsImageView = new ImageView();

    ArrayList<Storage> currentStorages = new ArrayList<>();
    ArrayList<ConvexPolygon> currentBounds = new ArrayList<>();
    Storage currentStorage;

    Color currentColor = Color.RED;

    // the regionIV is kept separate, because it allows us to redraw this
    // one without redrawing everything else
    final ImageView regionIV = new ImageView();

    // This one is transparent, and goes on top to capture all the mouse events
    final ImageView topImageView = renderColor(Color.TRANSPARENT);

   /* final MutableList<Point> infPatPoints = FastList.newListWith(
    		//Point.create(0, Math.PI / 2),
    		Point.create(22.49 * Math.PI / 180, 22.61 * Math.PI / 180),
    		Point.create(22.55 * Math.PI / 180, 22.55 * Math.PI / 180),
    		Point.create(22.49 * Math.PI / 180, 22.49 * Math.PI / 180));
    		//Point.create(0, 67.5 * Math.PI / 180));
    final ConvexPolygon infPatternArea = ConvexPolygon.create(infPatPoints.toImmutable());
    */
    
  /*  final MutableList<Point> infPatPoints2 = FastList.newListWith(
    		
	Point.create(224998 * Math.PI / 1800000, 225002 * Math.PI / 1800000),
	Point.create(225001 * Math.PI / 1800000, 225023 * Math.PI / 1800000),
	Point.create(225012 * Math.PI / 1800000, 225012 * Math.PI / 1800000),
	Point.create(22499999 * Math.PI / 180000000, 22499999 * Math.PI / 180000000));
	
    final ConvexPolygon infPatternArea2 = ConvexPolygon.create(infPatPoints2.toImmutable());
*///note this will grey out george may 31,2020
   
    
    final MutableList<Point> infPatPoints = FastList.newListWith(//george
    		Point.create(0, Math.PI / 2),
    		Point.create(22.5 * Math.PI / 180, 67.5 * Math.PI / 180),
    		Point.create(0, 67.5 * Math.PI / 180));
    final ConvexPolygon infPatternArea = ConvexPolygon.create(infPatPoints.toImmutable());

    // we use this list to keep track of which checkboxes are selected, since a new checkbox is
    // made each time we click somewhere.
    final int[] drawCBoxes = {-1, -1, -1};

    // right side box with mouse coordinates and code info
    // Degree
    final HBox textXHBox = new HBox();
    final Label textXLabel = new Label();
    final TextField textXField = new TextField();

    final HBox textYHBox = new HBox();
    final Label textYLabel = new Label();
    final TextField textYField = new TextField();

    // Lock
    final HBox textXLockHBox = new HBox();
    final Label textXLockLabel = new Label();
    final TextField textXLockField = new TextField();

    final HBox textYLockHBox = new HBox();
    final Label textYLockLabel = new Label();
    final TextField textYLockField = new TextField();

    final TextField xMinTextField = new TextField();
    final TextField xMaxTextField = new TextField();
    final TextField yMinTextField = new TextField();
    final TextField yMaxTextField = new TextField();

    final Button zoomButton = new Button();
    Color zoomColor = Color.RED;

    final RadioButton selectRdoBtn = new RadioButton();
    final RadioButton magnifyRdoBtn = new RadioButton();
    final RadioButton centerBtn = new RadioButton();
    final RadioButton demagnifyRdoBtn = new RadioButton();
    final ToggleGroup magnifyGroup = new ToggleGroup();

    final Label zoomScaleLabel = new Label();
    final TextField zoomScaleText = new TextField();
    final Button backwardSquareButton = new Button();
    final Button forwardSquareButton = new Button();

    final Button clearBtn = new Button();
    final Button resetBtn = new Button();
    final Button infoButton = new Button();

    final Button loadCoverBtn = new Button();
    final ComboBox<String> coversBox = new ComboBox<>();

    final Button checkCoverBtn = new Button();

    final Button checkOneBtn = new Button();
    final VBox checkOneWrap = new VBox(10);
    final TextArea checkOneInfo = new TextArea();

    final TextField labelMainWindow = new TextField();
    final Button coverBtn = new Button();
    final Button covRectsColorBox = new Button();
    final Map<ConvexPolygon, Color> mrrBounds = new HashMap<>();
    CoverSquare selectedRect = null;
    Color coverColor = Color.BLACK;

    final CheckBox coverColorCycle = new CheckBox();

    public Viewer(final Stage primaryStage) {
        // This gets passed in from the outside world
        mainWindow = primaryStage;

        final String windowTitle = String.format("Billiard Viewer 15copy2");//george may 22,2020

        Utils.setupCustomTooltipBehavior((int) (TipOpenDelay * 1000), (int) (TipCloseDelay * 1000), 200);

        zoomScaleLabel.setText("Zoom Scale:");
        zoomScaleText.setText("2");
        zoomScaleText.setTooltip(Utils.toolTip("The scale that you magnify and demagnify by"));
        zoomScaleText.setPrefWidth(55);
        zoomScaleText.setStyle(textBoxColor);

        backwardSquareButton.setText("Backward");
        backwardSquareButton.setTooltip(Utils.toolTip("Go to the last screen view you were at"));

        Utils.colorButton(backwardSquareButton, Color.SKYBLUE, clickColor);

        backwardSquareButton.setOnAction(event -> {

            viewRectangleBF.backward().ifPresent(rect -> {
                map.setViewRectangle(rect);
                renderRegions(guideLinesImageView, regionsImageView);
            });
        });

        forwardSquareButton.setText("Forward");
        forwardSquareButton.setTooltip(Utils.toolTip("Go to the next screen view you were at"));
        Utils.colorButton(forwardSquareButton, Color.SKYBLUE, clickColor);

        forwardSquareButton.setOnAction(event -> {

            viewRectangleBF.forward().ifPresent(rect -> {
                map.setViewRectangle(rect);
                renderRegions(guideLinesImageView, regionsImageView);
            });
        });

        // Create a new info window and show it
        infoButton.setText("Info");
        infoButton.setTooltip(Utils.toolTip("Brings up a window that will show you information about"
                                            + " a code sequence"));
        Utils.colorButton(infoButton, Color.LIGHTPINK, clickColor);

        infoButton.setOnAction(event -> new InfoWindow(windowTitle).show());


        coversBox.getItems().addAll("100-105", "105-110", "110-112", "112-112.1", "112.1-112.2", "112.2-112.3", "112.3-112.4", "112.3-112.4A", "112.3-112.4B", "112.3-112.4C", "112.3-112.4D", "112.3-112.4E", "112.3-112.4F", "112.3-112.4G", "112.3-112.4H", "112.3-112.4I", "112.3-112.4J", "112.3-112.4K", "112.3-112.4L", "12-14", "14-15", "15-17", "17-22.4988", "22.4988-33.8", "All");//george
        coversBox.setTooltip(Utils.toolTip("Select which cover will be loaded."));
        coversBox.setValue("100-105");
        Utils.colorButton(coversBox, Color.SKYBLUE, clickColor);

        loadCoverBtn.setText("Load Cover");
        Utils.colorButton(loadCoverBtn, Color.LIGHTPINK, clickColor);
        loadCoverBtn.setOnAction(e -> {

            final File dir;
            if (coversBox.getValue().equals("100-105")) {
                dir = new File("coversfolder/105cover/");

            } else if (coversBox.getValue().equals("105-110")) {
                dir = new File("coversfolder/110cover/");

            } else if (coversBox.getValue().equals("110-112")) {
                dir = new File("coversfolder/112cover/");

            } else if (coversBox.getValue().equals("112-112.1")) {
                dir = new File("coversfolder/112_1cover/");

            } else if (coversBox.getValue().equals("112.1-112.2")) {
                dir = new File("coversfolder/112_2cover/");

            } else if (coversBox.getValue().equals("112.2-112.3")) {
                dir = new File("coversfolder/112_3cover/");
                
            } else if (coversBox.getValue().equals("112.3-112.4")) {//george
                dir = new File("coversfolder/112_4cover/");
                
            } else if (coversBox.getValue().equals("112.3-112.4A")) {//george
                dir = new File("coversfolder/112_4coverA/");
                
            } else if (coversBox.getValue().equals("112.3-112.4B")) {//george
                dir = new File("coversfolder/112_4coverB/");
                
            } else if (coversBox.getValue().equals("112.3-112.4C")) {//george
                dir = new File("coversfolder/112_4coverC/");
                
            } else if (coversBox.getValue().equals("112.3-112.4D")) {//george
                dir = new File("coversfolder/112_4coverD/");
                
            } else if (coversBox.getValue().equals("112.3-112.4E")) {//george
                dir = new File("coversfolder/112_4coverE/");
                
            } else if (coversBox.getValue().equals("112.3-112.4F")) {//george
                dir = new File("coversfolder/112_4coverF/");
                
            } else if (coversBox.getValue().equals("112.3-112.4G")) {//george
                dir = new File("coversfolder/112_4coverG/");
                
            } else if (coversBox.getValue().equals("112.3-112.4H")) {//george
                dir = new File("coversfolder/112_4coverH/");
                
            } else if (coversBox.getValue().equals("112.3-112.4I")) {//george
                dir = new File("coversfolder/112_4coverI/");
                
            } else if (coversBox.getValue().equals("112.3-112.4J")) {//george
                dir = new File("coversfolder/112_4coverJ/");
                
            } else if (coversBox.getValue().equals("112.3-112.4K")) {//george
                dir = new File("coversfolder/112_4coverK/");
                
            } else if (coversBox.getValue().equals("112.3-112.4L")) {//george
                dir = new File("coversfolder/112_4coverL/");
                
            } else if (coversBox.getValue().equals("12-14")) {//george
                dir = new File("coversfolder/12-14cover/");
                
            } else if (coversBox.getValue().equals("14-15")) {//george
                dir = new File("coversfolder/14-15cover/");
                
            } else if (coversBox.getValue().equals("15-17")) {//george
                dir = new File("coversfolder/15-17cover/");
             
            } else if (coversBox.getValue().equals("17-22.4988")) {//george
                dir = new File("coversfolder/17-22.4988cover/");
                  
            } else if (coversBox.getValue().equals("22.4988-33.8")) {//george
                dir = new File("coversfolder/22.4988-33.8cover/");

            } else if (coversBox.getValue().equals("All")) {
                dir = new File("coversfolder/allcovers/");

            } else {
                final Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Load Cover");
                alert.setHeaderText("Error in Load");
                alert.setContentText("Cover folder or files might be missing");
                alert.showAndWait();
                return;
            }

            if (dir != null) {
            	clearBtn.fire();
                currentCover = Optional.of(dir.getPath());

                loadCoverAction(dir);
            }
        });

        checkCoverBtn.setText("Check Cover");
        Utils.colorButton(checkCoverBtn, Color.LIGHTPINK, clickColor);
        checkCoverBtn.setOnAction(e -> {

            if (currentCover.isPresent()) {

            	final Alert alert = new Alert(AlertType.CONFIRMATION);

                alert.setTitle("Check Cover");
                alert.setHeaderText("Check Cover");
                alert.setContentText("Checking a cover can take several hours or\n"
                		+ "even days. Continue?");
                final Optional<ButtonType> response = alert.showAndWait();
                if (response.isPresent() && response.get() == ButtonType.OK) {

                	try {
		                final ProcessBuilder builder = new ProcessBuilder("build/exe/cover/cover",
		                                                                  currentCover.get());
		                // Redirect the stdout and stderr so they are printed
		                builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		                builder.redirectError(ProcessBuilder.Redirect.INHERIT);

		                final Process process = builder.start();

				        process.waitFor();


	                } catch (final Exception ex) {
	                    new ErrorAlert(ex).showAndWait();
	                    return;
	                }
                }

            } else {
            	final Alert alert = new Alert(AlertType.INFORMATION);

                alert.setTitle("Cover");
                alert.setHeaderText("No cover loaded");
                alert.setContentText("Please load a cover before pressing this button.");

                alert.showAndWait();
            }
        });

        checkOneBtn.setText("Check this square");
        checkOneBtn.setTooltip(Utils.toolTip("Run our proof on one square"));
        Utils.colorButton(checkOneBtn, Color.LIGHTPINK, clickColor);
        checkOneBtn.setOnAction(event -> {
        	if (selectedRect != null) {
                try {
                    final CodeSequence codeSeq = currentStorage.classCodeSeq;
                    final InitialAngles angles = currentStorage.angles;

                    final Optional<String> string = Wrapper.checkSquare(selectedRect, codeSeq, angles, currentCover.get());
                    final Optional<CodeInfo> info = Wrapper.loadCodeInfo(new CodePair(codeSeq, angles));
                    final StringBuilder builder = new StringBuilder();
                    builder.append(string.get());
                    builder.append("\n\n");
                    builder.append("Equations of Exact Region\n");

                    for (final Equation equation : info.get().sinEquations) {
                        builder.append(equation);
                        builder.append("\n\n");
                    }

                    for (final Equation equation : info.get().cosEquations) {
                        builder.append(equation);
                        builder.append("\n\n");
                    }



                    checkOneInfo.setText(builder.toString());
                    
                   // System.out.print(builder.toString()); george aug 19,2019
                    
                    checkOneWrap.getChildren().add(checkOneInfo);
                    VBox.setVgrow(checkOneInfo, Priority.ALWAYS);

                } catch (Exception ex) {
                    new ErrorAlert(ex).show();
                }

        	} else {
        		final Alert alert = new Alert(AlertType.INFORMATION);

                alert.setTitle("Cover");
                alert.setHeaderText("No square selected");
                alert.setContentText("Please select a square before pressing this.");

                alert.showAndWait();
        	}
        });
        VBox.setVgrow(checkOneWrap, Priority.ALWAYS);

        labelMainWindow.setPrefWidth(80);

        labelMainWindow.setPromptText("Label");

        imageStack.getChildren().addAll(backgroundImageView, regionsImageView, guideLinesImageView,
                                        boundsImageView, regionIV, topImageView);

        // For input validation, you have two things to check
        // Check if the string is empty. This means the user entered no input
        // Then check if the string is valid

        resetBtn.setText("Reset");
        resetBtn.setTooltip(Utils.toolTip("Change the zoom level back to the default, like it was"
                                          + " when you first opened the program."));
        Utils.colorButton(resetBtn, Color.SKYBLUE, clickColor);

        resetBtn.setOnAction(event -> {
            map.reset();
            viewRectangleBF.add(map.getViewRectangle());
            renderRegions(guideLinesImageView, regionsImageView);
        });

        clearBtn.setText("Clear");
        clearBtn.setTooltip(Utils.toolTip("Clear everything from the screen  (The guidelines will be all that remains)"));

        Utils.colorButton(clearBtn, Color.SKYBLUE, clickColor);

        clearBtn.setOnAction(event -> {
            // reset the boxes on the right
            codeSequencesGPane.getChildren().clear();

            drawCBoxes[0] = -1;
            drawCBoxes[1] = -1;
            drawCBoxes[2] = -1;
            currentStorages.clear();
            selectedRect = null;
            checkOneWrap.getChildren().clear();
            coverRects.clear();
            coverArea = Optional.empty();
            regionsImageView.setImage(new WritableImage(SIZE, SIZE));
            boundsImageView.setImage(new WritableImage(SIZE, SIZE));

            regionIV.setImage(new WritableImage(SIZE, SIZE));
        });

        xMinTextField.setPromptText("X min");
        xMinTextField.setPrefColumnCount(8);
        xMinTextField.setStyle(textBoxColor);

        xMaxTextField.setPromptText("X max");
        xMaxTextField.setPrefColumnCount(8);
        xMaxTextField.setStyle(textBoxColor);

        yMinTextField.setPromptText("Y min");
        yMinTextField.setPrefColumnCount(8);
        yMinTextField.setStyle(textBoxColor);

        yMaxTextField.setPromptText("Y max");
        yMaxTextField.setPrefColumnCount(8);
        yMaxTextField.setStyle(textBoxColor);

        covRectsColorBox.setText("Black");
        covRectsColorBox.setTooltip(Utils.toolTip("The color used by the cover squares"));
        covRectsColorBox.setPrefWidth(120);
        covRectsColorBox.setOnAction(event -> {
            final int mouseY = MouseInfo.getPointerInfo().getLocation().y + 20;
            final int mouseX = MouseInfo.getPointerInfo().getLocation().x;
            final ColorPicker picker = new ColorPicker(mouseX, mouseY);
            final Optional<Color> opt = picker.pickColor();
            opt.ifPresent(color -> {
                coverColor = color;
                covRectsColorBox.setText(Colors.colorMap.get(color).get());
            });
        });

        zoomButton.setText("Zoom");
        zoomButton.setTooltip(Utils.toolTip(
            "Zoom to the interval specified. Note, if the interval is not a square, it will zoom"
            + " to the best fitting square of that interval. You may set the minX, maxX equal and"
            + " minY, maxY equal. If so, the program just centers those coordinates"));
        Utils.colorButton(zoomButton, Color.SKYBLUE, clickColor);

        zoomButton.setOnAction(event -> zoomAction());

        coverColorCycle.setText("Cycle");
        coverColorCycle.setTooltip(Utils.toolTip("Cycle through the top row of colors when clicking "
                                                 + "on the cover rectangles"));
        coverColorCycle.setStyle(textBoxColor);

        selectRdoBtn.setText("Select");
        selectRdoBtn.setSelected(true);
        magnifyRdoBtn.setText("Magnify");
        demagnifyRdoBtn.setText("Demagnify");
        centerBtn.setText("Center");
        selectRdoBtn.setStyle(textBoxColor);
        magnifyRdoBtn.setStyle(textBoxColor);
        demagnifyRdoBtn.setStyle(textBoxColor);
        centerBtn.setStyle(textBoxColor);
        centerBtn.setTooltip(Utils.toolTip("If you click the screen while this is selected, it will "
                                           + "pan so that the point you clicked is the new center of the screen"));

        selectRdoBtn.setToggleGroup(magnifyGroup);
        magnifyRdoBtn.setToggleGroup(magnifyGroup);
        demagnifyRdoBtn.setToggleGroup(magnifyGroup);
        centerBtn.setToggleGroup(magnifyGroup);

        // The topImageView is transparent and intercepts all the mouse events. This method allows
        // us to click on the transparent parts
        topImageView.setPickOnBounds(true);

        // handle panning and clicking events here
        topImageView.setOnMousePressed(event -> {
            final double initX = event.getX();
            final double initY = event.getY();
            final ImageView initLine = new ImageView();
            imageStack.getChildren().add(4, initLine);

            topImageView.setOnMouseDragged(event2 -> {
                final double finX = event2.getX();
                final double finY = event2.getY();
                final Optional<Line> panOpt = smartLine(initX, initY, finX, finY);
                if (panOpt.isPresent()) {
                	final Line panLine = panOpt.get();
	                panLine.setStroke(panColor);
	                imageStack.getChildren().remove(4);
	                imageStack.getChildren().add(4, panLine);
	                imageStack.getChildren().get(4).setTranslateX((finX + initX - SIZE) / 2);
	                imageStack.getChildren().get(4).setTranslateY((finY + initY - SIZE) / 2);
                }
            });
            topImageView.setOnMouseReleased(event3 -> {
                imageStack.getChildren().remove(4);
                pan(initX, initY, event3.getX(), event3.getY());
            });
        });

        textXLabel.setText("X:");
        textXField.setEditable(false);
        textXField.setPrefWidth(130);

        textYLabel.setText("Y:");
        textYField.setEditable(false);
        textYField.setPrefWidth(130);

        // Lock
        textXLockLabel.setText("X:");
        textXLockField.setEditable(false);
        textXLockField.setPrefWidth(130);

        textYLockLabel.setText("Y:");
        textYLockField.setEditable(false);
        textYLockField.setPrefWidth(130);

        topImageView.setOnMouseMoved(event -> {

            final double radianX = map.radianX(event.getX() + 0.5);
            final double radianY = map.radianY(event.getY() + 0.5);

            final double degreeX = Math.toDegrees(radianX);
            final double degreeY = Math.toDegrees(radianY);

            textXField.setText(Double.toString(degreeX));
            textYField.setText(Double.toString(degreeY));
        });

        final HBox minHBox = new HBox();
        minHBox.setSpacing(0);
        minHBox.getChildren().addAll(xMinTextField, yMinTextField);
        minHBox.setPadding(new Insets(0, 8, 8, 0));
        minHBox.setAlignment(Pos.CENTER);

        final HBox maxHBox = new HBox();
        maxHBox.setSpacing(0);
        maxHBox.getChildren().addAll(xMaxTextField, yMaxTextField);
        maxHBox.setPadding(new Insets(0, 8, 0, 0));
        maxHBox.setAlignment(Pos.CENTER);

        final VBox zoomFeildsVBox = new VBox();
        zoomFeildsVBox.setSpacing(8);
        zoomFeildsVBox.getChildren().addAll(minHBox, maxHBox);
        zoomFeildsVBox.setPadding(new Insets(0, 0, 8, 0));
        zoomFeildsVBox.setAlignment(Pos.CENTER);

        final HBox zoomHBox = new HBox();
        zoomHBox.setSpacing(8);
        zoomHBox.getChildren().addAll(zoomButton, zoomFeildsVBox);
        zoomHBox.setPadding(new Insets(0, 8, 0, 0));
        zoomHBox.setAlignment(Pos.CENTER);

        final HBox clickActionHBox = new HBox();
        clickActionHBox.setSpacing(8);
        clickActionHBox.getChildren().addAll(selectRdoBtn, magnifyRdoBtn, demagnifyRdoBtn, centerBtn);
        clickActionHBox.setPadding(new Insets(0, 8, 8, 0));
        clickActionHBox.setAlignment(Pos.CENTER);

        final HBox backForthHBox = new HBox();
        backForthHBox.setSpacing(8);
        backForthHBox.getChildren().addAll(
            zoomScaleLabel, zoomScaleText, backwardSquareButton, forwardSquareButton);
        backForthHBox.setPadding(new Insets(0, 8, 8, 0));
        backForthHBox.setAlignment(Pos.CENTER);

        final HBox windowHBox1 = new HBox(
            8, infoButton, clearBtn, coversBox, loadCoverBtn);
        windowHBox1.setPadding(new Insets(8, 8, 8, 0));
        windowHBox1.setAlignment(Pos.CENTER);

        final HBox windowHBox2 = new HBox(8, resetBtn, covRectsColorBox, coverColorCycle, checkCoverBtn);
        windowHBox2.setPadding(new Insets(0, 8, 8, 0));
        windowHBox2.setAlignment(Pos.CENTER);

        final Label mouseCoordinatesLabel = new Label("Mouse Coordinates");
        mouseCoordinatesLabel.setPadding(new Insets(0, 8, 8, 8));

        final VBox leftVBox = new VBox(8, windowHBox1, windowHBox2, zoomHBox, clickActionHBox,
                backForthHBox, mouseCoordinatesLabel, textXHBox, textYHBox, textXLockHBox,
                textYLockHBox, codeSequencesGPane, checkOneWrap);
        leftVBox.setPadding(new Insets(0, 10, 10, 10));

        textXHBox.getChildren().addAll(textXLabel, textXField, textYLabel, textYField);
        textXHBox.setSpacing(10);
        textXHBox.setAlignment(Pos.CENTER);

        textXLockHBox.getChildren().addAll(textXLockLabel, textXLockField, textYLockLabel, textYLockField);
        textXLockHBox.setSpacing(10);
        textXLockHBox.setAlignment(Pos.CENTER);

        // There are sort of two layers. There are all the gui elements that the
        // user interacts with. However, you can't directly use these when programming
        // So, you have variables behind the scene that represent the state of the gui.
        // Whenever the gui changes, these variables are updated automatically. Note
        // that all these changes must occur in one thread, since gui elements can
        // only be modified within the application thread

        codeSequencesGPane.setPadding(new Insets(0, 10, 0, 5));
        codeSequencesGPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        codeSequencesGPane.setHgap(10);
        codeSequencesGPane.setVgap(10);
        codeSequencesGPane.setPrefHeight(27);

        final HBox bpane = new HBox(10, leftVBox, imageStack);
        bpane.setAlignment(Pos.CENTER);

        // reflect
        final Affine reflectTransform = new Affine();
        reflectTransform.setMyy(-1);
        reflectTransform.setTy(imageStack.getBoundsInLocal().getHeight());
        imageStack.getTransforms().add(reflectTransform);

        // Scene
        final Scene scene = new Scene(bpane);

        // Stage
        mainWindow.setTitle(windowTitle);
        mainWindow.setOnCloseRequest(event -> {
            // close all the windows
            Platform.exit();
        });
        mainWindow.setScene(scene);
    }

    // Do initial rendering
    public void start() {
        renderRegions(guideLinesImageView, regionsImageView);
        mainWindow.show();
    }

    private void loadCoverAction(final File dir) {
        final String polygonString = Utils.readFromFile(dir + "/polygon.txt").trim();
        //final String squareString = Utils.readFromFile(dir + "/square.txt").trim();
        final String stablesString = Utils.readFromFile(dir + "/stables.txt").trim();
        final String triplesString = Utils.readFromFile(dir + "/triples.txt").trim();
        final String coverString = Utils.readFromFile(dir + "/cover.txt").trim();

        final ConvexPolygon polygon = Cover.parsePolygon(polygonString);
        final CoverSquare square = CoverSquare.initial(); // Cover.parseRectangle(squareString);
        final List<CodePair> stables = Cover.parseStables(stablesString);
        final List<TriplePair> triples = Cover.parseTriples(triplesString);
        final Tuple2<Map<CoverSquare, CodePair>, Map<CoverSquare, TriplePair>> cover = Cover.parseCover(coverString, square, stables, triples);

        // We can only load one cover at a time
        coverRects.clear();
        coverRects.addStables(cover._1, Color.BLACK);
        coverRects.addTriples(cover._2, Color.BLACK);
        coverArea = Optional.of(polygon);
        renderRegions(guideLinesImageView, regionsImageView);
    }

    private void zoomAction() {
        // so between 0 and pi, and min < max

        double xMin;
		double xMax;
		double yMin;
		double yMax;
		try {
			xMin = Math.toRadians(Double.parseDouble(xMinTextField.getText()));
			xMax = Math.toRadians(Double.parseDouble(xMaxTextField.getText()));
			yMin = Math.toRadians(Double.parseDouble(yMinTextField.getText()));
			yMax = Math.toRadians(Double.parseDouble(yMaxTextField.getText()));
		} catch (NumberFormatException e) {
			final Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Error");
			error.setHeaderText("Number Format Error");
			error.setContentText("Please check the zoom fields' input");
			error.showAndWait();
			return;
		}

        if (0 < xMin && xMin < xMax && xMax < Math.PI
         && 0 < yMin && yMin < yMax && yMax < Math.PI) {

        	if ((xMin == xMax) && (yMin == yMax)) {
                final double size = map.pixelSize();
                map.setTranslateY(yMin - (SIZE / 2) * size);

            } else {
                final double width = xMax - xMin;
                final double height = yMax - yMin;

                final double largest = height > width ? height : width;

                // a scale of 1 gives us a width of pi
                final double scale = Math.PI / largest;

                map.setScale(scale);

                final double size = map.pixelSize();
                map.setTranslateX((xMax + xMin) / 2 - (SIZE / 2) * size);
                map.setTranslateY((yMax + yMin) / 2 - (SIZE / 2) * size);
            }
            viewRectangleBF.add(map.getViewRectangle());
            renderRegions(guideLinesImageView, regionsImageView);
        }
    }

    private void pan(final double initX, final double initY, final double finX, final double finY) {
        if (Math.abs(finX - initX) > 5 || Math.abs(finY - initY) > 5) {
            map.translateXBy(map.radianX(initX) - map.radianX(finX));
            map.translateYBy(map.radianY(initY) - map.radianY(finY));

            viewRectangleBF.add(map.getViewRectangle());

            renderRegions(guideLinesImageView, regionsImageView);

        } else {
            click(initX, initY);
        }
    }

    private void click(final double pixelX, final double pixelY) {

        final double oldRadianX = map.radianX(pixelX + 0.5);
        final double oldRadianY = map.radianY(pixelY + 0.5);

        final double oldDegreeX = Math.toDegrees(oldRadianX);
        final double oldDegreeY = Math.toDegrees(oldRadianY);

        textXLockField.setText(Double.toString(oldDegreeX));
        textYLockField.setText(Double.toString(oldDegreeY));

        if (selectRdoBtn.isSelected()) {

            // now we get the current point
            final double rx = map.radianX(pixelX + 0.5);
            final double ry = map.radianY(pixelY + 0.5);

            final double radianX = rx;
            final double radianY = ry;

            // iterate over the onScreenSequences, and check which ones are positive


            // for the point. We want the codes on the right to be sorted from smallest
            // to largest, since that makes life much easier
            // map and filter would be really nice right now
            final SortedSet<Storage> selectedStorages = new TreeSet<>();
            Color color = Color.TRANSPARENT;
            selectedRect = null;
            checkOneWrap.getChildren().clear();

            currentStorages.clear();
            currentBounds.clear();
            currentColor = color;

            if (infPatternArea.location(rx, ry) == Location.INSIDE) {

            	int n = 1;
            	while (!(0 < 3 * Math.PI / 2 - (n + 2)*rx - 2*ry
            			  && 3 * Math.PI / 2 - (n + 2)*rx - 2*ry < Math.PI / 2)) {
            		n += 1;
            	}

                final MutableIntList codeList = IntArrayList.newListWith(1, 1, 2*n+1, 1, 2, 1, 2*n+1, 1, 1, 4*n+2);
                final MutableIntList unstList1 = IntArrayList.newListWith(1, 2, 1, 2*n);
                final MutableIntList unstList2 = IntArrayList.newListWith(1, 2, 1, 2*n+2);

                final CodeSequence stabCode = new CodeSequence(codeList);
                final CodeSequence unstCode1 = new CodeSequence(unstList1);
                final CodeSequence unstCode2 = new CodeSequence(unstList2);

                final Storage stable = loadStorage(new CodePair(stabCode, new InitialAngles(XYZ.Z, XYZ.Y))).get();
                final Storage unst1 = loadStorage(new CodePair(unstCode1, new InitialAngles(XYZ.Z, XYZ.Y))).get();
                final Storage unst2 = loadStorage(new CodePair(unstCode2, new InitialAngles(XYZ.Z, XYZ.Y))).get();

                selectedStorages.add(unst1);
                selectedStorages.add(stable);
                selectedStorages.add(unst2);

            } else {
				for (final CoverSquare rect : coverRects.stableEntrySet()) {
					if (rect.contains(radianX, radianY)) {
						final CodePair codeSeq = coverRects.getStable(rect);
						final Optional<Storage> optional = loadStorage(codeSeq);
						if (optional.isPresent()) {

							selectedRect = rect;
							checkOneWrap.getChildren().add(checkOneBtn);

							if (coverColorCycle.isSelected() && !coverColor.equals(Color.TRANSPARENT)) {
								coverColor = ColorPicker.next(coverColor);
								covRectsColorBox.setText(Colors.colorMap.get(coverColor).get());
							}

							final Storage.Stable stable = (Storage.Stable) optional.get();
							selectedStorages.add(stable);
                            currentStorage = stable;

							if (coverColor.equals(Color.TRANSPARENT)) {
								color = coverRects.getColor(rect);
							} else {
								color = coverColor;
							}

							for (final CoverSquare rect2 : coverRects.stableEntrySet()) {
								final CodePair codeSeq2 = coverRects.getStable(rect2);
								final Color covColor;
								if (color.equals(Color.TRANSPARENT)) {
									covColor = coverRects.getColor(rect2);
								} else {
									covColor = color;
								}
								if (codeSeq.equals(codeSeq2)) {
									coverRects.put(rect2, covColor);
								}
							}
						}
						break;
					}
				}
				for (final CoverSquare rect : coverRects.tripleEntrySet()) {
					if (rect.contains(radianX, radianY)) {
						final TriplePair codeSeq = coverRects.getTriple(rect);
						final Optional<Storage> optNeg = loadStorage(codeSeq.stableNeg);
						final Optional<Storage> optUnst = loadStorage(codeSeq.unstable);
						final Optional<Storage> optPos = loadStorage(codeSeq.stablePos);
						if (optNeg.isPresent() && optUnst.isPresent() && optPos.isPresent()) {

							selectedRect = rect;
							checkOneWrap.getChildren().add(checkOneBtn);

							if (coverColorCycle.isSelected() && !coverColor.equals(Color.TRANSPARENT)) {
								coverColor = ColorPicker.next(coverColor);
								covRectsColorBox.setText(Colors.colorMap.get(coverColor).get());
							}
							final Storage.Stable stableNeg = (Storage.Stable) optNeg.get();
							final Storage.Unstable unstable = (Storage.Unstable) optUnst.get();
							final Storage.Stable stablePos = (Storage.Stable) optPos.get();
							selectedStorages.add(stableNeg);
							selectedStorages.add(unstable);
							selectedStorages.add(stablePos);

							if (coverColor.equals(Color.TRANSPARENT)) {
								color = coverRects.getColor(rect);
							} else {
								color = coverColor;
							}

							for (final CoverSquare rect2 : coverRects.tripleEntrySet()) {
								final TriplePair codeSeq2 = coverRects.getTriple(rect2);
								final Color covColor;
								if (color.equals(Color.TRANSPARENT)) {
									covColor = coverRects.getColor(rect2);
								} else {
									covColor = color;
								}
								if (codeSeq.equals(codeSeq2)) {
									coverRects.put(rect2, covColor);
								}
							}
						}
						break;
					}
				}
			}
			makeRightScrollPane(selectedStorages, color);
            renderRegions(guideLinesImageView, regionsImageView);

        } else {
            final double zoom = Double.parseDouble(zoomScaleText.getText());

            if (magnifyRdoBtn.isSelected()) {
                map.scaleBy(zoom);
            } else if (demagnifyRdoBtn.isSelected()) {
                map.scaleBy(1 / zoom);
            } else if (centerBtn.isSelected()) {
                map.scaleBy(1);
            } else {
            	new ErrorAlert(new RuntimeException("Somehow, no click "
            			+ "setting was selected.")).showAndWait();
                return;
            }

            final double newRadianX = map.radianX(SIZE / 2 + 0.5);
            final double newRadianY = map.radianY(SIZE / 2 + 0.5);

            map.translateXBy(oldRadianX - newRadianX);
            map.translateYBy(oldRadianY - newRadianY);

            // we want map(mouse click) == zoomed map(center)

            viewRectangleBF.add(map.getViewRectangle());

            renderRegions(guideLinesImageView, regionsImageView);
        }
    }

    private void makeRightScrollPane(final SortedSet<Storage> selectedStorages, final Color color) {
        // remove any listings already there
        codeSequencesGPane.getChildren().clear();
        int row = 0;

        for (final Storage storage : selectedStorages) {
        	final int finalRow = row;
        	final CheckBox drawCBox = new CheckBox();

            drawCBox.setText("Draw");
            drawCBox.setStyle(textBoxColor);
            drawCBox.setAllowIndeterminate(true);
            drawCBox.setIndeterminate(drawCBoxes[finalRow] == 0);
            drawCBox.setSelected(drawCBoxes[finalRow] == 1);

            if (drawCBox.isSelected() || drawCBox.isIndeterminate()) {
            	currentStorages.add(storage);
                currentColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.5);
            }

            if (drawCBox.isSelected() && CodeType.isStable(storage.type)) {
            	final Storage.Stable stable = (Storage.Stable) storage;
            	currentBounds.add(stable.polygon);
            }

            final String codeString = storage.toString();
            final TextField lblCodeSequence = new TextField(codeString.split(",")[0]);
            final Label codeInfo = new Label();
            codeInfo.setText(storage.type + " (" + storage.classCodeSeq.length() + "," + storage.classCodeSeq.sum() + ")");
            codeInfo.setPadding(new Insets(5, 5, 5, 0));
            lblCodeSequence.setPrefWidth(100);
            lblCodeSequence.setEditable(false);

            drawCBox.setOnAction(event -> {
            	currentStorages.remove(storage);
                if (CodeType.isStable(storage.type)) {
                	currentBounds.remove(((Storage.Stable) storage).polygon);
                }
                if (drawCBox.isSelected()) {
                	drawCBoxes[finalRow] = 1;
                    currentStorages.add(storage);
                    currentColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.5);
                    if (CodeType.isStable(storage.type)) {
	                	final Storage.Stable stable = (Storage.Stable) storage;
	                	currentBounds.add(stable.polygon);
                	}
                } else if (drawCBox.isIndeterminate()) {
                	drawCBoxes[finalRow] = 0;
                	currentStorages.add(storage);
                    currentColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.5);
                } else {
                	drawCBoxes[finalRow] = -1;
                }
                renderRegions(guideLinesImageView, regionsImageView);
            });

            final HBox codeInfoHBox = new HBox(10);
            codeInfoHBox.getChildren().addAll(codeInfo, lblCodeSequence, drawCBox);
            codeInfoHBox.setAlignment(Pos.CENTER_LEFT);

            codeSequencesGPane.addRow(row, codeInfoHBox);
            row += 1;
        }
        renderRegions(guideLinesImageView, regionsImageView);
    }

    private static void setImageColor(final WritableImage image, final Color color) {
        final PixelWriter pixelWriter = image.getPixelWriter();

        for (int pixelX = 0; pixelX < SIZE; pixelX += 1) {
            for (int pixelY = 0; pixelY < SIZE; pixelY += 1) {
                pixelWriter.setColor(pixelX, pixelY, color);
            }
        }
    }

    private static ImageView renderColor(final Color color) {
        final WritableImage image = new WritableImage(SIZE, SIZE);

        setImageColor(image, color);

        final ImageView imageView = new ImageView(image);

        return imageView;
    }

    // NOTE: static functions don't rely on UI elements, since all the UI elements are non-static
    // the storages list already has newest first
    // Static functions are good, because they are more thread-safer

    private void renderRegions(final ImageView guideLinesImageView, final ImageView regionsImageView) {
        final WritableImage guideLinesImage = renderGuideLines();
        guideLinesImageView.setImage(guideLinesImage);

        final WritableImage regionImage = new WritableImage(SIZE, SIZE);

        for (final CoverSquare rect : coverRects.stableEntrySet()) {
            renderCovSqr(rect, regionImage, coverRects.getColor(rect), Color.FIREBRICK);
        }
        for (final CoverSquare rect : coverRects.tripleEntrySet()) {
        	renderCovSqr(rect, regionImage, coverRects.getColor(rect), Color.FIREBRICK);
        }
        if (selectedRect != null) {
        	renderCovSqr(selectedRect, regionImage, coverRects.getColor(selectedRect), Color.WHITE);
        }
        regionsImageView.setImage(regionImage);

        final WritableImage boundsImage = new WritableImage(SIZE, SIZE);
        if (coverArea.isPresent()) {
            renderPolygon(coverArea.get(), boundsImage, coverAreaColor);
        }

        boundsImageView.setImage(boundsImage);

        final WritableImage oboImage = new WritableImage(SIZE, SIZE);

        for (final Storage currentStorage : currentStorages) {
            renderRegion(currentStorage, oboImage, currentColor);
        }
        for (ConvexPolygon poly : currentBounds) {
        	renderPolygonShell(poly, oboImage, currentColor.brighter());
        }

        regionIV.setImage(oboImage);
    }

    private void renderPolygonShell(
            final ConvexPolygon poly, final WritableImage image, final Color color) {
            final PixelWriter pixelWriter = image.getPixelWriter();

            final ImmutableList<Point> vertices = poly.vertices;
            final int size = vertices.size();

            for (int i = 0; i < size; ++i) {
                final Point a = vertices.get(i);
                final Point b = vertices.get((i + 1) % size);

                // horizontal
                if (a.y == b.y) {
                    drawHorizontalLine(
                    		a.y, Math.min(a.x, b.x), Math.max(a.x, b.x), pixelWriter, color, true);
                }
                // vertical
                else if (a.x == b.x) {
                    drawVerticalLine(
                    		a.x, Math.min(a.y, b.y), Math.max(a.y, b.y), pixelWriter, color, true);
                }
                // diagonal
                else {
                    final double slopeY = (b.y - a.y) / (b.x - a.x);
                    final DoubleUnaryOperator funcY = x -> slopeY * (x - a.x) + a.y; // y(x)

                    final double slopeX = (b.x - a.x) / (b.y - a.y);
                    final DoubleUnaryOperator funcX = y -> slopeX * (y - a.y) + a.x; // x(y)

                    drawObliqueLine(funcY, Math.min(a.x, b.x), Math.max(a.x, b.x), funcX,
                             Math.min(a.y, b.y), Math.max(a.y, b.y), pixelWriter, color, true);
                }
            }
        }


    private void drawHorizontalLine(final double y, final double x1, final double x2,
                 final PixelWriter pixelWriter, final Color color, final boolean thicken) {
        final int pixelY = (int) map.pixelY(y);

        if (0 <= pixelY && pixelY < SIZE) {
            for (int pixelX = 0; pixelX < SIZE; pixelX += 1) {
                final double radianX = map.radianX(pixelX + 0.5);
                if (x1 <= radianX && radianX <= x2) {
                    pixelWriter.setColor(pixelX, pixelY, color);
                    if ((pixelY + 1 < SIZE && pixelY - 1 >= 0) && thicken) {
                    	pixelWriter.setColor(pixelX, pixelY + 1, color);
                        pixelWriter.setColor(pixelX, pixelY - 1, color);
                    }
                }
            }
        }
    }

    // x, y1, y2 are in radians
    private void drawVerticalLine(final double x, final double y1, final double y2,
               final PixelWriter pixelWriter, final Color color, final boolean thicken) {
        final int pixelX = (int) map.pixelX(x);

        if (0 <= pixelX && pixelX < SIZE) {
            for (int pixelY = 0; pixelY < SIZE; pixelY += 1) {
                final double radianY = map.radianY(pixelY + 0.5);
                if (y1 <= radianY && radianY <= y2) {
                    pixelWriter.setColor(pixelX, pixelY, color);
                    if ((pixelX + 1 < SIZE && pixelX - 1 >= 0) && thicken) {
                    	pixelWriter.setColor(pixelX + 1, pixelY, color);
                        pixelWriter.setColor(pixelX - 1, pixelY, color);
                    }
                }
            }
        }
    }

    private void drawObliqueLine(final DoubleUnaryOperator y, final double x1, final double x2,
                                 final DoubleUnaryOperator x, final double y1, final double y2,
                                 final PixelWriter pixelWriter, final Color color, final boolean thicken) {
        // let's iterate across the x values
        for (int pixelX = 0; pixelX < SIZE; pixelX += 1) {
            final double radianX = map.radianX(pixelX);
            final double radianY = y.applyAsDouble(radianX);

            if (y1 <= radianY && radianY <= y2) {
                final int pixelY = (int) map.pixelY(radianY);

                if (0 <= pixelY && pixelY < SIZE) {
                    pixelWriter.setColor(pixelX, pixelY, color);
                    if (thicken && (1 <= pixelY && pixelY < SIZE - 1)) {
	                    pixelWriter.setColor(pixelX, pixelY + 1, color);
	                    pixelWriter.setColor(pixelX, pixelY - 1, color);
                    }
                }
            }
        }

        // now iterate over the rows
        for (int pixelY = 0; pixelY < SIZE; pixelY += 1) {
            final double radianY = map.radianY(pixelY);
            final double radianX = x.applyAsDouble(radianY);

            // is it part of the line segment?
            if (x1 <= radianX && radianX <= x2) {
                final int pixelX = (int) map.pixelX(radianX);

                // is it on screen?
                if (0 <= pixelX && pixelX < SIZE) {
                    pixelWriter.setColor(pixelX, pixelY, color);
                    if (thicken && (1 <= pixelX && pixelX < SIZE - 1)) {
	                    pixelWriter.setColor(pixelX + 1, pixelY, color);
	                    pixelWriter.setColor(pixelX - 1, pixelY, color);
                    }
                }
            }
        }
    }

    private WritableImage renderGuideLines() {
        // render the lines in the background image
        final WritableImage image = new WritableImage(SIZE, SIZE);
        final PixelWriter pixelWriter = image.getPixelWriter();

       //  we consider the infinite pattern area as part of the background
        for (int i = 0; i < SIZE; i++) {
        	for (int j = 0; j < SIZE; j++) {
        		final double rx = map.radianX(i);
        		final double ry = map.radianY(j);
        		if (infPatternArea.location(rx, ry) == Location.INSIDE) {
            		pixelWriter.setColor(i, j, Color.LIGHTGREY);//george may 20,2020 this gives the infinite corner
        		}
            	if (ry > 67.5 * Math.PI / 180 && (rx + ry) < Math.PI / 2 && rx > 0) {
            	}
            }
        }
        
        // we consider the infinite pattern area as part of the background
      /*  for (int i = 0; i < SIZE; i++) {
        	for (int j = 0; j < SIZE; j++) {
        		final double rx = map.radianX(i);
        		final double ry = map.radianY(j);
        		if (infPatternArea2.location(rx, ry) == Location.INSIDE) {
            		pixelWriter.setColor(i, j, Color.LIGHTGREY);//george may 20,2020 this gives the infinite corner
        		}
            	if (rx > 22.4998 * Math.PI / 180 && (rx + ry) < 45.0024 * Math.PI / 180 && ry > 22.499999 * Math.PI /180) {
            	}
            }
        }*/ //george may31,2020 this will grey out
        
     // we consider the infinite pattern area as part of the background
       /* for (int i = 0; i < SIZE; i++) {
        	for (int j = 0; j < SIZE; j++) {
        		final double rx = map.radianX(i);
        		final double ry = map.radianY(j);
        		if (infPatternArea.location(rx, ry) == Location.INSIDE) {
            		pixelWriter.setColor(i, j, Color.LIGHTGREY);//george may 20,2020 this gives the infinite corner
        		}
            	if (rx > 22.49 * Math.PI / 180 && (rx + ry) < 90 * Math.PI / 180 && ry > 22.49 * Math.PI /180) {
            	}
            }
        }*/

        // we have several horizontal lines, several vertical lines, and oblique ones
        drawHorizontalLine(0, 0, Math.PI, pixelWriter, lineColor, false);
        drawVerticalLine(0, 0, Math.PI, pixelWriter, lineColor, false);

        drawHorizontalLine(Math.PI / 2, 0, Math.PI / 2, pixelWriter, lineColor, false);
        drawVerticalLine(Math.PI / 2, 0, Math.PI / 2, pixelWriter, lineColor, false);

        // x + y = 90
        drawObliqueLine(x-> Math.PI / 2 - x,
                 0, Math.PI / 2, y -> Math.PI / 2 - y, 0, Math.PI / 2, pixelWriter, lineColor, false);

        // x + y = 180
        drawObliqueLine(
            x -> Math.PI - x, 0, Math.PI, y -> Math.PI - y, 0, Math.PI, pixelWriter, lineColor, false);

        // IMPORTANT: This is the line x + y = 80
        drawObliqueLine(x
                        -> 4 * Math.PI / 9 - x,
                        0, 4 * Math.PI / 9,
                        y -> 4 * Math.PI / 9 - y, 0, 4 * Math.PI / 9, pixelWriter, lineColor, false);

        // IMPORTANT: This is the line x + y = 75
        drawObliqueLine(x
                        -> 15 * Math.PI / 36 - x,
                        0, 15 * Math.PI / 36,
                        y -> 15 * Math.PI / 36 - y, 0, 15 * Math.PI / 36, pixelWriter, lineColor, false);

        // IMPORTANT: This is the line x + y = 70
        drawObliqueLine(x
                        -> 7 * Math.PI / 18 - x,
                        0, 7 * Math.PI / 18,
                        y -> 7 * Math.PI / 18 - y, 0, 7 * Math.PI / 18, pixelWriter, lineColor, false);

        // IMPORTANT: This is the line x + y = 68
        drawObliqueLine(x
                        -> 17 * Math.PI / 45 - x,
                        0, 17 * Math.PI / 45,
                        y -> 17 * Math.PI / 45 - y, 0, 17 * Math.PI / 45, pixelWriter, lineColor, false);
        
        // IMPORTANT: This is the line x + y = 67.7 //george
        drawObliqueLine(x
                        -> 677 * Math.PI / 1800 - x,
                        0, 677 * Math.PI / 1800,
                        y -> 677 * Math.PI / 1800 - y, 0, 677 * Math.PI / 1800, pixelWriter, lineColor, false);
  
        
        // IMPORTANT: This is the line x + y = 67.6 //george
        drawObliqueLine(x
                        -> 169 * Math.PI / 450 - x,
                        0, 169 * Math.PI / 450,
                        y -> 169 * Math.PI / 450 - y, 0, 169 * Math.PI / 450, pixelWriter, lineColor, false);

        // IMPORTANT: This is the line x + y = 28 //george
        drawObliqueLine(x
                        -> 280 * Math.PI / 1800 - x,
                        0, 280 * Math.PI / 1800,
                        y -> 280 * Math.PI / 1800 - y, 0, 280 * Math.PI / 1800, pixelWriter, lineColor, false);
  
        // IMPORTANT: This is the line x + y = 30 //george
        drawObliqueLine(x
                        -> 300 * Math.PI / 1800 - x,
                        0, 300 * Math.PI / 1800,
                        y -> 300 * Math.PI / 1800 - y, 0, 300 * Math.PI / 1800, pixelWriter, lineColor, false);

        // IMPORTANT: This is the line x + y = 34 //george
        drawObliqueLine(x
                        -> 340 * Math.PI / 1800 - x,
                        0, 340 * Math.PI / 1800,
                        y -> 340 * Math.PI / 1800 - y, 0, 340 * Math.PI / 1800, pixelWriter, lineColor, false);

        // IMPORTANT: This is the line x + y = 44.9976 //george
        drawObliqueLine(x
                        -> 449976 * Math.PI / 1800000 - x,
                        0, 449976 * Math.PI / 1800000,
                        y -> 449976 * Math.PI / 1800000 - y, 0, 449976 * Math.PI / 1800000, pixelWriter, lineColor, false);
 
        // IMPORTANT: This is the line x + y = 45 //george
        drawObliqueLine(x
                        -> 450 * Math.PI / 1800 - x,
                        0, 450 * Math.PI / 1800,
                        y -> 450 * Math.PI / 1800 - y, 0, 450 * Math.PI / 1800, pixelWriter, lineColor, false);
 
        // IMPORTANT: This is the line x + y = 45.0024 //george
        drawObliqueLine(x
                        -> 450024 * Math.PI / 1800000 - x,
                        0, 450024 * Math.PI / 1800000,
                        y -> 450024 * Math.PI / 1800000 - y, 0, 450024 * Math.PI / 1800000, pixelWriter, lineColor, false);
 
        
        // IMPORTANT: This is the line x = y
        drawObliqueLine(x -> x, 0, Math.PI / 4, y -> y, 0, Math.PI / 4, pixelWriter, lineColor, false);

     // x = 12 degrees George may 25,2020
        drawVerticalLine(Math.PI / 15, Math.PI / 15, 37 * Math.PI / 120, pixelWriter, lineColor, false);

        // x = 1 degrees George may 25,2020
        drawVerticalLine(Math.PI / 180, Math.PI / 180, Math.PI / 2, pixelWriter, lineColor, false);

        // x = .5 degrees George may 25,2020
        drawVerticalLine(Math.PI / 360, Math.PI / 360, Math.PI / 2, pixelWriter, lineColor, false);

        // x = .425 degrees George may 25,2020
        drawVerticalLine(425*Math.PI / 180000, 425*Math.PI / 180000, Math.PI / 2, pixelWriter, lineColor, false);

        // x = .3625 degrees George may 25,2020
        drawVerticalLine(3625*Math.PI / 1800000, 3625*Math.PI / 1800000, Math.PI / 2, pixelWriter, lineColor, false);

        // x = .35 degrees George may 25,2020
        drawVerticalLine(35*Math.PI / 18000, 35*Math.PI / 18000, Math.PI / 2, pixelWriter, lineColor, false);

        // x = .325 degrees George may 25,2020
        drawVerticalLine(325*Math.PI / 180000, 325*Math.PI / 180000, Math.PI / 2, pixelWriter, lineColor, false);

        // x = .295 degrees George may 25,2020
        drawVerticalLine(295*Math.PI / 180000, 295*Math.PI / 180000, Math.PI / 2, pixelWriter, lineColor, false);

        // x = .26 degrees George may 25,2020
        drawVerticalLine(26*Math.PI / 18000, 26*Math.PI / 18000, Math.PI / 2, pixelWriter, lineColor, false);

        // x = .23 degrees George may 25,2020
        drawVerticalLine(23*Math.PI / 18000, 23*Math.PI / 18000, Math.PI / 2, pixelWriter, lineColor, false);

        // x = .18 degrees George may 25,2020
        drawVerticalLine(18*Math.PI / 18000, 18*Math.PI / 18000, Math.PI / 2, pixelWriter, lineColor, false);

        // x = .13 degrees George may 25,2020
        drawVerticalLine(13*Math.PI / 18000, 13*Math.PI / 18000, Math.PI / 2, pixelWriter, lineColor, false);

        return image;
    }

    private void renderPolygon(
        final ConvexPolygon poly, final WritableImage image, final Color color) {
        final PixelWriter pixelWriter = image.getPixelWriter();

        final ImmutableList<Point> vertices = poly.vertices;
        final int size = vertices.size();

        for (int i = 0; i < size; ++i) {
            final Point a = vertices.get(i);
            final Point b = vertices.get((i + 1) % size);

            // horizontal
            if (a.y == b.y) {
                drawHorizontalLine(
                		a.y, Math.min(a.x, b.x), Math.max(a.x, b.x), pixelWriter, color, false);
            }
            // vertical
            else if (a.x == b.x) {
                drawVerticalLine(
                		a.x, Math.min(a.y, b.y), Math.max(a.y, b.y), pixelWriter, color, false);
            }
            // diagonal
            else {
                final double slopeY = (b.y - a.y) / (b.x - a.x);
                final DoubleUnaryOperator funcY = x -> slopeY * (x - a.x) + a.y; // y(x)

                final double slopeX = (b.x - a.x) / (b.y - a.y);
                final DoubleUnaryOperator funcX = y -> slopeX * (y - a.y) + a.x; // x(y)

                drawObliqueLine(funcY, Math.min(a.x, b.x), Math.max(a.x, b.x), funcX,
                                Math.min(a.y, b.y), Math.max(a.y, b.y), pixelWriter, color, false);
            }
        }
    }

    /*
    private void renderRect(final Rectangle rect, final WritableImage image,
                            final Color colorInside, final Color colorBound) {
        final PixelWriter pixelWriter = image.getPixelWriter();

        final int startPX = Math.max((int) map.pixelX(rect.intervalX.min), 0);
        final int startPY = Math.max((int) map.pixelY(rect.intervalY.min), 0);
        final int endPX = Math.min((int) map.pixelX(rect.intervalX.max), SIZE);
        final int endPY = Math.min((int) map.pixelY(rect.intervalY.max), SIZE);

        for (int i = startPX; i <= endPX; i++) {
            for (int j = startPY; j <= endPY; j++) {
                if (SIZE > i && i >= 0 && SIZE > j && j >= 0) {
                    if ((i == startPX || i == endPX || j == startPY || j == endPY)) {
                        try {
                            pixelWriter.setColor(i, j, colorBound);
                        } catch (final IndexOutOfBoundsException e) {
                            // do nothing
                        }

                    } else {
                        pixelWriter.setColor(i, j, colorInside);
                    }
                }
            }
        }
    }
    */

    private void renderCovSqr(final CoverSquare cov, final WritableImage image,
    		final Color colorInside, final Color colorBound) {
    	final PixelWriter pixelWriter = image.getPixelWriter();
    	final double[] bounds =  cov.bounds();
        final int startPX = Math.max((int) map.pixelX(bounds[0]), 0);
        final int endPX = Math.min((int) map.pixelX(bounds[1]), SIZE);
        final int startPY = Math.max((int) map.pixelY(bounds[2]), 0);
        final int endPY = Math.min((int) map.pixelY(bounds[3]), SIZE);

        for (int i = startPX; i <= endPX; i++) {
            for (int j = startPY; j <= endPY; j++) {
                if (SIZE > i && i >= 0 && SIZE > j && j >= 0) {
                    if ((i == startPX || i == endPX || j == startPY || j == endPY)) {
                        try {
                            pixelWriter.setColor(i, j, colorBound);
                        } catch (final IndexOutOfBoundsException e) {
                            // do nothing
                        }

                    } else {
                        pixelWriter.setColor(i, j, colorInside);
                    }
                }
            }
        }
    }

    private void renderUnstable(final Storage.Unstable unstable, final PixelWriter pixelWriter,
                                final Rectangle viewRectangle, final Color initColor) {
        final Color color;
        // this is the standard color:
        if (initColor.equals(new Color(
                Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), 0.5))) {
            color = Color.RED;
        } else {
            color = initColor;
        }

        final List<Point> points = new ArrayList<>();

        final int xCoeff = unstable.constraint.coeff(XYEta.X);
        final int yCoeff = unstable.constraint.coeff(XYEta.Y);
        final int etaCoeff = unstable.constraint.coeff(XYEta.Eta);

        final int startPixelX = 0;
        final int startPixelY = 0;
        final int endPixelX = SIZE;
        final int endPixelY = SIZE;

        if (xCoeff == 0) {
            // horizontal line
            // solve for y
            final double y = -(double) etaCoeff / yCoeff * Math.PI / 2;

            if (viewRectangle.intervalY.contains(y)) {
                // now we iterate over the vertical lines
                for (int i = startPixelX; i < endPixelX; i += 1) {
                    final double radX = map.radianX(i);
                    final Point point = Point.create(radX, y);
                    points.add(point);
                }
            }
        } else if (yCoeff == 0) {
            // vertical line
            // solve for x
            final double x = -(double) etaCoeff / xCoeff * Math.PI / 2;

            if (viewRectangle.intervalX.contains(x)) {
                // now iterate over the horizontal lines
                for (int i = startPixelY; i < endPixelY; i += 1) {
                    final double radY = map.radianY(i);
                    final Point point = Point.create(x, radY);
                    points.add(point);
                }
            }
        } else {
            // oblique line
            // we can solve for x and y
            final DoubleUnaryOperator x = radY
                -> - (double) yCoeff / xCoeff * radY - (double) etaCoeff / (double) xCoeff * Math.PI / 2;

            final DoubleUnaryOperator y = radX
                -> - (double) xCoeff / yCoeff * radX - (double) etaCoeff / (double) yCoeff * Math.PI / 2;

            for (int i = startPixelX; i < endPixelX; i += 1) {
                final double radX = map.radianX(i);
                final double radY = y.applyAsDouble(radX);
                // need to make sure intersection is in the viewing box
                if (viewRectangle.intervalY.contains(radY)) {
                    final Point point = Point.create(radX, radY);
                    points.add(point);
                }
            }

            for (int i = startPixelY; i < endPixelY; i += 1) {
                final double radY = map.radianY(i);
                final double radX = x.applyAsDouble(radY);
                if (viewRectangle.intervalX.contains(radX)) {
                    final Point point = Point.create(radX, radY);
                    points.add(point);
                }
            }

            // now sort the points in lexicographical order
            final Comparator<Point> ordering = (a, b) -> {
                final double ax = a.x;
                final double ay = a.y;

                final double bx = b.x;
                final double by = b.y;

                if ((ax == bx) && (ay == by)) {
                    // a == b
                    return 0;
                } else if ((ax < bx) || (ax == bx && ay < by)) {
                    // a < b
                    return -1;
                } else {
                    // a > b
                    return 1;
                }
            };

            points.sort(ordering);
        }

        double startX = unstable.lineSegment.start.x;
        double endX = unstable.lineSegment.end.x;
        double startY = unstable.lineSegment.start.y;
        double endY = unstable.lineSegment.end.y;

        if (startX == endX) {
            startX = startX - 0.01;
            endX = endX + 0.01;
        } else if (startY == endY) {
            startY = startY - 0.01;
            endY = endY + 0.01;
        }

        final Rectangle boundingRectangle = Rectangle.create(startX, endX, startY, endY);

        for (int i = 0; i < points.size() - 1; i += 1) {
            final Point point = points.get(i);
            final Point nextPoint = points.get(i + 1);

            final Point midPoint = point.add(nextPoint).scale(0.5);
            final double rx = midPoint.x;
            final double ry = midPoint.y;

            if ((boundingRectangle.contains(rx, ry) && unstable.isPositive(rx, ry))) {
                final int px = (int) map.pixelX(rx);
                final int py = (int) map.pixelY(ry);
                try {
                    pixelWriter.setColor(px, py, color);
                    pixelWriter.setColor(px + 1, py, color);
                    pixelWriter.setColor(px - 1, py, color);
                    pixelWriter.setColor(px, py + 1, color);
                    pixelWriter.setColor(px, py - 1, color);
                } catch (final IndexOutOfBoundsException e) {
                }
            }
        }
    }

    private void renderRegion(final Storage region, final WritableImage image, final Color color) {
        final PixelReader pixelReader = image.getPixelReader();
        final PixelWriter pixelWriter = image.getPixelWriter();

        // now we create a rectangle that describes the current viewing screen
        final Rectangle viewRectangle = map.getViewRectangle();

        // ideally we would just iterate over all the points within the intersection
        // if they intersect, calculate the points we need to calculate
        if (region.intersects(viewRectangle)) {
            if (CodeType.isStable(region.type)) {
                final Storage.Stable stable = (Storage.Stable) region;

                // Determine the color of each pixel in a specified row
                for (int readY = 0; readY < SIZE; readY += 1) {
                    final double ry = map.radianY(readY + 0.5);
                    for (int readX = 0; readX < SIZE; readX += 1) {
                        final double rx = map.radianX(readX + 0.5);

                        final Color pixelColor = pixelReader.getColor(readX, readY);
                        // if it's not colored our color already, then let's see if we can color it
                        if (pixelColor != color && !color.equals(Color.TRANSPARENT)) {
                            // if the point is inside the bounding rectangle and is positive

                            final Location location = stable.polygon.location(rx, ry);
                            if (location == Location.INSIDE) {
                                if (stable.isPositive(rx, ry)) {
                                    pixelWriter.setColor(readX, readY, color);
                                }

                            }
                        }
                    }
                }

            } else {
                renderUnstable((Storage.Unstable) region, pixelWriter, viewRectangle, color.invert());
            }
        }
    }

    private static Optional<Line> smartLine(final double sX, final double sY, final double eX, final double eY) {
        final boolean[] problems = {sX > 0 && sX < SIZE && sY > 0 && sY < SIZE,
                                    eX > 0 && eX < SIZE && eY > 0 && eY < SIZE};

        if (problems[0] && problems[1]) {
            // the line is entirely inside
            return Optional.of(new Line(sX, sY, eX, eY));
        } else if (problems[0]) {
            // the start is inside
            final Point direct = Point.unit(Point.create(sX - eX, sY - eY));
            final Point start = Point.create(sX, sY);
            final Point lineEnd = onScreenLine(start, direct);

            return Optional.of(new Line(sX, sY, lineEnd.x, lineEnd.y));

        } else if (problems[1]) {
            // the end is inside
            final Point direct = Point.unit(Point.create(eX - sX, eY - sY));
            final Point start = Point.create(eX, eY);
            final Point lineStart = onScreenLine(start, direct);

            return Optional.of(new Line(lineStart.x, lineStart.y, eX, eY));
        } else {
            new ErrorAlert(new RuntimeException("Error when making the pan line")).showAndWait();
            return Optional.empty();
        }
    }

    // if you have a line which has one end on screen, you can use this to find the onscreen part
    // of that line
    private static Point onScreenLine(final Point start, final Point direct) {
        final Point end;
        final double OFFSET = 0.00000000005;

        final double angle = FastMath.atan2(direct.y, direct.x);

        if (Math.abs(angle - Math.PI) < OFFSET || Math.abs(angle + Math.PI) < OFFSET) {
            end = Point.create(SIZE, start.y);

        } else if (Math.abs(angle - Math.PI / 2) < OFFSET) {
            end = Point.create(start.x, 0);

        } else if (Math.abs(angle) < OFFSET) {
            end = Point.create(0, start.y);

        } else if (Math.abs(angle + Math.PI / 2) < OFFSET) {
            end = Point.create(start.x, SIZE);

        } else if (-Math.PI < angle && angle < -Math.PI / 2) {
            final double arg0 = (SIZE - start.y) / FastMath.sin(angle);
            final double arg1 = (SIZE - start.x) / FastMath.cos(angle);
            final double len = trueMin(arg0, arg1);
            end = start.add(direct.scale(len));

        } else if (-Math.PI / 2 < angle && angle < 0) {
            final double arg0 = (SIZE - start.y) / FastMath.sin(angle);
            final double arg1 = start.x / FastMath.cos(angle);
            final double len = -trueMin(Math.abs(arg0), Math.abs(arg1));
            end = start.add(direct.scale(len));

        } else if (0 < angle && angle < Math.PI / 2) {
            final double arg0 = start.y / FastMath.sin(angle);
            final double arg1 = start.x / FastMath.cos(angle);
            final double len = -trueMin(Math.abs(arg0), Math.abs(arg1));
            end = start.add(direct.scale(len));

        } else if (Math.PI / 2 < angle && angle < Math.PI) {
            final double arg0 = start.y / FastMath.sin(angle);
            final double arg1 = (SIZE - start.x) / FastMath.cos(angle);
            final double len = -trueMin(Math.abs(arg0), Math.abs(arg1));
            end = start.add(direct.scale(len));

        } else {
            end = start;
            new ErrorAlert(new RuntimeException("Something went wrong in"
            		+ " 'onScreenLine' method")).showAndWait();
        }

        return end;
    }

    private static double trueMin(final double a, final double b) {
        final double result;
        if (Math.abs(a) < Math.abs(b)) {
            result = a;
        } else {
            result = b;
        }
        return result;
    }

    public static Optional<Storage> loadStorage(final CodePair codePair) {

        final Optional<CodeInfo> opt = Wrapper.loadCodeInfo(codePair);

        if (!opt.isPresent()) {
            return Optional.empty();
        }

        final CodeInfo codeInfo = opt.get();

        final MutableList<Equation> eqs = new FastList<>();
        for (final SinEquation sin : codeInfo.sinEquations) {
            eqs.add(sin);
        }

        for (final CosEquation cos : codeInfo.cosEquations) {
            eqs.add(cos);
        }

        final CodeType type = codePair.sequence.type();

        final Storage storage;

        if (CodeType.isStable(type)) {

            final ConvexPolygon polygon = ConvexPolygon.create(codeInfo.points);
            storage = new Storage.Stable(codePair.sequence, codePair.angles, type, eqs.toImmutable(), polygon);

        } else {

            final LineSegment lineSegment = LineSegment.create(codeInfo.points);
            storage = new Storage.Unstable(codePair.sequence, codePair.angles, type, eqs.toImmutable(), lineSegment);
        }

        return Optional.of(storage);
    }
}
