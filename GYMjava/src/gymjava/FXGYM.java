/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gymjava;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//...
public class FXGYM extends Application{
     public static void main(String[] args) {
       launch(args);
}
    private CashAccount cashAccount = new CashAccount(); // Cash account instance
    private CreditAccount creditCardAccount = new CreditAccount(); // Credit account instance
   
    File selectedPictureFile;
    GYM gym=new GYM();
    Member member=new Member();
    
    @Override
    public void start(Stage stage)throws Exception{
        VBox layout = new VBox(20);
        layout.setBackground(new Background(new BackgroundFill(Color.TAN, CornerRadii.EMPTY, Insets.EMPTY))); //add backgroud color
        layout.setAlignment(Pos.CENTER); //alignment
        layout.setPadding(new Insets(30));
        Scene scene=new Scene(layout,700,500); //size
        
        //set the icon image
        Image icon=new Image("gym.png");//icon image
        stage.getIcons().add(icon); //add on stage
        stage.setTitle("GYM Registration");//program name
        stage.setResizable(false);//cant resize
        
        //text "add member"
        Text text1=new Text();
        text1.setText("Register New Member");
        text1.setFont(Font.font("Verdana",25)); //font and size
        text1.setFill(Color.SADDLEBROWN); //color
        
        //text 'payment method"
        Text text2=new Text();
        text2.setText("Payment method");
        text2.setFont(Font.font("Verdana",17)); //font and size
        text2.setFill(Color.SADDLEBROWN); //color
       
       //input fields
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number: ex(123-1234567)");
       
        TextField paymentField = new TextField();
        paymentField.setPromptText("Amount Paid");
       
        TextField dateField = new TextField();
        dateField.setPromptText("Date: yyyy-MM-dd");
        
       // Selecting a picture
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter
                 ("Image Files", "*.jpg", "*.png")); //specify the types of images
        Button choosePictureButton = new Button("Choose Picture");
        choosePictureButton.setOnAction(e -> { //once its clicked
        selectedPictureFile = fileChooser.showOpenDialog(stage);
        });
        
       //radio buttons
        ToggleGroup RadioGroup = new ToggleGroup(); //group the buttons
        RadioButton cashOption = new RadioButton("Cash");
        RadioButton creditCardOption = new RadioButton("Credit Card");
        cashOption.setToggleGroup(RadioGroup); //add to the group
        creditCardOption.setToggleGroup(RadioGroup); //add to the group
        
        HBox paymentMethodLayout = new HBox(10, cashOption, creditCardOption);//add the buttons on the scene
        paymentMethodLayout.setAlignment(Pos.CENTER);//align
        
        
       // Button to trigger registration
        Button registerButton = new Button("Register");
        Font font = Font.font("Verdana",FontWeight.EXTRA_LIGHT, 20); //font and size
        registerButton.setFont(font);
        registerButton.setOnAction(e -> { //once its clicked
            //get the input info
            String name = nameField.getText();
            String phone = phoneField.getText();
            String payment = paymentField.getText();
            String date=dateField.getText();

            // Validate input if its empty
            if (name.isEmpty() || phone.isEmpty() || payment.isEmpty() || date.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Missing Information", "Please complete all fields.");
                return;
            }
            
            //validate number
            if (!isValidPhoneNumber(phone)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Phone Number", "Phone number must be in the format 123-456-7890.");
                return;
            }
            //validate date
            if(!isValidDate(date)){
                showAlert(Alert.AlertType.ERROR, "Invalid Date", "Date must be in the format yyyy-mm-dd.");
                return;
            }
            //if the picture is empty
            if (selectedPictureFile == null) {
                showAlert(Alert.AlertType.WARNING, "Missing Picture", "Please choose a picture.");
                return;
            }
            //validate payment
            double amountPaid;
            amountPaid = Double.parseDouble(payment);//turn the input to a number
            if (!isValidPayment(amountPaid)){
                showAlert(Alert.AlertType.ERROR, "Invalid Payment Amount", "Payment amount must be greater than 0.");
                return;
            }
            //validate selected method    
            RadioButton selectedMethod = (RadioButton) RadioGroup.getSelectedToggle();
            if (selectedMethod == null) {
                showAlert(Alert.AlertType.ERROR, "Payment Method", "Choose a payment method.");
                return;
            }
            //add the payment to the selected account
            if (selectedMethod == cashOption) {
                cashAccount.addPayment(amountPaid); //adds to the cash
            } else {
                creditCardAccount.addPayment(amountPaid); // Adds to credit card account
            }
            //add to the arraylist
            member.registerMember(name, phone, amountPaid,selectedMethod.getText(), date, selectedPictureFile.getAbsolutePath());
            gym.paymentInfo(name,selectedMethod.getText(),amountPaid,date);
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Member registered!");
          
           // Clear fields after successful registration
            nameField.clear();
            phoneField.clear();
            paymentField.clear();
            dateField.clear();
            RadioGroup.selectToggle(null); // Clear payment method
            selectedPictureFile = null;//Clear the selected picture
        });
        
        
        //button to show the balance
        Button balance=new Button("Show GYM Balance");    
        balance.setOnAction(e1 -> { //once its clicked
            showAlert(
                Alert.AlertType.INFORMATION,
                "Account Balances",
                "Cash Account Balance: " + cashAccount.getBalance() +
                     "\nCredit Card Account Balance: " + creditCardAccount.getBalance()+
                     "\nTotal Balance: "+(cashAccount.getBalance()+creditCardAccount.getBalance())
            );
        });
        
