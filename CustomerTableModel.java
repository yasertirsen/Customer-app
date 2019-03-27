package lab4_5;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import lab2Exercise2.Movie;

public class CustomerTableModel extends AbstractTableModel {
	
	private String[] columnNames; 
	
	private ArrayList<Customer> data = new ArrayList<Customer>();
	
	public CustomerTableModel(ArrayList<Customer> dataRef) {
		this.columnNames = new String[]{ 
				"Name",
				"Age",
				"Email"};
		data = dataRef;
		
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		Customer customer = data.get(row);
		if(col == 0)
			return customer.getName();
		else if(col == 1)
			return customer.getAge();
		else if(col == 2)
			return customer.getEmail();
		
		return null;
	}
	@Override
	public String getColumnName(int col) {
        return columnNames[col];
	}
	@Override
	public Class getColumnClass(int columnIndex) {
		return getValueAt(0,columnIndex).getClass();
	}
	@Override
	  public boolean isCellEditable(int row, int col) {
			return true;
	  }
	
    public void setValueAt(Customer customer, int row, int col) {
        data.add(row, customer);
        fireTableCellUpdated(row, col);
    }

}
