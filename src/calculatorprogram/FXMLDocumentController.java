package calculatorprogram;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {

    private double mouse_x = 0;
    private double mouse_y = 0;

    private final DecimalFormat df = new DecimalFormat("#.######");
    private final Operations op = new Operations();

    private double lastNumber = 0;
    private String currentOperator = "";

    private boolean newNumber = true;
    private boolean Error = false;

    @FXML private TextField Screen;
    @FXML private Label Ans;
    @FXML private AnchorPane topWindow;

    //begin Window Drag ===================
    public void getMousePosition(MouseEvent e) {
        mouse_x = e.getSceneX();
        mouse_y = e.getSceneY();
    }
    //end =================================
    public void DragWindow(MouseEvent e) {
        Stage stage = (Stage) topWindow.getScene().getWindow();
        stage.setX(e.getScreenX() - mouse_x);
        stage.setY(e.getScreenY() - mouse_y);
    }

    //begin Window Control ======================
    public void Quit(ActionEvent e) {
        Platform.exit();
    }
    //end =======================================

    public void minimizeWindow(ActionEvent e) {
        Stage stage = (Stage) topWindow.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Screen.setText("0");
    }
    //begin adding Numbers ========================
    public void addNumber(ActionEvent e){
        Button b = (Button) e.getSource();
        if (newNumber){
            Screen.setText(b.getText());
            newNumber = false;
        }
        else
            Screen.appendText(b.getText());
    }
    //end ==========================================
    // begin adding point for double numbers========
    public void addPoint(ActionEvent e){
    
        if(newNumber){
            Screen.setText("0.");
            newNumber = false;
        
        }else if(!Screen.getText().contains(".")) Screen.appendText(".");
    
    }
    // end =========================================
    
    
    //begin clear TextField=========================
    public void clearScreen(ActionEvent e){
        if (Error) {clearAll(null); return;}
        
        Screen.setText("0");
        newNumber = true;
    }
    //begin clear All
    public void clearAll(ActionEvent e){
        Screen.setText("0");
        lastNumber = 0;
        Ans.setText("");
        currentOperator = "";
        newNumber = true;
        Error = false;
    }
    //end end ==========================================
    
    //begin signChange +/-==========================
    public void signChange(ActionEvent e){
        Screen.setText(Double.toString(Double.parseDouble(Screen.getText()) * -1));
    }
    //end ==========================================
    
    
    //begin Operators + - * /=======================
    public void operatorPressed(ActionEvent e){
        if (Error) return;
        
        
        double currentNumber = Double.parseDouble(Screen.getText());
        Button b = (Button) e.getSource();
        
        
        if(!newNumber && !currentOperator.isEmpty())
        {
            lastNumber = calculate(lastNumber, currentNumber, currentOperator);
            newNumber = true;
        }else lastNumber = currentNumber;
        
        currentOperator = b.getText();
        Ans.setText(df.format(lastNumber) + " " + currentOperator);
        newNumber = true;
    }
    //end===========================================
    
    //begin Equal Button============================
    public void equalPressed(ActionEvent e){
        if (Error || currentOperator.isEmpty()) return;
        
        double currentNumber = Double.parseDouble(Screen.getText());
        double result = calculate(lastNumber, currentNumber, currentOperator);
        
        if (!Error){
            Screen.setText(df.format(result));
            Ans.setText(lastNumber + " " + currentOperator + " " + currentNumber + " = " + result);
            currentOperator = "";
            newNumber = true;
            
        }
    }
    //end ===========================================
    
    //begin Calculate Function=======================
    private double calculate(double a, double b, String opSymbol){
        return switch (opSymbol){
                case "+" -> op.add(a, b);
                case "-" -> op.minus(a, b);
                case "X" -> op.multi(a, b);
                case "/" -> {
                    if (b == 0){
                        Screen.setText("Math Error");
                        Error = true;
                        yield 0;
                    }yield op.div(a, b);
                }
                default -> b;
        };
    }
    //end =====================================================
}
