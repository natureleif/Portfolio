package whitman.cs370proj.composer;

import java.util.Optional;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;


public class fileManager {
    private static File currentFile;

    public static void save(){
        String data = Converter.toString(CompManager.getComposition());

        if (currentFile == null) {
          saveAs();
        }
        else {
          try { 
            String fileName = currentFile.getName();
            File myObj = new File(fileName);
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(data);
            myWriter.close();
            VersionController.hasChanged = false;
            System.out.println("Successfully wrote to " + myObj.getName());
          } 
          catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        }
      }

      public static void saveAs(){
        String data = Converter.toString(CompManager.getComposition());

        Stage stage = new Stage();
        stage.setTitle("Save Window");
        
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().add(new ExtensionFilter("All Files", "*.txt"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        String fileName = selectedFile.getName();
        
        try { 
            File myObj = new File(fileName+=".txt");
            System.out.println("File created: " + myObj.getName());
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write(data);
            myWriter.close();
            currentFile = myObj;
            VersionController.hasChanged = false;
            System.out.println("Successfully wrote to the file.");
          }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
      }

      public static void newFile() {
        // check if current composition in the panel is already saved
          if (VersionController.hasChanged == true) {
            Boolean cont = saveAlert();

            if (cont == true) {
              save();
              clear();
            }
          } else {
            clear();
          }
      }

      public static void clear() {
        // clear the board :)
        CompManager.setComposition("[]");
        // set currentFile to null
        currentFile = null;
      }

      public static void open() {
        Boolean cont = true;
        if (VersionController.hasChanged == true) {
          cont = saveAlert();
        }
        if (cont == false) {}
        else {
          //create open dialog box
          String data = Converter.toString(CompManager.getComposition());

          Stage stage = new Stage();
          stage.setTitle("Save Window");
        
          FileChooser fileChooser = new FileChooser();

          fileChooser.getExtensionFilters().add(new ExtensionFilter("All Files", "*.txt"));
          File selectedFile = fileChooser.showOpenDialog(stage);
          load(selectedFile);
        }
      }

      public static Boolean saveAlert() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("New");
            alert.setHeaderText(null);
            alert.setContentText("Your current composition doesn't appear to be saved. Do you want to save before continuing?");
            ButtonType buttonYes = new ButtonType("Yes");
            ButtonType buttonNo = new ButtonType("No");
            ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonYes, buttonNo, buttonCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonYes){
                save();
                return true;
            } else if (result.get() == buttonNo) {
                return true;
            } 
        return false;
      }

    public static void load(File newFile){
      try{
          currentFile = newFile;
          String path = currentFile.getAbsolutePath();
          File file = new File(path);
          Scanner myReader = new Scanner(file); 
          while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            CompManager.setComposition(data);
          }
          myReader.close();
          VersionController.hasChanged = false;
        }
      catch(FileNotFoundException e){
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }
}

