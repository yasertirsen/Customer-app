package lab4_5;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.stream.FileImageOutputStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;


public class CustomerApp extends JFrame implements ActionListener{
	
	
	//form
	private JLabel nameLabel = new JLabel("Customer Name");
    private JTextField nameField = new JTextField(10);
	private JLabel ageLabel = new JLabel("Age");
	private JTextField ageField = new JTextField(10);
	private JLabel emailLabel = new JLabel("Email");
	private JTextField emailField = new JTextField(10);
	private JButton addButton = new JButton("Add");
	private JButton deleteButton = new JButton("Delete");
	private JPanel formPanel = new JPanel();
	private JPanel tblPanel = new JPanel();
	private JTable tbl;
	private CustomerTableModel tm;
	private JScrollPane srl;
	private JMenuBar mBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenuItem fileOpen, fileSave;
	private JFileChooser jfc;
	ArrayList<Customer> customers = new ArrayList<Customer>();
	
	public CustomerApp() {
		
		// Setting up menu
		createMenu();
		
		//Setting up JTable
		tm = new CustomerTableModel(customers);
		tbl = new JTable(tm);
		tbl.setPreferredScrollableViewportSize(new Dimension(500, 70));
        tbl.setFillsViewportHeight(true);
        
        srl = new JScrollPane(tbl);
		
		//create form panel & table panel
		createFormPanel();
		createTblPanel();
		
		//default JFileChooser to current location
		jfc = new JFileChooser();
		jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));

		
		//create file save and open action listener
		fileOpen.addActionListener(this);
		
		fileSave.addActionListener(this);
		
		//create add button action listener
		addButton.addActionListener(this);
		
		deleteButton.addActionListener(this);
		////add form panel
		add(formPanel, BorderLayout.NORTH);
		
		////add table panel
		add(tblPanel, BorderLayout.CENTER);
		
		//add menu bar

		
		//set Frame properties
	    this.setTitle("Customer Management Application");
		this.setVisible(true);
		this.setSize(600, 400);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);;
		
	}
	public void createTblPanel(){
		TitledBorder title = BorderFactory.createTitledBorder("Customer Records");
		tblPanel.setBorder(title);
		tblPanel.add(srl);
		tblPanel.add(deleteButton);
	}
	public void createFormPanel() {
	    formPanel.setLayout(new GridBagLayout());
		TitledBorder title = BorderFactory.createTitledBorder("Customer Details");
		formPanel.setBorder(title);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor =  GridBagConstraints.WEST; 
	    c.gridx = 0;
	    c.gridy = 0;
	    formPanel.add(nameLabel, c);
	    c.gridx = 1;
	    formPanel.add(nameField, c);
		c.gridx = 0;
	    c.gridy = 1;
	    formPanel.add(ageLabel, c);
	    c.gridx = 1;
	    formPanel.add(ageField,c);
		c.gridx = 0;
	    c.gridy = 2;
	    formPanel.add(emailLabel,c);
	    c.gridx = 1;
	    formPanel.add(emailField,c);;
		c.gridx = 0;
	    c.gridy = 3;
	    c.gridwidth = 2; 
	    c.fill = GridBagConstraints.NONE;
	    c.anchor =  GridBagConstraints.CENTER; 
	    formPanel.add(addButton, c);
   }
	public void createMenu(){
		fileOpen = new JMenuItem("Open");
		fileSave = new JMenuItem("Save As");
		fileMenu.add(fileOpen);
		fileMenu.add(fileSave);
		mBar.add(fileMenu);
		setJMenuBar(mBar);
		
	}


	public static void main(String[] args) throws IOException {
		new CustomerApp();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == addButton){
			String name;
			int age;
			String email;
			Customer customer = null;
			if((nameField.getText().isEmpty()) || (ageField.getText().isEmpty())
					|| (emailField.getText().isEmpty()))
				JOptionPane.showMessageDialog(CustomerApp.this, "All fields have a value");
			else{
				try{
					name = nameField.getText();
					age = Integer.parseInt(ageField.getText());
					//if(emailField.getText().matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$"))
					email = emailField.getText();
					tm.fireTableDataChanged();
					customer = new Customer(name, age, email);
					customers.add(customer);
					nameField.setText("");
					ageField.setText("");
					emailField.setText("");
					tm.fireTableDataChanged();
					}
					catch(NumberFormatException error){
						JOptionPane.showMessageDialog(CustomerApp.this, "Age must be a number");
					}
				}
		}
		
		if(e.getSource() == deleteButton){
				String r = null;
				int row = tbl.getSelectedRow();
				if(row == -1)
					r = JOptionPane.showInputDialog(this, "Please input row to be deleted");
				
				row = Integer.parseInt(r);
				System.out.println(row);
				customers.remove(row);
				tm.fireTableRowsDeleted(customers.size(), customers.size());
		}
		
		if(e.getSource() == fileSave){
			if(customers.isEmpty())
				JOptionPane.showMessageDialog(this, "You must have saved records");
			else{
				int returnVal = jfc.showSaveDialog(CustomerApp.this);
				//writing arraylist customer to a n output stream
				File selectedFile;
					try {
						selectedFile = jfc.getSelectedFile();
						System.out.println(selectedFile.getAbsolutePath());
						writeToFile(selectedFile);
					} catch (IOException e1) {
						System.out.println("Problem with file");
					}
					catch (NullPointerException e2) {
						System.out.println("Cancelled");
					}
				if(returnVal == JFileChooser.APPROVE_OPTION){
					customers.clear();
					tm.fireTableDataChanged();
					System.out.println("Saved");
				}
			}
		}
		
		if(e.getSource() == fileOpen){
			int returnVal = jfc.showOpenDialog(CustomerApp.this);
			//writing arraylist customer to a n output stream
			File selectedFile;
			
			try {
				selectedFile = jfc.getSelectedFile();
				System.out.println(selectedFile.getAbsolutePath());
				readFromFile(selectedFile);
				tm.fireTableDataChanged();
		} 	catch (IOException e1) {
				System.out.println("Problem with file");
			}
			catch (NullPointerException e2) {
				 System.out.println("Cancel was selected");
			}
			
		}
		
	}
	
	public void writeToFile(File path) throws IOException {
		FileOutputStream fs = null;
		ObjectOutputStream out = null;
		try {
			//openining stream
			fs = new FileOutputStream(path);
			out = new ObjectOutputStream(fs);
			
			//writing to stream
			out.writeObject(customers);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally{
			fs.close();
			out.close();
		}
	}
	
	public void readFromFile(File path) throws IOException{
		FileInputStream fileIn = null;
		ObjectInputStream in = null;
		ArrayList<Customer> c = new ArrayList<Customer>();
		Customer customer = null;
		try {
			fileIn = new FileInputStream(path);
			in = new ObjectInputStream(fileIn);
			c = (ArrayList<Customer>)in.readObject();
			customers.clear();
			customers.addAll(c);
			}
		catch(EOFException e) {}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			in.close();
			fileIn.close();
		}
	}
}
