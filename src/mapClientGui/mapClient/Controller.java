package mapClient;

import java.awt.Desktop;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Controller class for the main scene.
 * 
 * @author Fabio
 *
 */
public class Controller {
  /**
   * Settings of the client software.
   */
  private Settings settings = new Settings();
  /**
   * TextArea for the console output.
   */
  @FXML
  private TextArea consoleTxtArea;
  /**
   * TextField for the server ip.
   */
  @FXML
  private TextField ipTxtField;
  /**
   * TextField for the server port.
   */
  @FXML
  private TextField portTxtField;
  /**
   * TextField for the table name.
   */
  @FXML
  private TextField tableNameTxtField;
  /**
   * RadioButton to learn from data.
   */
  @FXML
  private RadioMenuItem dataRadioBtn;
  /**
   * RadioButton to load file.
   */
  @FXML
  private RadioMenuItem fileRadioBtn;
  /**
   * ToggleGroup containing the data and file RadioButton.
   */
  @FXML
  private ToggleGroup radioGroup;
  /**
   * MenuItem to clear the console.
   */
  @FXML
  private MenuItem clearConsole;
  /**
   * MenuItem to clear the settings.
   */
  @FXML
  private MenuItem clearSettings;
  /**
   * Button to start the prediction phase.
   */
  @FXML
  private Button predictButton;
  /**
   * Button to connect to the server.
   */
  @FXML
  private Button connectButton;

  /**
   * Handles the logic behing the pressing of the predict button.
   * 
   * @param evt Input triggering event.
   */
  @FXML
  private void predictButtonPressed(ActionEvent evt) {
    if (settings.isConnected()) {
      predictClass();
    } else {
      consoleTxtArea.appendText("Can't predict if not connected to the server\n");
    }

  }

  /**
   * Handles the logic behing the pressing of the connect button.
   * 
   * @param evt Input triggering event.
   */
  @FXML
  private void connectButtonPressed(ActionEvent evt) {
    boolean validFields = validFields();
    boolean differentSettings = differentSettings();
    if (validFields && (differentSettings || settings.isFirstConnection())) {
      closeConnection();
      connect();
    }
    if (!differentSettings && !settings.isFirstConnection()) {
      consoleTxtArea.appendText("Already connected with these settings!\n");
    }
  }

  /**
   * Clears the console.
   * 
   * @param evt Input triggering event.
   */
  @FXML
  private void clearConsole(ActionEvent evt) {
    consoleTxtArea.setText("");
  }

  /**
   * Clears the settings.
   * 
   * @param evt Input triggering event.
   */
  @FXML
  private void clearSettings(ActionEvent evt) {
    ipTxtField.setText("localhost");
    portTxtField.setText("8080");
    tableNameTxtField.setText("");
    dataRadioBtn.setSelected(true);
    fileRadioBtn.setSelected(false);
  }

  /**
   * Displays information about the software.
   * 
   * @param evt Input triggering event.
   */
  @FXML
  private void aboutButtonPressed(ActionEvent evt) {
    Alert alert = new Alert(AlertType.INFORMATION);
    ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
    alert.setHeaderText("REGRESSION TREE MINER");
    alert.setContentText("Project developed by Fabio Fucci\n"
        + "for the course advanced programming methods\nheld by the university of Bari Aldo Moro");

    alert.showAndWait();
  }

