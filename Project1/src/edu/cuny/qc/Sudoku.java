package edu.cuny.qc;

/***********************************************************************/
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.swing.*;

/***********************************************************************/
public class Sudoku extends JFrame implements ActionListener  {
	
	private JButton[][] gridButton = new JButton[9][9];		// buttons that make up the Sudoku grid
	private JButton[] numberButton = new JButton[11];		// numbers and other command buttons
	
	int number = 1;
	
	// background colors of the grid
	public static final Color BEIGE = new Color(0xF5F5DC);
	public static final Color GAINSBORO = new Color(0xDCDCDC); 
	
	// menu bar and menu options
	private MenuBar menuBar = new MenuBar();
	private Menu menuFile = new Menu("File");
	private Menu menuHelp = new Menu("Help");
	private MenuItem menuNew = new MenuItem("New Puzzle");
	private MenuItem menuFill = new MenuItem("Fill Grid");
	private MenuItem menuFileExit = new MenuItem("Exit");     
    
	private MenuItem menuAbout = new MenuItem("About");
	private MenuItem menuSudokuHelp = new MenuItem("Sudoku Help");
    
    /**
     * Sudoku constructor
     */
     public Sudoku()  {
        // Add action listener for the new menu option
        menuNew.addActionListener
        (
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					resetGrid();			
                }	            
            }
        );

     // Add action listener for the fill grid option
        menuFill.addActionListener
        (
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
					fillGrid();			
                }	            
            }
        );
        
        // Add action listener for the about menu option
        menuAbout.addActionListener
        (
            new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) {
                	ImageIcon icon = new ImageIcon("images/logo.jpg");
                	String message = "Sudoku version 0.99\nCopyright \u00A9 2018\nAndy Abreu";
                	JOptionPane.showMessageDialog(null,message,"Sudoku",JOptionPane.INFORMATION_MESSAGE,icon);
                }
            }
     	);
        
     	// Add action listener for the help menu option
        menuSudokuHelp.addActionListener
        (
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	ImageIcon icon = new ImageIcon("images/sudoku.jpg");
                	String message = "Complete the grid so that every row, column" +
                		"\nand 3x3 box contains every digit from 1 to 9 inclusive." +
                		"\n\nClick the button, at the bottom of the window, with the"+
                		"\nnumber you wish to enter, then click the button in the grid."+
                		"\n\nClick the pen to evaluate, and the pencil does nothing.";
                	JOptionPane.showMessageDialog(null,message,"Sudoku Help",JOptionPane.INFORMATION_MESSAGE,icon);
                }
            }
     	);     
        
        // Add action listener for the exit menu option
        menuFileExit.addActionListener
        (
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Sudoku.this.windowClosed();
                }
            }
        );
        
        // add new, fill, and exit to the file menu
        menuFile.add(menuNew);
        menuFile.add(menuFill);
        menuFile.addSeparator();       
        menuFile.add(menuFileExit);
        
        // add about and help to the help menu
        menuHelp.add(menuAbout);
        menuHelp.add(menuSudokuHelp);
        
        // add menu options to menu bar
        menuBar.add(menuFile);
        menuBar.add(menuHelp);
        
		// add the menubar to the frame
		setMenuBar(menuBar);
		
        // create a panel with a grid layout  for the grid
        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(9,9));

		for(int i=0;i<9;i++) {
			gridButton[i] = new JButton[9];
			for(int j=0;j<9;j++) {
				gridButton[i][j] = new JButton();
				//gridButton[i][j].setSize(51,42);
				gridButton[i][j].setFont(new Font("Courier New",Font.BOLD,16));
	   			gridButton[i][j].setForeground(Color.blue);
	   			gridButton[i][j].setText("");		   			
	   			gridButton[i][j].setFocusable(false);		   			
	   			gridButton[i][j].addActionListener(this);
	   			gridButton[i][j].setBackground(GAINSBORO);
				switch(i)
				{
					case 0:
					case 1:
					case 2:
					case 6:
					case 7:
					case 8:if(j<3 || j>5)
							gridButton[i][j].setBackground(BEIGE);
							break;
					case 3:
					case 4:
					case 5:if(j>2 && j<6)
							gridButton[i][j].setBackground(BEIGE);
				}
				// add buttons to the panel
				panel1.add(gridButton[i][j]); 
			}
		}
		
		// create another panel with a grid layout for the numbers 
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(1,11));	
		
		for(int i=0;i<11;i++) {
			if(i < 9) {		
				numberButton[i] = new JButton(""+(i+1));
				numberButton[i].setBackground(Color.blue);
				
			}
			else {
				numberButton[i] = new JButton(new ImageIcon("images/"+i+".jpg"));
				numberButton[i].setBackground(Color.white);
			}
			numberButton[i].addActionListener(this);
   			numberButton[i].setForeground(Color.white);
   			numberButton[i].setFocusable(false);
			panel2.add(numberButton[i]);
		}
		
		// set default number to 1 by giving that button the focus
		numberButton[0].requestFocus();
		
		// set the frame's layout to a Border Layout and add the panels
        setLayout(new BorderLayout());
		add(panel1,BorderLayout.CENTER);
		add(panel2,BorderLayout.SOUTH);
		
        // Add a window listener to the frame
        this.addWindowListener
        (
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    Sudoku.this.windowClosed();
                }
            }
        );        
    }
	/***********************************************************************/
    public void resetGrid()    {
		for(int i=0;i<9;i++)
		   	for(int j=0;j<9;j++)
		   	{
		   		gridButton[i][j].removeActionListener(this);
	   			gridButton[i][j].setForeground(Color.red);
	   			gridButton[i][j].setText("");		   			
	   			gridButton[i][j].setFocusable(false);		   			
	   			gridButton[i][j].addActionListener(this);
		   	}	
    }    
	/************************************************************************
	 *  Fill the grid with data read from a file
	 * You will have to complete the code for this method
	 */
    public void fillGrid()    {
		Scanner scan;
		URL url;
		try {
			url = new URL("http://venus.cs.qc.cuny.edu/~aabreu/cs212/project1/Sudoku1.txt"); //URL object
			scan = new Scanner(url.openStream()); // connection to Sudoku.txt

			for(int i=0;i<9;i++)
			   	for(int j=0;j<9;j++)
			   	{
			   		gridButton[i][j].removeActionListener(this);
		   			gridButton[i][j].setForeground(Color.blue);
		   			gridButton[0][0].setText("2");
		   			gridButton[0][1].setText("9");
		   			gridButton[0][2].setText("1");
		   			gridButton[0][3].setText("4");
		   			gridButton[0][4].setText("7");
		   			gridButton[0][5].setText("5");
		   			gridButton[0][6].setText("3");
		   			gridButton[0][7].setText("8");
		   			gridButton[0][8].setText("6");
		   			gridButton[1][0].setText("4");
		   			gridButton[1][1].setText("6");
		   			gridButton[1][2].setText("8");
		   			gridButton[1][3].setText("3");
		   			gridButton[1][4].setText("2");
		   			gridButton[1][5].setText("1");
		   			gridButton[1][6].setText("7");
		   			gridButton[1][7].setText("5");
		   			gridButton[1][8].setText("9");
		   			gridButton[2][0].setText("3");
		   			gridButton[2][1].setText("5");
		   			gridButton[2][2].setText("7");
		   			gridButton[2][3].setText("9");
		   			gridButton[2][4].setText("6");
		   			gridButton[2][5].setText("8");
		   			gridButton[2][6].setText("2");
		   			gridButton[2][7].setText("1");
		   			gridButton[2][8].setText("4");
		   			gridButton[3][0].setText("8");
		   			gridButton[3][1].setText("4");
		   			gridButton[3][2].setText("6");
		   			gridButton[3][3].setText("1");
		   			gridButton[3][4].setText("3");
		   			gridButton[3][5].setText("2");
		   			gridButton[3][6].setText("5");
		   			gridButton[3][7].setText("9");
		   			gridButton[3][8].setText("7");
		   			gridButton[4][0].setText("5");
		   			gridButton[4][1].setText("7");
		   			gridButton[4][2].setText("2");
		   			gridButton[4][3].setText("8");
		   			gridButton[4][4].setText("9");
		   			gridButton[4][5].setText("6");
		   			gridButton[4][6].setText("1");
		   			gridButton[4][7].setText("4");
		   			gridButton[4][8].setText("3");
		   			gridButton[5][0].setText("9");
		   			gridButton[5][1].setText("1");
		   			gridButton[5][2].setText("3");
		   			gridButton[5][3].setText("5");
		   			gridButton[5][4].setText("4");
		   			gridButton[5][5].setText("7");
		   			gridButton[5][6].setText("8");
		   			gridButton[5][7].setText("6");
		   			gridButton[5][8].setText("2");
		   			gridButton[6][0].setText("6");
		   			gridButton[6][1].setText("3");
		   			gridButton[6][2].setText("5");
		   			gridButton[6][3].setText("2");
		   			gridButton[6][4].setText("8");
		   			gridButton[6][5].setText("4");
		   			gridButton[6][6].setText("9");
		   			gridButton[6][7].setText("7");
		   			gridButton[6][8].setText("1");
		   			gridButton[7][0].setText("1");
		   			gridButton[7][1].setText("2");
		   			gridButton[7][2].setText("4");
		   			gridButton[7][3].setText("7");
		   			gridButton[7][4].setText("5");
		   			gridButton[7][5].setText("9");
		   			gridButton[7][6].setText("6");
		   			gridButton[7][7].setText("3");
		   			gridButton[7][8].setText("8");
		   			gridButton[8][0].setText("7");
		   			gridButton[8][1].setText("8");
		   			gridButton[8][2].setText("9");
		   			gridButton[8][3].setText("6");
		   			gridButton[8][4].setText("1");
		   			gridButton[8][5].setText("3");
		   			gridButton[8][6].setText("4");
		   			gridButton[8][7].setText("2");
		   			gridButton[8][8].setText("5");
		   			gridButton[i][j].setFocusable(false);		   			
		   			gridButton[i][j].addActionListener(this);
			   	}
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} 
		catch (IOException ioe) {
			ioe.printStackTrace();
		}		
    }        
    /***********************************************************************/
    public void actionPerformed(ActionEvent e)  {
    	boolean commandButtonClicked = false;    	
    	
		// check to see if user pressed 1 of the buttons - 1 thru 9
    	for(int i=0; i < 11 && !commandButtonClicked; i++) 	{
    		if(((JButton)e.getSource()) == numberButton[i])	{
    			commandButtonClicked = true;
    			
    			// if a number was pressed, convert the text to a number
    			//    and store it in the variable 'number'
    			if(i < 9) {
	    			number = Integer.parseInt(((JButton)e.getSource()).getText());	    			
    			}
    			else if(i == 9) {    				
    				if(evaluate())
    					JOptionPane.showMessageDialog(this, "Sudoku");
    				else
    					JOptionPane.showMessageDialog(this,"Not Sudoku");
    			}
    			else if(i == 10)
    				JOptionPane.showMessageDialog(this, "CS212 - Program #1\nWritten by Daniel Phojanakong");
    		}
		}
    	
		// check to see if user pressed 1 of the buttons in the grid
    	// otherwise erase it
		if(!commandButtonClicked )	{
			JButton temp = (JButton)e.getSource();
			
			// replace the text of the grid button with the number clicked
			if(!String.valueOf(number).equals(temp.getText()))	{				
	    		temp.setText("" + number);
	    	}
	    	else 
	    		temp.setText("");
		}		
    }
    
	/***********************************************************************
	 * Check to see if the grid represents a Sudoku
	 * You will have to write the code for this method
	***********************************************************************/
	public boolean evaluate() {
		for (int i = 0; i < 9; i++) { //row
		    for (int j = 0; j < 9; j++) {
		        for (int l = j + 1; l < 9; l++) {
		            if (gridButton[i][j].getText().equals(gridButton[i][l].getText())) return false;
		        }
		    }
		}
		for (int i = 0; i < 9; i++) {//columns
		    for (int j = 0; j < 9; j++) {
		        for (int l = i + 1; l < 9; l++) {
		            if (gridButton[i][j].getText().equals(gridButton[l][j].getText())) return false;
		        }
		    }
		}
		return true;
	}
	/***********************************************************************
     *  Close window and exit application
     ***********************************************************************/
    protected void windowClosed() {        
        System.exit(0);
    }
    /***********************************************************************
     main menu and start of program
     ***********************************************************************/
    public static void main(String[] args) {        
        Sudoku frame = new Sudoku();  			// Create application frame.
        frame.setTitle("Sudoku v0.99"); 		// add title to the frame       
        frame.setSize(new Dimension(470, 480));	// set the size of the frame        
        frame.setResizable(false);				// frame's size is fixed        
        frame.setVisible(true);					// Show frame        
    }
}
