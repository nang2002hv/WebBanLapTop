package com.example.NguyenTracNang.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.NguyenTracNang.Controller.Cart;
import com.example.NguyenTracNang.Controller.Customer;
import com.example.NguyenTracNang.Controller.Item;
import com.example.NguyenTracNang.Controller.OrderManagement;
import com.example.NguyenTracNang.Controller.Product;

public class JDBC {
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/online_store";
	private static final String JDBC_USERNAME = "root";
	private static final String JDBC_PASSWORD = "20052002";

	private static final String SELECT_VAI_TRO ="select * from nguoi_dung where so_dien_thoai = ? and password =?";
	private static final String SELECT_PRODUCT_BY_ID = "select *from san_pham where id = ?";
	private static final String INSERT_PRODUCT_BY_ID = "insert into san_pham(cpu,don_gia, don_vi_ban,	don_vi_kho, dung_luong_pin,"
			+ "											he_dieu_hanh,man_hinh,ram,ten_san_pham,thiet_ke,thong_tin_bao_hanh,thong_tin_chung,"
			+ "											ma_danh_muc,ma_hang_sx) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE_PRODUCT_BY_ID = "update san_pham set cpu = ?,don_gia = ?, don_vi_ban = ?,don_vi_kho = ?, dung_luong_pin = ?,"
			+ "			 								he_dieu_hanh = ?,man_hinh = ?,ram =?,ten_san_pham = ?,thiet_ke = ?,thong_tin_bao_hanh = ?,thong_tin_chung = ?,"
			+ "			 								ma_danh_muc = ?,ma_hang_sx = ? where id = ?";

	private static final String DELETE_PRODUCT_BY_ID = "DELETE FROM san_pham WHERE id = ?";
	private static final String SELECT_ALL_CUSTOMER ="SELECT*FROM NGUOI_DUNG";
	private static final String SELECT_CUSTOMER_BY_ID = "SELECT*FROM NGUOI_DUNG WHERE ID =?";
	private static final String INSERT_CUSTOMER_BY_ID ="INSERT INTO NGUOI_DUNG(DIA_CHI,EMAIL,HO_TEN,PASSWORD,SO_DIEN_THOAI,VAI_TRO) VALUES (?,?,?,?,?,?)";
	private static final String UPDATE_CUSTOMER_BY_ID ="UPDATE NGUOI_DUNG SET DIA_CHI = ?,EMAIL = ?,HO_TEN = ?,PASSWORD = ?,SO_DIEN_THOAI = ?,VAI_TRO = ? WHERE ID = ?";
	private static final String DELETE_CUSTOMER_BY_ID = "DELETE FROM NGUOI_DUNG WHERE id = ?";
	
	private static final String Insert_Order = "INSERT INTO GIO_HANG(TONG_TIEN,MA_NGUOI_DUNG,DATE) VALUES (?,?,?)";
	private static final String SELECT_TOP_ONE_Order ="SELECT id FROM GIO_HANG ORDER BY ID DESC LIMIT 1;";
	private static final String Insert_OrderDetail = "INSERT INTO CHI_MUC_GIO_HANG(SO_LUONG,MA_GIO_HANG,MA_SAN_PHAM,DON_GIA) VALUES (?,?,?,?)";
	
	private static final String SELECT_ORDER_CUSTOMER ="select *from nguoi_dung as a, gio_hang b, chi_muc_gio_hang as c, san_pham d where a.so_dien_thoai = ? and a.id = b.ma_nguoi_dung and b.id = c.ma_gio_hang and c.ma_san_pham = d.id ";
	private static final String SELECT_ORDER_DETATIL ="SELECT * FROM online_store.chi_muc_gio_hang as a, gio_hang as b, nguoi_dung as c, san_pham as d where b.id = a.ma_gio_hang and b.ma_nguoi_dung = c.id and a.ma_san_pham = d.id;";
	private static final String add_subtrac_Product_Product ="UPDATE SAN_PHAM SET DON_VI_BAN = ?, DON_VI_KHO = ? WHERE ID = ?";
	
	
	//Chuyển định dạng tiền 100000 --> 100.000 đ
	public  String formatMoney(long money) {
		String moneyString = String.valueOf(money);
		int moneyLength = moneyString.length();
		StringBuilder formattedMoney = new StringBuilder();

		int count = 0;
		for (int i = moneyLength - 1; i >= 0; i--) {
			formattedMoney.append(moneyString.charAt(i));
			count++;
			if (count == 3 && i != 0) {
				formattedMoney.append('.');
				count = 0;
			}
		}

		return formattedMoney.reverse().toString() + " đ";
	}
	
	
	public JDBC() {
		
	}


