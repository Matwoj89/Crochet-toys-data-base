package Amigurumi;

import java.awt.EventQueue;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import datechooser.beans.DateChooserCombo;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.awt.event.ActionEvent;
import com.toedter.calendar.JDateChooser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

public class Main_Window extends javax.swing.JFrame {

	private JFrame frame;
	private JTextField txt_id;
	private JTextField txt_name;
	private JTextField txt_price;
	private JDateChooser txt_AddDate;
	private JTable JTable_Products;
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_Window window = new Main_Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	 // Create the application.
	
	public Main_Window() {
		initialize();
		getConnection();
		Show_Products_In_JTable();
	}

	String ImgPath = null;
	int pos = 0;
	private JLabel lbl_image;

	// Function To Connect To MySQL Database
	
	public Connection getConnection() {

		Connection con = null;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/products_db?autoReconnect=true&useSSL=false", "root", "");
			;
			return con;
		} catch (SQLException ex) {
			Logger.getLogger(Main_Window.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	// Check Input Fields

	public boolean checkInputs() {
		if (txt_name.getText() == null || txt_price.getText() == null || txt_AddDate.getDate() == null) {
			return false;
		} else {
			try {
				Float.parseFloat(txt_price.getText());
				return true;
			} catch (Exception ex) {
				return false;
			}
		}
	}

	// Resize Image

	public ImageIcon ResizeImage(String imagePath, byte[] pic) {
		ImageIcon myImage = null;

		if (imagePath != null) {
			myImage = new ImageIcon(imagePath);
		} else {
			myImage = new ImageIcon(pic);
		}

		Image img = myImage.getImage();
		Image img2 = img.getScaledInstance(lbl_image.getWidth(), lbl_image.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(img2);
		return image;
	}

	// Display Data in Table
	// 1 - Fill ArrayList with data

	public ArrayList<Product> getProductList() {
		ArrayList<Product> productList = new ArrayList<Product>();
		Connection con = getConnection();
		String query = "SELECT * FROM products";

		Statement st;
		ResultSet rs;

		try {

			st = con.createStatement();
			rs = st.executeQuery(query);
			Product product;

			while (rs.next()) {
				product = new Product(rs.getInt("id"), rs.getString("name"), Float.parseFloat(rs.getString("price")),
						rs.getString("add_date"), rs.getBytes("image"));
				productList.add(product);
			}

		} catch (SQLException ex) {
			Logger.getLogger(Main_Window.class.getName()).log(Level.SEVERE, null, ex);
		}

		return productList;
	}

	// 2- Populate the JTable

	public void Show_Products_In_JTable() {
		ArrayList<Product> list = getProductList();
		DefaultTableModel model = (DefaultTableModel) JTable_Products.getModel();
		// clear table
		model.setRowCount(0);
		Object[] row = new Object[4];
		for (int i = 0; i < list.size(); i++) {
			row[0] = list.get(i).getId();
			row[1] = list.get(i).getName();
			row[2] = list.get(i).getPrice();
			row[3] = list.get(i).getAddDate();

			model.addRow(row);
		}
	}

	public void ShowItem(int index) {
		txt_id.setText(Integer.toString(getProductList().get(index).getId()));
		txt_name.setText(getProductList().get(index).getName());
		txt_price.setText(Float.toString(getProductList().get(index).getPrice()));

		try {
			Date addDate = null;
			addDate = new SimpleDateFormat("yyyy-MM-dd").parse((String) getProductList().get(index).getAddDate());
			txt_AddDate.setDate(addDate);
		} catch (ParseException ex) {
			Logger.getLogger(Main_Window.class.getName()).log(Level.SEVERE, null, ex);
		}

		lbl_image.setIcon(ResizeImage(null, getProductList().get(index).getImage()));
	}

	
	 // Initialize the contents of the frame.
	 
	private void initialize() {

		txt_name = new javax.swing.JTextField();
		txt_id = new javax.swing.JTextField();
		txt_price = new javax.swing.JTextField();
		txt_AddDate = new com.toedter.calendar.JDateChooser();
		lbl_image = new javax.swing.JLabel();
		JTable_Products = new javax.swing.JTable();

		frame = new JFrame();
		frame.setBounds(100, 100, 999, 606);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(51, 204, 255));
		panel.setBounds(0, 0, 983, 567);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		lbl_image = new JLabel("");
		lbl_image.setBackground(new Color(255, 0, 255));
		lbl_image.setBounds(204, 95, 415, 420);
		panel.add(lbl_image);

		JLabel lblNewLabel = new JLabel("Crochet toys data base");
		lblNewLabel.setFont(new Font("Lucida Handwriting", Font.BOLD, 40));
		lblNewLabel.setBounds(31, 0, 588, 97);
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("What is it?");
		lblNewLabel_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1.setFont(new Font("Lucida Calligraphy", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(721, 11, 113, 20);
		panel.add(lblNewLabel_1);

		JLabel lblId = new JLabel("ID:");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblId.setBounds(390, 526, 25, 33);
		panel.add(lblId);

		txt_id = new JTextField();
		txt_id.setFont(new Font("Tahoma", Font.BOLD, 13));
		txt_id.setEnabled(false);
		txt_id.setBounds(418, 526, 25, 36);
		panel.add(txt_id);
		txt_id.setColumns(10);

		JButton Btn_First = new JButton("First");
		Btn_First.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pos = 0;
				ShowItem(pos);
			}
		});
		Btn_First.setBounds(208, 526, 64, 36);
		panel.add(Btn_First);

		JButton Btn_Next = new JButton("");
		Btn_Next.setIcon(new ImageIcon("C:\\Users\\user\\Desktop\\Right.png"));
		Btn_Next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pos++;

				if (pos >= getProductList().size()) {
					pos = getProductList().size() - 1;
				}

				ShowItem(pos);
			}
		});
		Btn_Next.setBounds(447, 526, 98, 36);
		panel.add(Btn_Next);

		JButton Btn_Previous = new JButton("");
		Btn_Previous.setIcon(new ImageIcon("C:\\Users\\user\\Desktop\\left2.png"));
		Btn_Previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pos--;

				if (pos < 0) {
					pos = 0;
				}

				ShowItem(pos);
			}
		});
		Btn_Previous.setBounds(282, 526, 98, 36);
		panel.add(Btn_Previous);

		JButton Btn_Last = new JButton("Last");
		Btn_Last.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pos = getProductList().size() - 1;
				ShowItem(pos);
			}
		});
		Btn_Last.setBounds(555, 526, 64, 36);
		panel.add(Btn_Last);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(629, 121, 341, 223);
		panel.add(scrollPane_1);

		JTable_Products = new JTable();
		JTable_Products.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		scrollPane_1.setViewportView(JTable_Products);
		JTable_Products
				.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Name", "Price", "Add Date" }));
		JTable_Products.setColumnSelectionAllowed(true);
		JTable_Products.setCellSelectionEnabled(true);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(629, 42, 341, 68);
		panel.add(scrollPane);

		JTextArea txtramigurumilitCrocheted = new JTextArea();
		scrollPane.setViewportView(txtramigurumilitCrocheted);
		txtramigurumilitCrocheted.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		txtramigurumilitCrocheted.setBackground(new Color(255, 255, 153));
		txtramigurumilitCrocheted.setWrapStyleWord(true);
		txtramigurumilitCrocheted.setTabSize(10);
		txtramigurumilitCrocheted.setText(
				"Amigurumi (lit. crocheted or knitted stuffed toy)\r\n"
				+ "is the Japanese art of knitting or crocheting small,\r\n"
				+ "stuffed yarn creatures. The word is a portmanteau\r\n"
				+ "of the Japanese words ami, meaning crocheted\r\n"
				+ "or knitted, and nuigurumi, meaning stuffed doll.\r\n"
				+ "In the West they are called amigurumi, which\r\n"
				+ "are vary in size and there are no restrictions about\r\n"
				+ "size or look. While the art of amigurumi has been\r\n"
				+ "known in Japan for several decades, the craft first\r\n"
				+ "started appealing to the masses. In other countries,\r\n"
				+ "especially in the West, in 2003. By 2006, amigurumi\r\n"
				+ "were reported to be some of the most popular\r\n"
				+ "items on Etsy, an online craft marketplace, where\r\n"
				+ "they typically sold for $10 to $100.\r\n"
				+ "Since then, popularity has continued to increase.");

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(51, 153, 255));
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_1.setBounds(10, 95, 186, 420);
		panel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 221, 76, 20);
		panel_1.add(lblName);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setBounds(10, 252, 76, 20);
		panel_1.add(lblPrice);
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JLabel lblAddDate = new JLabel("Date:");
		lblAddDate.setBounds(10, 283, 76, 20);
		panel_1.add(lblAddDate);
		lblAddDate.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JTextArea txtrIfUWant = new JTextArea();
		txtrIfUWant.setWrapStyleWord(true);
		txtrIfUWant.setText("For add new Crochet toy\r\n please use this panel\r\n");
		txtrIfUWant.setTabSize(10);
		txtrIfUWant.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		txtrIfUWant.setBackground(new Color(51, 204, 255));
		txtrIfUWant.setBounds(21, 30, 147, 34);
		panel_1.add(txtrIfUWant);

		JButton Btn_Choose_Image = new JButton("Choose image");
		Btn_Choose_Image.setBounds(10, 88, 166, 44);
		panel_1.add(Btn_Choose_Image);

		JDateChooser txt_AddDate = new JDateChooser();
		txt_AddDate.setBounds(62, 283, 118, 20);
		panel_1.add(txt_AddDate);

		txt_price = new JTextField();
		txt_price.setBounds(62, 252, 118, 20);
		panel_1.add(txt_price);
		txt_price.setColumns(10);

		txt_name = new JTextField();
		txt_name.setBounds(62, 223, 118, 20);
		panel_1.add(txt_name);
		txt_name.setColumns(10);

		JTextArea txtrIfEverythingIs = new JTextArea();
		txtrIfEverythingIs.setLineWrap(true);
		txtrIfEverythingIs.setWrapStyleWord(true);
		txtrIfEverythingIs.setText("If everything is correct\r\n      click \"Insert\"");
		txtrIfEverythingIs.setTabSize(10);
		txtrIfEverythingIs.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		txtrIfEverythingIs.setBackground(new Color(51, 204, 255));
		txtrIfEverythingIs.setBounds(28, 328, 140, 34);
		panel_1.add(txtrIfEverythingIs);

		JButton btn_Insert = new JButton("Insert");
		btn_Insert.setBounds(10, 373, 168, 36);
		panel_1.add(btn_Insert);

		JTextArea txtrInsertValuesBelow = new JTextArea();
		txtrInsertValuesBelow.setWrapStyleWord(true);
		txtrInsertValuesBelow.setText("Insert values below");
		txtrInsertValuesBelow.setTabSize(10);
		txtrInsertValuesBelow.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		txtrInsertValuesBelow.setBackground(new Color(51, 204, 255));
		txtrInsertValuesBelow.setBounds(35, 172, 118, 20);
		panel_1.add(txtrInsertValuesBelow);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(30, 144, 255));
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel_2.setBounds(629, 368, 341, 147);
		panel.add(panel_2);
		panel_2.setLayout(null);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(240, 11, 91, 66);
		panel_2.add(btnUpdate);

		// Button Delete The Data From MySQL Database

		JButton btnDelete = new JButton("Delete");
		btnDelete.setForeground(new Color(0, 0, 0));
		btnDelete.setBackground(new Color(255, 0, 0));
		btnDelete.setBounds(240, 88, 91, 39);
		panel_2.add(btnDelete);

		JTextArea txtrSelectToyWhich = new JTextArea();
		txtrSelectToyWhich.setWrapStyleWord(true);
		txtrSelectToyWhich.setText(
				"Select toy which u want to correct.\r\nThen insert propert data in sections:\r\nName, price, date or choose new img.\r\nAfter that click \"Update\".");
		txtrSelectToyWhich.setTabSize(10);
		txtrSelectToyWhich.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		txtrSelectToyWhich.setBackground(new Color(51, 204, 255));
		txtrSelectToyWhich.setBounds(10, 11, 220, 66);
		panel_2.add(txtrSelectToyWhich);

		JTextArea txtrForRemoveToy = new JTextArea();
		txtrForRemoveToy.setLineWrap(true);
		txtrForRemoveToy.setWrapStyleWord(true);
		txtrForRemoveToy.setText("To remove toy from data base click \"Delete\".");
		txtrForRemoveToy.setTabSize(10);
		txtrForRemoveToy.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		txtrForRemoveToy.setBackground(new Color(51, 204, 255));
		txtrForRemoveToy.setBounds(10, 88, 220, 34);
		panel_2.add(txtrForRemoveToy);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!txt_id.getText().equals("")) {
					try {
						Connection con = getConnection();
						PreparedStatement ps = con.prepareStatement("DELETE FROM products WHERE id = ?");
						int id = Integer.parseInt(txt_id.getText());
						ps.setInt(1, id);
						ps.executeUpdate();
						Show_Products_In_JTable();
						JOptionPane.showMessageDialog(null, "Product Deleted");
					} catch (SQLException ex) {
						Logger.getLogger(Main_Window.class.getName()).log(Level.SEVERE, null, ex);
						JOptionPane.showMessageDialog(null, "Product Not Deleted");
					}

				} else {
					JOptionPane.showMessageDialog(null, "Product Not Deleted : No Id to Delete");
				}

			}
		});
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (checkInputs() && txt_id.getText() != null) {
					String UpdateQuery = null;
					PreparedStatement ps = null;
					Connection con = getConnection();

					// update without image
					if (ImgPath == null) {
						try {
							UpdateQuery = "UPDATE products SET name = ?, price = ?" + ", add_date = ? WHERE id = ?";
							ps = con.prepareStatement(UpdateQuery);

							ps.setString(1, txt_name.getText());
							ps.setString(2, txt_price.getText());

							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							String addDate = dateFormat.format(txt_AddDate.getDate());

							ps.setString(3, addDate);

							ps.setInt(4, Integer.parseInt(txt_id.getText()));

							ps.executeUpdate();
							Show_Products_In_JTable();
							JOptionPane.showMessageDialog(null, "Product Updated");

						} catch (SQLException ex) {
							Logger.getLogger(Main_Window.class.getName()).log(Level.SEVERE, null, ex);
						}

					}
					// update with image
					else {

						try {
							InputStream img = new FileInputStream(new File(ImgPath));

							UpdateQuery = "UPDATE products SET name = ?, price = ?"
									+ ", add_date = ?, image = ? WHERE id = ?";

							ps = con.prepareStatement(UpdateQuery);

							ps.setString(1, txt_name.getText());
							ps.setString(2, txt_price.getText());

							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							String addDate = dateFormat.format(txt_AddDate.getDate());

							ps.setString(3, addDate);

							ps.setBlob(4, img);

							ps.setInt(5, Integer.parseInt(txt_id.getText()));

							ps.executeUpdate();
							Show_Products_In_JTable();
							JOptionPane.showMessageDialog(null, "Product Updated");

						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}

					}
				} else {
					JOptionPane.showMessageDialog(null, "One Or More Fields Are Empty Or Wrong");
				}
			}

		});
		btn_Insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (ImgPath != null) {
					try {
						Connection con = getConnection();
						PreparedStatement ps = con.prepareStatement(
								"INSERT INTO products(name,price,add_date,image)" + "values(?,?,?,?)");
						ps.setString(1, txt_name.getText());
						ps.setString(2, txt_price.getText());

						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						String AddDate = dateFormat.format(txt_AddDate.getDate());
						ps.setString(3, AddDate);

						InputStream img = new FileInputStream(new File(ImgPath));
						ps.setBlob(4, img);
						ps.executeUpdate();
						Show_Products_In_JTable();

						JOptionPane.showMessageDialog(null, "Data Inserted");

					} catch (Exception ex) {

						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(null, "One or More Field Are Empty");
				}

				System.out.println("Name => " + txt_name.getText());
				System.out.println("Price => " + txt_price.getText());
				System.out.println("Image => " + ImgPath);
			}
		});
		Btn_Choose_Image.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser file = new JFileChooser();
				file.setCurrentDirectory(new File(System.getProperty("user.home")));

				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.image", "jpg", "png");
				file.addChoosableFileFilter(filter);
				int result = file.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = file.getSelectedFile();
					String path = selectedFile.getAbsolutePath();
					lbl_image.setIcon(ResizeImage(path, null));
					ImgPath = path;
				} else {
					System.out.println("No File Selected");
				}

			}

		});

	}

	private javax.swing.JScrollPane jScrollPane1;
};