  /**
   * Displays the software guide.
   * 
   * @param evt Input triggering event.
   */
  @FXML
  private void guideButtonPressed(ActionEvent evt) {
    try {
      Path tempOutput = Files.createTempFile("guide", ".pdf");
      tempOutput.toFile().deleteOnExit();
      InputStream is = getClass().getClassLoader().getResourceAsStream("guide.pdf");
      Files.copy(is, tempOutput, StandardCopyOption.REPLACE_EXISTING);
      Desktop.getDesktop().open(tempOutput.toFile());
    } catch (RuntimeException | IOException e) {
      Alert alert = new Alert(AlertType.ERROR);
      ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("error.png"));
      alert.setTitle("Exception Dialog");
      alert.setHeaderText("Error while opening user guide.");
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      String exceptionText = sw.toString();

      TextArea textArea = new TextArea(exceptionText);
      textArea.setEditable(false);
      textArea.setWrapText(true);

      textArea.setMaxWidth(Double.MAX_VALUE);
      textArea.setMaxHeight(Double.MAX_VALUE);
      GridPane.setVgrow(textArea, Priority.ALWAYS);
      GridPane.setHgrow(textArea, Priority.ALWAYS);

      GridPane expContent = new GridPane();
      expContent.setMaxWidth(Double.MAX_VALUE);
      Label label = new Label("The exception stacktrace was:");

      expContent.add(label, 0, 0);
      expContent.add(textArea, 0, 1);

      alert.getDialogPane().setExpandableContent(expContent);

      alert.showAndWait();
    }
  }

  /**
   * Learn a new regression tree from the table tableName.
   * 
   * @param tableName Name of the table.
   * @throws NotConnectedException If there are connection problems.
   */
  private void learnDatabase(String tableName) throws NotConnectedException {
    try {
      consoleTxtArea.appendText("Starting data acquisition phase!\n");
      settings.getOut().writeObject(0);
      settings.getOut().writeObject(tableName);
      String answer = settings.getIn().readObject().toString();
      if (!answer.equals("OK")) {
        throw new NotConnectedException(answer);

      }
      consoleTxtArea.appendText(
          "Starting learning phase!\nServer is saving the tree in file " + tableName + ".dmp\n");
      settings.getOut().writeObject(1);
      answer = settings.getIn().readObject().toString();
      if (!answer.equals("OK")) {
        throw new NotConnectedException(answer);

      }
    } catch (IOException | ClassNotFoundException e) {
      throw new NotConnectedException(e.getMessage());

    }
  }

  /**
   * Load a rregression tree from file.
   * 
   * @param tableName Name of the table.
   * @throws NotConnectedException If there are connection problems.
   */
  private void loadFile(String tableName) throws NotConnectedException {
    try {
      consoleTxtArea.appendText("Starting loading phase!\n");
      settings.getOut().writeObject(2);
      settings.getOut().writeObject(tableName);
      String answer = settings.getIn().readObject().toString();
      if (!answer.equals("OK")) {
        throw new NotConnectedException(answer);
      }
    } catch (IOException | ClassNotFoundException e) {
      throw new NotConnectedException(e.getMessage());
    }

  }

  /**
   * Starts the prediction phase.
   */
  private void predictClass() {
    try {
      char risp;
      int path = -1;
      String answer;
      Alert alert = new Alert(AlertType.NONE);
      ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("icon.png"));
      ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
      do {
        settings.getOut().writeObject(3);
        consoleTxtArea.appendText("Starting prediction phase!\n");
        answer = settings.getIn().readObject().toString();
        String[] options;
        while (answer.equals("QUERY")) {
          answer = settings.getIn().readObject().toString();
          options = answer.split("\n");
          consoleTxtArea.appendText(answer + "\n");

          ArrayList<ButtonType> optionsButtons = new ArrayList<>();

          for (int i = 0; i < options.length; i++) {
            optionsButtons.add(new ButtonType(options[i]));
          }
          alert.getButtonTypes().clear();
          alert.getButtonTypes().addAll(optionsButtons);
          alert.getButtonTypes().add(cancelButton);
          alert.setContentText("Choose path");
          Optional<ButtonType> result = alert.showAndWait();
          ButtonType choice = result.get();
          path = optionsButtons.indexOf(choice);
          if (path == -1) {
            settings.getOut().writeObject(path);
            settings.getIn().readObject();
            consoleTxtArea.appendText("Prediction aborted\n");
            return;
          }

          settings.getOut().writeObject(path);
          consoleTxtArea.appendText("You chose path " + path + "\n");

          answer = settings.getIn().readObject().toString();
        }

        if (answer.equals("OK")) {
          answer = settings.getIn().readObject().toString();
          consoleTxtArea.appendText("Predicted class:" + answer + "\n");
        } else {
          consoleTxtArea.appendText(answer + "\n");
        }

        alert.getButtonTypes().clear();
        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");

        alert.getButtonTypes().addAll(buttonYes, buttonNo);
        alert.setContentText("Would you repeat?");
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType choice = result.get();
        if (choice.equals(buttonYes)) {
          risp = 'Y';
        } else {
          risp = 'N';
        }
      } while (Character.toUpperCase(risp) == 'Y');

    } catch (IOException | ClassNotFoundException e) {
      consoleTxtArea.appendText("Prediction aborted\n");

    }
  }

  /**
   * Connects to the server with the current settings.
   */
  private void connect() {
    try {

      consoleTxtArea.appendText("Connecting to the Server\n");
      settings.setSocket(new Socket(InetAddress.getByName(ipTxtField.getText()),
          Integer.parseInt(portTxtField.getText())));
      settings.setOut(new ObjectOutputStream(settings.getSocket().getOutputStream()));
      settings.setIn(new ObjectInputStream(settings.getSocket().getInputStream()));

      String tableName;
      tableName = tableNameTxtField.getText();
      RadioMenuItem button = (RadioMenuItem) radioGroup.getSelectedToggle();
      if (button.isSelected() && button.getText().contains("Data")) {
        learnDatabase(tableName);
      } else {
        loadFile(tableName);

      }
      consoleTxtArea
          .appendText("Succesfully connected, press predict button to start prediction phase\n");

    } catch (NumberFormatException | IOException | NotConnectedException e) {
      consoleTxtArea.appendText("Could not connect to the Server:\n" + e.getMessage() + "\n");
      closeConnection();
      return;
    }
    settings.setConnected(true);
    if (settings.isFirstConnection()) {
      settings.setFirstConnection(false);
    }
    settings.setPort(portTxtField.getText());
    settings.setIp(ipTxtField.getText());
    settings.setTableName(tableNameTxtField.getText());
    settings.setGetTreeFrom(Settings.LearnFrom.Data);
    settings.setGetTreeFrom(Settings.LearnFrom.File);

    Settings.LearnFrom tempGetTreeFrom;
    RadioMenuItem button = (RadioMenuItem) radioGroup.getSelectedToggle();
    if (button.isSelected() && button.getText().contains("Data")) {
      tempGetTreeFrom = Settings.LearnFrom.Data;
    } else {
      tempGetTreeFrom = Settings.LearnFrom.File;
    }
    settings.setGetTreeFrom(tempGetTreeFrom);
  }

  /**
   * Checks if the current settings are valid.
   * 
   * @return False if the server port is not a number, if the ip string is not
   *         valid or if the user did not enter a table name, else true.
   */
  private boolean validFields() {
    String stringAddr = ipTxtField.getText();
    String stringPort = portTxtField.getText();

    try {
      Integer.parseInt(stringPort);

    } catch (NumberFormatException e) {
      consoleTxtArea.appendText("The server port must be a number!\n");
      return false;
    }
    if (stringAddr.equals("")) {
      consoleTxtArea.appendText("The server ip is not valid!\n");
      return false;
    }
    if (!stringAddr.equals("localhost"))
      try {

        System.out.println(stringAddr);
        InetAddress.getByName(stringAddr);
      } catch (UnknownHostException e) {
        consoleTxtArea.appendText("The server ip is not valid!\n");
        return false;
      }
    String tableName = tableNameTxtField.getText();
    if (tableName.equals("")) {
      consoleTxtArea.appendText("Invalid table name\n");
      return false;
    }

    return true;
  }

  /**
   * Checks if the current settings are different from the previous ones, used to
   * avoid reconnecting to the server with the same settings.
   * 
   * @return
   */
  private boolean differentSettings() {
    Settings.LearnFrom tempGetTreeFrom;
    RadioMenuItem button = (RadioMenuItem) radioGroup.getSelectedToggle();
    if (button.isSelected() && button.getText().contains("Data")) {
      tempGetTreeFrom = Settings.LearnFrom.Data;
    } else {
      tempGetTreeFrom = Settings.LearnFrom.File;
    }

    if (!settings.getPort().equals(portTxtField.getText())) {
      return true;
    } else if (!settings.getIp().equals(ipTxtField.getText())) {
      return true;
    } else if (!settings.getTableName().equals(tableNameTxtField.getText())) {
      return true;
    } else if (!settings.getGetTreeFrom().equals(tempGetTreeFrom)) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * Closes the connection with the server.
   */
  void closeConnection() {
    settings = new Settings();
    settings.closeConnection();
  }

}