        //button to show the registered members
        Button listButton=new Button("Show Member List");
        listButton.setOnAction(e -> member.showMemberList()); //once its clicked, show the info
    
        // Button to export account balances and payments to Notepad
        Button exportButton = new Button("Export Payments");
        exportButton.setOnAction(e -> gym.writePaymentsToFile()); //once its clicked, write info to a file
        
        HBox buttonLayout=new HBox(10,balance,listButton, exportButton);//place them together
        buttonLayout.setAlignment(Pos.CENTER);
       
        //set everything on the screen
        layout.getChildren().addAll(
            text1,
            nameField,
            phoneField,
            paymentField,
            dateField,
            choosePictureButton,
            text2,
            paymentMethodLayout,
            registerButton,
            buttonLayout);
        
        stage.setScene(scene);
        stage.show();
            
}
        // Utility to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    
    // Phone number validation 
    private boolean isValidPhoneNumber(String phone) {
        Pattern pattern = Pattern.compile("^\\d{3}-\\d{7}"); // Expected format
        return pattern.matcher(phone).matches();
    }
    //Date validation
    private  boolean isValidDate(String date) {
        Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");//expected format
        return pattern.matcher(date).matches();
    }
  private boolean isValidPayment(double amount) {//must be greater than 0
        return amount > 0;
    }
  
 // Member class to store details
    class Member {
        private final List<Member> memberDetails = new ArrayList<>();
        private String name;
        private String phone;
        private double payment;
        private String method;
        private String picturePath;
        private String date;
        
        //constructors
        public Member(String name, String phone, double payment, String method,String date,String picturePath ) {
            this.name = name;
            this.phone = phone;
            this.payment = payment;
            this.method = method;
            this.picturePath = picturePath;
            this.date=date;
        }

        public Member(){   
        }
        
        //getters
        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public double getPayment() {
            return payment;
        }

        public String getMethod() {
            return method;
        }
        
        public String getPicturePath() {
            return picturePath;
        }
         
        public String getDate() {
            return date;
        }
        
    //register a member
     private void registerMember(String name, String phone,double payment, String method,  String date,String picturePath) {
        memberDetails.add(new Member(name, phone, payment, method, date, picturePath));
       
    }
     
    //show members
      private void showMemberList() {
          VBox memberLayout=new VBox(10); //new tab to show
          memberLayout.setBackground(new Background(new BackgroundFill(Color.TAN, CornerRadii.EMPTY, Insets.EMPTY)));
          memberLayout.setPadding(new Insets(10));
         
          for (Member member : memberDetails) {
          VBox textInfoLayout = new VBox(5);
          textInfoLayout.getChildren().addAll(
                    new Text("Name: " + member.getName()),
                    new Text("Phone: " + member.getPhone()),
                    new Text("Payment: $" + member.getPayment()),
                    new Text("Method: " + member.getMethod()),
                    new Text("Date: "+member.getDate()),
                    new Text("Picture path: "+member.getPicturePath())
            );
         
            memberLayout.getChildren().addAll(textInfoLayout);
          }
          
        ScrollPane scrollPane = new ScrollPane(memberLayout);
        scrollPane.setBackground(new Background(new BackgroundFill(Color.TAN, CornerRadii.EMPTY, Insets.EMPTY)));
        scrollPane.setFitToHeight(true); // Adjust the height to match the scene
        scrollPane.setFitToWidth(true); // Adjust the width to match the scene
        
        Scene memberListScene = new Scene(scrollPane,400,400);//add on scene
        Stage memberListStage = new Stage();
        
        memberListStage.setTitle("Registered Members");
        memberListStage.setScene(memberListScene);
        memberListStage.show();
    }
} 

 //interface    
    interface Interface {
    public void addPayment(double amount); // Add payment to the account
    public double getBalance();//show the balance
}

