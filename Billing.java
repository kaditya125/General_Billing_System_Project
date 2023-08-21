import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Class For Item
class Item 
{
    //Data Members Of Class Item
    private String name;
    private double price;
    private int quantity;

    //Constructor Of Item Class
    public Item(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    //Getter Methods to access private data members. 
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}

//Class For Bill
class Bill 
{
    //List Of Items 
    private List<Item> items;
    //Constructor of Bill Class
    public Bill() {
        items = new ArrayList<>();
    }
    //Function to add Item in items list.
    public void addItem(Item item) {
        items.add(item);
    }
    //Function to get Item in items list.
    public List<Item> getItems() {
        return items;
    }
    //Function to remove Item from items List
    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }
    //Function to calculate total
    public double getTotal() {
        double total = 0.0;

        for (Item item : items) {
            total += item.getPrice() * item.getQuantity();
        }

        return total;
    }
}


public class BillGenerationSystem extends JFrame implements ActionListener 
{
    //Textfeilds for taking user input
    private JTextField itemNameField, priceField, quantityField;
    //Buttons for choosing the task
    private JButton addItemButton, generateBillButton, clearButton, removeItemButton, saveBillButton, loadBillButton, printBillButton;

    private DefaultTableModel tableModel;
    //Table for adding Items 
    private JTable billTable;
    //Instance of Bill Class for particular transaction
    private Bill bill;

    public BillGenerationSystem() 
    {
        //Creating the outer frame
        setTitle("Bill Generation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        //Creating a top container which contains 4 rows 
        /*
         * Item Name(Label)     :      TextFeild
         * Price                :      TextFeild
         * Quantity             :      TextFeild
         * 2 buttons to add and clear items.
         */
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        //Row 1
        JLabel itemNameLabel = new JLabel("Item Name:");
        itemNameField = new JTextField();
        //Row 2
        JLabel priceLabel = new JLabel("Price:");
        priceField = new JTextField();
        //Row 3
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField();
        //Row 4
        addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(this);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);

        //Add items created to top container
        inputPanel.add(itemNameLabel);
        inputPanel.add(itemNameField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);
        inputPanel.add(addItemButton);
        inputPanel.add(clearButton);


        //Create a Table of Items using Jtable and Default Table model
        tableModel = new DefaultTableModel(new Object[]{"Item Name", "Price", "Quantity"}, 0);
        billTable = new JTable(tableModel);

        //To make Scrollable panel put the JTable in Scrollpane
        JScrollPane scrollPane = new JScrollPane(billTable);

        //Creating a Panel for Buttons in bottom with flow layout
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        //Button 1
        generateBillButton = new JButton("Generate Bill");
        generateBillButton.addActionListener(this);
        //Button 2
        removeItemButton = new JButton("Remove Item");
        removeItemButton.addActionListener(this);
        //Button 3
        saveBillButton = new JButton("Save Bill");
        saveBillButton.addActionListener(this);
        //Button 4
        loadBillButton = new JButton("Load Bill");
        loadBillButton.addActionListener(this);
        //Button 5
        printBillButton = new JButton("Print Bill");
        printBillButton.addActionListener(this);
        //Adding Buttons to the panel
        buttonsPanel.add(generateBillButton);
        buttonsPanel.add(removeItemButton);
        buttonsPanel.add(saveBillButton);
        buttonsPanel.add(loadBillButton);
        buttonsPanel.add(printBillButton);

        //Outer Panel which contains all the 3 child panels created
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);

        //Create a new instance of Bill for every transaction
        bill = new Bill();
    }

    public void actionPerformed(ActionEvent e) 
    {
        //When Add Item is Clicked
        if (e.getSource() == addItemButton) 
        {
            //Get the parameters from text feilds
            String itemName = itemNameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            //Condition for error checking for invalid input
            if (itemName.isEmpty() || price <= 0 || quantity <= 0) 
            {
                JOptionPane.showMessageDialog(this, "Please enter valid item details!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //Create an instance of item and add it to the list
            Item item = new Item(itemName, price, quantity);
            bill.addItem(item);

            tableModel.addRow(new Object[]{itemName, price, quantity});

            itemNameField.setText("");
            priceField.setText("");
            quantityField.setText("");

            JOptionPane.showMessageDialog(this, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } 
        else if (e.getSource() == generateBillButton) 
        {
            if (bill.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please add items to generate a bill!", "No Items", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double total = bill.getTotal();
            StringBuilder billText = new StringBuilder();
            billText.append("<html><body><h2>**** Bill ****</h2><br>");
            for (Item item : bill.getItems()) {
                billText.append(item.getName())
                        .append("\t\t$")
                        .append(item.getPrice())
                        .append("\t\t")
                        .append(item.getQuantity())
                        .append("<br>");
            }
            billText.append("--------------------------<br>");
            billText.append("Total:\t\t\t$").append(total).append("<br>");
            billText.append("<h2>**********</h2></body></html>");

            JLabel billLabel = new JLabel(billText.toString());
            billLabel.setFont(new Font("Arial", Font.PLAIN, 16));

            JOptionPane.showMessageDialog(this, billLabel, "Bill", JOptionPane.INFORMATION_MESSAGE);
        } 
        else if (e.getSource() == clearButton) {
            itemNameField.setText("");
            priceField.setText("");
            quantityField.setText("");
        } 
        else if (e.getSource() == removeItemButton) {
            int selectedIndex = billTable.getSelectedRow();
            if (selectedIndex != -1) {
                bill.removeItem(selectedIndex);
                tableModel.removeRow(selectedIndex);
                JOptionPane.showMessageDialog(this, "Item removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to remove!", "No Item Selected", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (e.getSource() == saveBillButton) {
            if (bill.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please add items to save a bill!", "No Items", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    FileWriter writer = new FileWriter(filePath);
                    for (Item item : bill.getItems()) {
                        writer.write(item.getName() + "\t" + item.getPrice() + "\t" + item.getQuantity() + "\n");
                    }
                    writer.close();
                    JOptionPane.showMessageDialog(this, "Bill saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error occurred while saving the bill!", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        } 
        else if (e.getSource() == loadBillButton) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    tableModel.setRowCount(0);
                    java.util.Scanner scanner = new java.util.Scanner(new java.io.File(filePath));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] parts = line.split("\t");
                        if (parts.length == 3) {
                            String itemName = parts[0];
                            double price = Double.parseDouble(parts[1]);
                            int quantity = Integer.parseInt(parts[2]);
                            Item item = new Item(itemName, price, quantity);
                            bill.addItem(item);
                            tableModel.addRow(new Object[]{itemName, price, quantity});
                        }
                    }
                    scanner.close();
                    JOptionPane.showMessageDialog(this, "Bill loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error occurred while loading the bill!", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        } 
        else if (e.getSource() == printBillButton) {
            try {
                billTable.print();
            } catch (java.awt.print.PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Error occurred while printing the bill!", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BillGenerationSystem billGenerationSystem = new BillGenerationSystem();
                billGenerationSystem.pack();
                billGenerationSystem.setLocationRelativeTo(null);
                billGenerationSystem.setVisible(true);
            }
        });
    }
}