	//Chuyển đổi định dạng tiền ngược lại
	public  long parseMoney(String formattedMoney) {
	    String cleanMoney = formattedMoney.replace(" đ", "").replace(".", "");

	    try {
	        return Long.parseLong(cleanMoney);
	    } catch (NumberFormatException e) {
	        throw new IllegalArgumentException("Invalid formatted money: " + formattedMoney);
	    }
	}

	public  Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
		return connection;
	}
	
	public Customer login(String so_dien_thoai, String password) {
		Customer customer = null;
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_VAI_TRO);
			ps.setString(1,so_dien_thoai);
			ps.setString(2,password);
			ResultSet resultSet = ps.executeQuery();
			if(resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("ho_ten");
				String email = resultSet.getString("email");
				String password1 = resultSet.getString("password");
				String address = resultSet.getString("dia_chi");
				String phone = resultSet.getString("so_dien_thoai");
				int vai_tro = resultSet.getInt("vai_tro");
				customer = new Customer(id,name, email, address,password1, phone,vai_tro);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customer;
	}

	public  void InsertDATA(String address, String email, String ho_ten, String password, String so_dien_thoai,
			int vai_tro) throws SQLException {
		try {
			Connection connection = getConnection();
			PreparedStatement ps = connection.prepareStatement(
					"Insert into nguoi_dung(dia_chi,email,ho_ten,password,so_dien_thoai,vai_tro) values (?,?,?,?,?,?)");
			ps.setString(1, address);
			ps.setString(2, email);
			ps.setString(3, ho_ten);
			ps.setString(4, password);
			ps.setString(5, so_dien_thoai);
			ps.setInt(6, vai_tro);
			ps.executeUpdate();
		} catch (

		SQLException e) {
			e.printStackTrace();
		}
	}

	

	public  ResultSet getData(String sql) throws SQLException {
		ResultSet resultSet = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			resultSet = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;

	}

	public  List<Product> getAllData() throws SQLException {
		ResultSet resultSet = null;
		List<Product> products = new ArrayList<>();
		String sql = "SELECT *FROM san_pham ";
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				int don_vi_ban = resultSet.getInt("don_vi_ban");
				int don_vi_kho = resultSet.getInt("don_vi_kho");
				long money = resultSet.getLong("don_gia");
				String formattedMoney = formatMoney(money);
				String don_gia = formattedMoney;
				long ma_danh_muc = resultSet.getLong("ma_danh_muc");
				long ma_hang_sx = resultSet.getLong("ma_hang_sx");
				String cpu = resultSet.getString("cpu");
				String dung_luong_pin = resultSet.getString("dung_luong_pin");
				String he_dieu_hanh = resultSet.getString("he_dieu_hanh");
				String man_hinh = resultSet.getString("man_hinh");
				String ram = resultSet.getString("ram");
				String ten_san_pham = resultSet.getString("ten_san_pham");
				String thiet_ke = resultSet.getString("thiet_ke");
				String bao_hanh = resultSet.getString("thong_tin_bao_hanh");
				String thong_tin_chung = resultSet.getString("thong_tin_chung");
				int quantity = 0;
				Product product = new Product(id, don_vi_ban, don_vi_kho, don_gia, ma_danh_muc, ma_hang_sx, cpu,
						dung_luong_pin, he_dieu_hanh, man_hinh, ram, ten_san_pham, thiet_ke, bao_hanh, thong_tin_chung,quantity);
				products.add(product);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;

	}

	public  ResultSet getAllDataHSX(String sql) throws SQLException {
		ResultSet resultSet = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			resultSet = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;

	}

	public  Product getOneProduct(int product_id) {
		Product product = new Product();
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_PRODUCT_BY_ID);
			ps.setInt(1, product_id);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				int don_vi_ban = resultSet.getInt("don_vi_ban");
				int don_vi_kho = resultSet.getInt("don_vi_kho");
				long money = resultSet.getLong("don_gia");
				String formattedMoney = formatMoney(money);
				String don_gia = formattedMoney;
				long ma_danh_muc = resultSet.getLong("ma_danh_muc");
				long ma_hang_sx = resultSet.getLong("ma_hang_sx");
				String cpu = resultSet.getString("cpu");
				String dung_luong_pin = resultSet.getString("dung_luong_pin");
				String he_dieu_hanh = resultSet.getString("he_dieu_hanh");
				String man_hinh = resultSet.getString("man_hinh");
				String ram = resultSet.getString("ram");
				String ten_san_pham = resultSet.getString("ten_san_pham");
				String thiet_ke = resultSet.getString("thiet_ke");
				String bao_hanh = resultSet.getString("thong_tin_bao_hanh");
				String thong_tin_chung = resultSet.getString("thong_tin_chung");
				int quantity = 0;
				product = new Product(id, don_vi_ban, don_vi_kho, don_gia, ma_danh_muc, ma_hang_sx, cpu, dung_luong_pin,
						he_dieu_hanh, man_hinh, ram, ten_san_pham, thiet_ke, bao_hanh, thong_tin_chung,quantity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product;
	}

	public  int insertProduct(Product product) {
		int i = 0;
		try {
			Connection con = getConnection();
			PreparedStatement ps1 = con.prepareStatement(INSERT_PRODUCT_BY_ID);
			ps1.setString(1, product.getCpu());
			ps1.setLong(2, parseMoney(product.getDon_gia()));
			ps1.setInt(3,product.getDon_vi_ban());
			ps1.setInt(4, product.getDon_vi_kho());
			ps1.setString(5, product.getDung_luong_pin());
			ps1.setString(6, product.getHe_dieu_hanh());
			ps1.setString(7, product.getMan_hinh());
			ps1.setString(8, product.getRam());
			ps1.setString(9, product.getTen_san_pham());
			ps1.setString(10, product.getThiet_ke());
			ps1.setString(11, product.getBao_hanh());
			ps1.setString(12, product.getThong_tin_chung());
			ps1.setLong(13, product.getMa_danh_muc());
			ps1.setLong(14, product.getMa_hang_sx());
			ps1.executeUpdate();
			PreparedStatement ps2 = con.prepareStatement("SELECT id FROM san_pham ORDER BY id DESC LIMIT 1");
			ResultSet resultSet = ps2.executeQuery();
			if(resultSet.next()) {
				i = resultSet.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	
	public  void updateProduct(Product product, String key) {
		try {
			Connection con = getConnection();
			PreparedStatement ps1 = con.prepareStatement(UPDATE_PRODUCT_BY_ID);
			ps1.setString(1, product.getCpu());
			ps1.setLong(2, parseMoney(product.getDon_gia()));
			ps1.setInt(3,product.getDon_vi_ban());
			ps1.setInt(4, product.getDon_vi_kho());
			ps1.setString(5, product.getDung_luong_pin());
			ps1.setString(6, product.getHe_dieu_hanh());
			ps1.setString(7, product.getMan_hinh());
			ps1.setString(8, product.getRam());
			ps1.setString(9, product.getTen_san_pham());
			ps1.setString(10, product.getThiet_ke());
			ps1.setString(11, product.getBao_hanh());
			ps1.setString(12, product.getThong_tin_chung());
			ps1.setLong(13, product.getMa_danh_muc());
			ps1.setLong(14, product.getMa_hang_sx());
			ps1.setString(15, key);
			ps1.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteProduct(int id) {
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_PRODUCT_BY_ID);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// CUSTOMER
	public List<Customer> getAllCustomer(){
		List<Customer> customers = new ArrayList<>();
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_ALL_CUSTOMER);
			ResultSet resultSet = ps.executeQuery();
			while(resultSet.next()){
				int id = resultSet.getInt("id");
				String name = resultSet.getString("ho_ten");
				String email = resultSet.getString("email");
				String password = resultSet.getString("password");
				String address = resultSet.getString("dia_chi");
				String phone = resultSet.getString("so_dien_thoai");
				int vai_tro = resultSet.getInt("vai_tro");
				customers.add(new Customer(id,name, email, address, password, phone,vai_tro));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customers;
	}
	
	public Customer getCustomer(int key) {
		Customer customer = new Customer();
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_CUSTOMER_BY_ID);
			ps.setInt(1, key);
			ResultSet resultSet = ps.executeQuery();
			while(resultSet.next()){
				int id = resultSet.getInt("id");
				String name = resultSet.getString("ho_ten");
				String email = resultSet.getString("email");
				String password = resultSet.getString("password");
				String address = resultSet.getString("dia_chi");
				String phone = resultSet.getString("so_dien_thoai");
				int vai_tro = resultSet.getInt("vai_tro");
				customer = new Customer(id,name, email, address,password, phone,vai_tro);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customer;
	}
	
	public void insertCustomer(Customer customer) {
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_CUSTOMER_BY_ID);
			ps.setString(1, customer.getAddress());
			ps.setString(2,customer.getEmail());
			ps.setString(3, customer.getName());
			ps.setString(4, customer.getPassword());
			ps.setString(5, customer.getPhone());
			ps.setInt(6, customer.getVai_tro());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateCustomer(Customer customer, String key) throws SQLException{
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_CUSTOMER_BY_ID);
			ps.setString(1, customer.getAddress());
			ps.setString(2,customer.getEmail());
			ps.setString(3, customer.getName());
			ps.setString(4, customer.getPassword());
			ps.setString(5, customer.getPhone());
			ps.setInt(6, customer.getVai_tro());
			ps.setString(7, key);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void deteleCustomer(int id) {
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_CUSTOMER_BY_ID);
			ps.setInt(1, id);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addOrder(Customer u, Cart cart) {
		LocalDate curDate =java.time.LocalDate.now();
		String date = curDate.toString();
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(Insert_Order);
			ps.setLong(1, parseMoney(cart.getTotalMoney()));
			ps.setInt(2, u.getId());
			ps.setString(3, date);
			ps.executeUpdate();
			PreparedStatement ps1 = con.prepareStatement(SELECT_TOP_ONE_Order);
			ResultSet resultSet = ps1.executeQuery();
			if(resultSet.next()) {
				int iod =resultSet.getInt("id");
				for(Item i : cart.getItems()) {
					PreparedStatement ps3 = con.prepareStatement(Insert_OrderDetail);
					ps3.setInt(1,i.getQuantity());
					ps3.setInt(2,iod);
					ps3.setInt(3,i.getProduct().getId());
					ps3.setDouble(4, i.getPrice());
					ps3.executeUpdate();
					ps3.close();
					int don_vi_kho = i.getProduct().getDon_vi_kho()-i.getQuantity();
					int don_vi_ban = i.getProduct().getDon_vi_ban()+i.getQuantity();
					PreparedStatement ps4 = con.prepareStatement(add_subtrac_Product_Product);
					ps4.setInt(1, don_vi_ban);
					ps4.setInt(2, don_vi_kho);
					ps4.setInt(3, i.getProduct().getId());
					ps4.executeUpdate();
					ps4.close();
				}
			}
			ps.close();
			ps1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<OrderManagement> getALLDonHang() {
		List<OrderManagement> orderManagements = new ArrayList<>();
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_ORDER_DETATIL);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				OrderManagement orderManagement = new OrderManagement();
				orderManagement.getOrderDetail().setId(resultSet.getInt("a.id"));
				orderManagement.getProduct().setTen_san_pham(resultSet.getString("ten_san_pham"));
				orderManagement.getCustomer().setPhone(resultSet.getString("so_dien_thoai"));
				orderManagement.getCustomer().setName(resultSet.getString("ho_ten"));
				orderManagement.getOrderDetail().setPid(resultSet.getInt("ma_san_pham"));
				orderManagement.getOrderDetail().setCid(resultSet.getInt("ma_gio_hang"));
				orderManagement.getOrderDetail().setQuantity(resultSet.getInt("so_luong"));
				orderManagement.getOrderDetail().setPrice(resultSet.getLong("don_gia"));
				orderManagement.getOrder().setDate(resultSet.getString("date"));
				orderManagements.add(orderManagement);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderManagements;
	}
	
	public void deleteOrderManagement(int id) {
		try {
			int cid = 0;
			int ma_san_pham = 0;
			int quantity = 0;
			Connection con = getConnection();
			PreparedStatement ps1 = con.prepareStatement("select * from chi_muc_gio_hang where id = ?");
			ps1.setInt(1, id);
			ResultSet resultSet = ps1.executeQuery();
			if(resultSet.next()) {
				cid = resultSet.getInt("ma_gio_hang");
				ma_san_pham = resultSet.getInt("ma_san_pham");
				quantity = resultSet.getInt("so_luong");
			}
			ps1.close();
			PreparedStatement ps2 = con.prepareStatement("select count(*) as soluong from chi_muc_gio_hang where ma_gio_hang = ? group by ma_gio_hang");
			ps2.setInt(1, cid);
			resultSet = ps2.executeQuery();
			int count = 0;
			if(resultSet.next()) {
				count = resultSet.getInt("soluong");
			}
			ps2.close();
			if(count >= 2) {
				PreparedStatement ps3 = con.prepareStatement("delete from chi_muc_gio_hang where id = ?");
				ps3.setInt(1, id);
				ps3.executeUpdate();
				ps3.close();
			} else {
				PreparedStatement ps3 = con.prepareStatement("delete from chi_muc_gio_hang where id = ?");
				ps3.setInt(1, id);
				ps3.executeUpdate();
				ps3.close();
				PreparedStatement ps4 = con.prepareStatement("delete from gio_hang where id = ?");
				ps4.setInt(1, cid);
				ps4.executeUpdate();
				ps4.close();
			}
			Product product = getOneProduct(ma_san_pham);
			int don_vi_ban = product.getDon_vi_ban()-quantity;
			int don_vi_kho = product.getDon_vi_kho() + quantity;
			PreparedStatement ps3 = con.prepareStatement(add_subtrac_Product_Product);
			ps3.setInt(1, don_vi_ban);
			ps3.setInt(2, don_vi_kho);
			ps3.setInt(3, ma_san_pham);
			ps3.executeUpdate();
			ps3.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<OrderManagement> getALLDonHangCustomer(String phone) {
		List<OrderManagement> orderManagements = new ArrayList<>();
		try {
			Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_ORDER_CUSTOMER);
			ps.setString(1, phone);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				OrderManagement orderManagement = new OrderManagement();
				orderManagement.getOrderDetail().setId(resultSet.getInt("c.id"));
				orderManagement.getProduct().setTen_san_pham(resultSet.getString("ten_san_pham"));
				orderManagement.getCustomer().setPhone(resultSet.getString("so_dien_thoai"));
				orderManagement.getCustomer().setName(resultSet.getString("ho_ten"));
				orderManagement.getOrderDetail().setPid(resultSet.getInt("ma_san_pham"));
				orderManagement.getOrderDetail().setCid(resultSet.getInt("ma_gio_hang"));
				orderManagement.getOrderDetail().setQuantity(resultSet.getInt("so_luong"));
				orderManagement.getOrderDetail().setPrice(resultSet.getLong("don_gia"));
				orderManagement.getOrder().setDate(resultSet.getString("date"));
				orderManagements.add(orderManagement);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderManagements;
	}
}