//account parent class    
    abstract class  Account implements Interface{
    protected double balance;
    
    public Account(){//constructor
        this.balance=0.0;
    }
   
}

 //cashaccount child class
    class CashAccount extends Account {
    
    public CashAccount() {
        this.balance = 0.0; // Start with zero balance
    }
    @Override
     public void addPayment(double amount) {
        this.balance += amount; // Add payment to cash balance
    }

    @Override
    public double getBalance() {
        return balance; // Return current cash balance
    }
    
}

    //credit account child class
    class CreditAccount extends Account{

    public CreditAccount() {
        this.balance = 0; // Start with zero balance
    }
    @Override
    public void addPayment(double amount) {
        this.balance += amount; // Add payment to credit balance
    }

    @Override
    public double getBalance() {
        return balance; // Return current credit balance
    }
    
}
 //class payment to store payment info
    class Payment {
    private final String name;
    private final String type;
    private final double amount;
    private final String date;
    
    //contrustor
    public Payment(String name,String type, double amount, String date) {
        this.name=name;
        this.type = type;
        this.amount = amount;
        this.date=date;
    }
    
    //getters
    public String getName(){
        return name;
    }
      public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
    
    //display info
    @Override
    public String toString() {
        return "Name: " + name +",Type: " + type + ", Amount: " + amount + ", Date: " + date;
    }
}

//class gym
    class GYM {
    private final ArrayList<Payment> payments = new ArrayList<>(); //store payments
    private final CashAccount cashAccount = new CashAccount();
    private final CreditAccount creditAccount = new CreditAccount();

    //type of accounts the gym can have
    public double getCashBalance() {
        return cashAccount.getBalance();
    }

    public double getCreditBalance() {
        return creditAccount.getBalance();
    }
    
    //add the payments to the specified account aand store in the list
    public void addCash(String name,double amount, String date) {
        cashAccount.addPayment(amount);
        payments.add(new Payment(name,"cash", amount, date));
    }
    
    public void addCredit(String name, double amount, String date) {
        creditAccount.addPayment(amount);
        payments.add(new Payment(name, "credit", amount, date));
    }
    
    //payment information
    public void paymentInfo( String name, String method,double amount,String date){
       //  Payments.add(method);
         payments.add(new Payment(name, method,amount,date));
     }
   //write the informations on a txt file
    public void writePaymentsToFile() {
        String outputFileName = "gym_payments.txt";//the path must be specified depending on the computer and the file must exist

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))) {
            writer.println("Payments:");
           for (Payment payment : payments) {
                writer.println(payment.toString());
            }
         System.out.println("Payment details successfully written to: " + outputFileName);//shpws that the info has been written
        } catch (IOException e) {
            System.err.println("Error writing to " + outputFileName + ": " + e.getMessage());
        }
    }
    
 }

}