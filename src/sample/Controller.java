package sample;

import com.oracle.webservices.internal.api.EnvelopeStyle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    @FXML
    private MenuItem openFIleItem;


    @FXML
    private ListView listView;


    @FXML
    private Menu skipTransitionMenu;

    @FXML
    private RadioMenuItem skipLift;

    @FXML
    private RadioMenuItem skipClimb;

    @FXML
    private RadioMenuItem skipWalk;


    @FXML
    private RadioMenuItem prioriyLift;

    @FXML
    private RadioMenuItem priorityClimb;

    @FXML
    private RadioMenuItem priorityWalk;

    @FXML
    private RadioMenuItem coordinate;

    @FXML
    private RadioMenuItem weight;

    private static String transitionName;

    private static String priorityTransitionName;

    private boolean isWeight = true;

    private boolean isCoordinate = false;

    @FXML
    private MenuButton startPlace;

    @FXML
    private MenuButton endPlace;

    @FXML
    private CheckBox activateSkipTransition;

    @FXML
    private CheckBox acitivatePriority;

    @FXML
     Label label;

     Group group;

    private List<RadioMenuItem> startMenuItemList = new ArrayList<>();

    private List<RadioMenuItem> endMenuItemList = new ArrayList<>();

    private String startPlaceName = "";
    private String endPlaceName = "";

    @FXML
    public SubScene subScene;
    //
    @FXML
    private Button find;

    public void setTextInLable(String s) {
        String s1 = s;
        System.out.println(s1);
        group = new Group();
   GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(10);
        gridpane.setVgap(10);

        label = new Label(s);
        GridPane.setHalignment(label, HPos.CENTER);
        gridpane.add(label, 0, 0);
//
        group.getChildren().addAll(gridpane);


        subScene = new SubScene(group, 100, 100);


    }

    public void openFile() {
        System.out.println("Open file");
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);


        if (selectedFile != null) {
            listView.getItems().add(selectedFile.getAbsolutePath());

            if (skipLift.isSelected()) {
                transitionName = skipLift.getText();
                skipWalk.setSelected(false);
                skipClimb.setSelected(false);
            } else if (skipClimb.isSelected()) {
                transitionName = skipClimb.getText();
                skipWalk.setSelected(false);
                skipLift.setSelected(false);
            } else if (skipWalk.isSelected()) {
                transitionName = skipWalk.getText();
                skipLift.setSelected(false);
                skipClimb.setSelected(false);
            }


            if (prioriyLift.isSelected()) {
                priorityTransitionName = prioriyLift.getText();
                priorityClimb.setSelected(false);
                priorityWalk.setSelected(false);
            } else if (priorityClimb.isSelected()) {
                priorityTransitionName = priorityClimb.getText();
                priorityWalk.setSelected(false);
                prioriyLift.setSelected(false);
            } else if (priorityWalk.isSelected()) {
                priorityTransitionName = priorityWalk.getText();
                prioriyLift.setSelected(false);
                priorityClimb.setSelected(false);
            }

            if (coordinate.isSelected()) {
                isWeight = false;
                isCoordinate = true;
            } else if (weight.isSelected()) {
                isCoordinate = false;
                isWeight = true;
            }

            initialBuild(selectedFile.getAbsolutePath());


        } else {
            System.out.println("File is not valid");
        }


    }

    public void doExit() {
        Platform.exit();
    }

    public String skipTransition() {
        return transitionName;
    }

    public void initialBuild(String fileName) {
        Build build = new Build();

        List<String> transitlines = new ArrayList<>();

        List<String> roomLines = new ArrayList<>();
        List<Place> places = new ArrayList<>();
        Supplier<Stream<String>> supplierStream = () -> {

            try {
                return Files.lines(Paths.get(fileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        transitlines = supplierStream.get().filter(line -> line.contains("yes") || line.contains("no")).collect(Collectors.toList());
        roomLines = supplierStream.get().filter(line -> line.contains("room") || line.contains("transit")).collect(Collectors.toList());

        for (String string : roomLines) {
            string = string.replaceAll("\\s+", "");
            string = string.replaceAll(";", "");
            String[] values = string.split(",");
            places.add(new Place(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]), PlaceEnum.valueOf(values[4].toUpperCase())));
        }
        for (Place place : places) {
            RadioMenuItem startPlaceMenuItem = new RadioMenuItem(place.getNumber());
            startPlace.getItems().addAll(startPlaceMenuItem);
            startMenuItemList.add(startPlaceMenuItem);
            RadioMenuItem endPlaceMenuItem = new RadioMenuItem(place.getNumber());
            endPlace.getItems().addAll(endPlaceMenuItem);
            endMenuItemList.add(endPlaceMenuItem);
            build.addPlaceToBuild(place);
        }
        for (String string : transitlines) {
            string = string.replaceAll("\\s+", "");
            string = string.replaceAll(";", "");
            String[] values = string.split(",");
            if (isCoordinate) {
                build.createTransition(values[0], values[1], TransitionEnum.valueOf(values[2].toUpperCase()), Integer.parseInt(values[3]), values[4]);
            }
            if (isWeight) {
                build.createTransitionWithWight(values[0], values[1], TransitionEnum.valueOf(values[2].toUpperCase()), Double.parseDouble(values[3]), values[4]);

            }
        }


        BaseSearch search =
                new DijkstraSearch(build);

        boolean skip = activateSkipTransition.isSelected();
        if (skip) {
            search.skipTransition(skip, TransitionEnum.valueOf(transitionName.toUpperCase()));
        }

        boolean findByPrioirity = acitivatePriority.isSelected();
        if (findByPrioirity) {
            search.findPathByPriorityTransition(findByPrioirity, TransitionEnum.valueOf(priorityTransitionName.toUpperCase()));
        }


        find.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        for (RadioMenuItem rmi : startMenuItemList) {
                            if (rmi.isSelected()) {
                                startPlaceName = rmi.getText();
                                break;
                            }
                        }
                        for (RadioMenuItem rmi : endMenuItemList) {
                            if (rmi.isSelected()) {
                                endPlaceName = rmi.getText();
                                break;
                            }
                        }
                        if (isWeight) {
                            if (search.checkForPath(startPlaceName, endPlaceName)) {
                                System.out.println("Path Found!");
                            } else {
                                System.out.println("No path found!");
                            }
                        } else if (isCoordinate) {

                            if (search.checkForPathByCoordinates(startPlaceName, endPlaceName)) {
                                System.out.println("Path Found!");
                            } else {
                                System.out.println("No path found!");
                            }
                        }
                    }
                });

    }

    public void print(Node node){

        PrinterJob job = PrinterJob.createPrinterJob();

        if(job !=null){
            boolean printed = job.printPage(node);
            if(printed){
                job.endJob();
            }
        }
    }
    public void init(Stage primaryStage) {
    }
}
