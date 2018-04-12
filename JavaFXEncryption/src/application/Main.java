package application;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Main extends Application {
	public String plaintext;
	public String ciphertext;
	public String secretkey;
	public String ivtext;

	@Override
	public void start(Stage primaryStage) {
		try {
			Encryption encryption = new Encryption();
			primaryStage.setTitle("AES Encryption");
			GridPane grid = new GridPane();
			grid.setHgap(50);
			grid.setVgap(10);
			
			Button encryptbtn = new Button();
	        encryptbtn.setText("Encrypt Plain Text"); 
	        TextField plainTextField = new TextField();
	        plainTextField.setPrefWidth(400);
	        plainTextField.setPromptText("Enter your message");
	        grid.add(encryptbtn, 1, 1);
	        grid.add(plainTextField, 2, 1);
	        
		    Button decryptbtn = new Button();
		    decryptbtn.setText("Decrypt Cipher Text");
		    TextField cipherTextField = new TextField();
		    grid.add(decryptbtn, 1, 2);
		    grid.add(cipherTextField, 2,2);
		       
		    Button generatekeys = new Button();
		    generatekeys.setText("Generate New Keys");
		    TextField keyTextField = new TextField();
		    Label lblKey = new Label("128-bit Secret Key");
		    keyTextField.setPrefWidth(300);
		    TextField iVTextField = new TextField();
		    Label lblIV = new Label("Initialization Vector");
	
		    grid.add(generatekeys, 1, 3);
		    grid.add(lblKey, 1, 4);
		    grid.add(keyTextField, 2, 4);
		    grid.add(lblIV, 1, 5);
		    grid.add(iVTextField, 2, 5);
	        
			Scene scene = new Scene(grid,800,200);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
			encryptbtn.setOnAction(new EventHandler<ActionEvent>() {
				 
	            @Override
	        public void handle(ActionEvent event) {
		        plaintext = plainTextField.getText();
		        secretkey = keyTextField.getText();
		        ivtext = iVTextField.getText();
	                  
	                try {
						ciphertext = encryption.encrypt(plaintext, secretkey, ivtext);
						cipherTextField.setText(ciphertext);
						plainTextField.setText("");

					} catch (Exception e) {
						plainTextField.setText("Generate Keys First");
						e.printStackTrace();
					}
	            }
	        });

			decryptbtn.setOnAction(new EventHandler<ActionEvent>() {
				 
	            @Override
	            public void handle(ActionEvent event) {
	            	try {
		                secretkey = keyTextField.getText();
		                ivtext = iVTextField.getText();
		                ciphertext = cipherTextField.getText();
						plaintext = encryption.decrypt(ciphertext, secretkey, ivtext);
						plainTextField.setText(plaintext);
						cipherTextField.setText("");

					} catch (Exception e) {
						plainTextField.setText("Invalid Input");
						e.printStackTrace();
					}
	            }});
			
			generatekeys.setOnAction(new EventHandler<ActionEvent>() {
				 
	            @Override
	            public void handle(ActionEvent event) {
	            	
	            		try {
							encryption.generatekeys();
						} catch (Exception e) {
							e.printStackTrace();
						}
						keyTextField.setText(encryption.getkey());
						iVTextField.setText(encryption.getIV());
	            	}
	            });
				}catch(Exception e){
	    			e.printStackTrace();
				}
		}

	public static void main(String[] args) {
		launch(args);
	}
}
