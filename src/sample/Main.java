package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Build build = new Build();

        String fileName = "/Users/olegzaimm/Downloads/test.txt";
        List<String> transitlines = new ArrayList<>();

        List<String> roomLines = new ArrayList<>();
        List<Place> places = new ArrayList<>();
        Supplier<Stream<String>> supplierStream = ()-> {

            try {
                return Files.lines(Paths.get(fileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        transitlines = supplierStream.get().filter(line -> line.contains("yes") || line.contains("no")).collect(Collectors.toList());
        roomLines = supplierStream.get().filter(line -> line.contains("room") || line.contains("transit")).collect(Collectors.toList());

        for(String string : roomLines){
            string = string.replaceAll("\\s+","");
            string = string.replaceAll(";","");
            String[] values = string.split(",");
            places.add(new Place(values[0],Integer.parseInt(values[1]),Integer.parseInt(values[2]),Integer.parseInt(values[3]),PlaceEnum.valueOf(values[4].toUpperCase())));
        }
        for(Place place : places){
            build.addPlaceToBuild(place);
        }
        for(String string : transitlines){
            string = string.replaceAll("\\s+","");
            string = string.replaceAll(";","");
            String[] values = string.split(",");
            // build.createTransition(values[0],values[1],TransitionEnum.valueOf(values[2].toUpperCase()),Integer.parseInt(values[3]),values[4]);
            build.createTransitionWithWight(values[0],values[1],TransitionEnum.valueOf(values[2].toUpperCase()),Double.parseDouble(values[3]),values[4]);
        }



        BaseSearch search =
                new DijkstraSearch(build);

//        search.skipTransition(true, TransitionEnum.CLIMB);

        search.findPathByPriorityTransition(true,TransitionEnum.LIFT);

        if(search.checkForPath("500", "403")) {
            System.out.println("Path Found!");
        }else {
            System.out.println("No path found!");
        }

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
