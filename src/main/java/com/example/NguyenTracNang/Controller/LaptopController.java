package com.example.NguyenTracNang.Controller;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.NguyenTracNang.JDBC.JDBC;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LaptopController {
	static int isadmin = 0;
	static int isLogin = 0;
	static int check1 = 0;	// Biến để lưu trạng thái đăng nhập
	static String priceSet = "Tất cả";
	static String brandSet = "Tất cả";
	
	JDBC jdbc = new JDBC();
	
	public static String formatMoney(long money) {
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
	
	public  long parseMoney(String formattedMoney) {
	    String cleanMoney = formattedMoney.replace(" đ", "").replace(".", "");

	    try {
	        return Long.parseLong(cleanMoney);
	    } catch (NumberFormatException e) {
	        throw new IllegalArgumentException("Invalid formatted money: " + formattedMoney);
	    }
	}
	@GetMapping("/client")
	public String Latop(Model model) throws IOException {
		model.addAttribute("check1", check1);
		return "client/trangchu";
	}

	@GetMapping("/severlogin")
	public String login(Model model) throws IOException {
		model.addAttribute("check1", check1);
		isLogin = 1;
		Customer customer = new Customer();
		model.addAttribute("isLogin", 1);
		model.addAttribute("customer", customer);
		return "client/trangchu";
	}
	
	@GetMapping("/checkout")
	public String checkout(Model model, HttpSession session) {
		session.removeAttribute("customer_login");
		check1= 0;
		return "redirect:/severlogin";
	}

	@GetMapping("/register")
	public String register(Model model) throws IOException {
		
		model.addAttribute("check1", check1);
		isLogin = 0;
		Customer customer = new Customer();
		model.addAttribute("isLogin", 0);
		model.addAttribute("customer", customer);
		return "client/trangchu";

	}

	@GetMapping("/laptopaz")
	public String laptopaz(Model model) throws IOException, SQLException {
		model.addAttribute("check1", check1);
		
		List<Product> list = jdbc.getAllData();;
		
		isLogin = 2;
		model.addAttribute("brand", "");
		model.addAttribute("price", "");
		model.addAttribute("brandSet", brandSet);
		model.addAttribute("priceSet", priceSet);
		model.addAttribute("list", list);
		model.addAttribute("isLogin", 2);
		return "client/trangchu";
	}

	@GetMapping("/laptopaz1")
	public String laptopHSX(Model model, HttpServletRequest request) throws IOException {
		String brand = request.getParameter("brand");
		String price = request.getParameter("price_range");
		String search = request.getParameter("search");
		model.addAttribute("check1", check1);
		List<Product> list = new ArrayList<>();
		ResultSet resultSet = null;
		String sql = "SELECT *FROM san_pham,hang_san_xuat where san_pham.ma_hang_sx = hang_san_xuat.id";
		if (search != "") {
			sql += " and san_pham.ten_san_pham like " + "'%" + search + "%'";
		}
		if (brand != null && brand != "") {
			brandSet = brand;
			sql += " and ten_hang_san_xuat =" + "'" + brand + "'";
		} else {
			brandSet = "Tất cả";
		}
		if (price != null && price != "") {
			if (price.equals("5-10")) {
				priceSet = "5 triệu - 10 triệu";
			}
			if (price.equals("10-20")) {
				priceSet = "10 triệu - 20 triệu";
			}
			if (price.equals("20-30")) {
				priceSet = "20 triệu - 30 triệu";
			}
			if (price.equals("30-40")) {
				priceSet = "30 triệu - 40 triệu";
			}
			if (price.equals("40-50")) {
				priceSet = "40 triệu - 50 triệu";
			}
			if (price.equals("Over-50")) {
				priceSet = "Trên 50 triệu";
			}
			String[] parts = price.split("-");
			String part1 = parts[0];
			String part2 = parts[1];
			if (part1.equals("over")) {
				sql += " and don_gia >= " + 50000000;
			} else {
				int min = Integer.valueOf(part1) * 1000000;
				int max = Integer.valueOf(part2) * 1000000;
				sql += " and don_gia >= " + min + " and don_gia <= " + max;
			}
		} else {
			priceSet = "Tất Cả";
		}
		try {
			resultSet = jdbc.getAllDataHSX(sql);
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
						dung_luong_pin, he_dieu_hanh, man_hinh, ram, ten_san_pham, thiet_ke, bao_hanh,
						thong_tin_chung, quantity);
				list.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("brand", brand);
		model.addAttribute("price", price);
		model.addAttribute("brandSet", brandSet);
		model.addAttribute("priceSet", priceSet);
		model.addAttribute("list", list);
		model.addAttribute("isLogin", 2);
		return "client/trangchu";
	}

	@GetMapping("/product")
	public String product(Model model, HttpServletRequest request) throws IOException {
		int id = Integer.valueOf(request.getParameter("id"));
		String sql = "Select *from san_pham where id =" + id;
		ResultSet resultSet = null;
		List<Product> list = new ArrayList<>();
		try {
			resultSet = jdbc.getData(sql);
			while (resultSet.next()) {
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
				Product product = new Product(id, don_vi_ban, don_vi_kho, don_gia, ma_danh_muc, ma_hang_sx, cpu, dung_luong_pin,
						he_dieu_hanh, man_hinh, ram, ten_san_pham, thiet_ke, bao_hanh, thong_tin_chung,quantity);
				list.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("check1", check1);
		
		model.addAttribute("product", list.get(0));
		model.addAttribute("isLogin", 3);
		return "client/trangchu";
	}
	

	@PostMapping("/ktlogin")
	public String ktlogin(@ModelAttribute("customer") Customer customer, Model model,HttpSession session) throws IOException, SQLException {		
		Customer customer1 = jdbc.login(customer.getPhone(), customer.getPassword());
		if(customer1 != null) {
			if (customer1.getVai_tro() == 1) {
				session.removeAttribute("loi_dang_nhap");
				List<Product> list = new ArrayList<>();
				list = jdbc.getAllData();
				model.addAttribute("isadmin", -1);
				model.addAttribute("products", list);
				return "admin/admin";
			} else {
				if(customer1.getVai_tro() == 2) {
					session.setAttribute("customer_login", customer1);
					session.removeAttribute("loi_dang_nhap");
					if (customer1.getName() != "") {
						check1 = 1;
						model.addAttribute("check1", check1);
						return "client/trangchu";
					}
				}
			}
		}
		return "redirect:/severlogin";
	}

	@PostMapping("/ktregister")
	public String ktregister(@ModelAttribute("customer") Customer customer, Model model) throws IOException {
		model.addAttribute("check1", check1);
		try {
			String phone = customer.getPhone();
			String password = customer.getPassword();
			String name = customer.getName();
			String email = customer.getEmail();
			String address = customer.getAddress();
			int vaitro = 2;
			jdbc.InsertDATA(address, email, name, password, phone, vaitro);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("isLogin", 1);
		return "client/trangchu";
	}
	
	@GetMapping("/information")
	public String information (Model model, HttpSession session) {
		Customer customer = (Customer)session.getAttribute("customer_login");
		List<OrderManagement> orderManagements = jdbc.getALLDonHangCustomer(customer.getPhone());
		model.addAttribute("orderManagements", orderManagements);
		model.addAttribute("check1", check1);
		model.addAttribute("isLogin",5);
		return "client/trangchu";
	}
	
	@PostMapping("/getinformation")
	public String getinformation(Model model, Customer customer,HttpSession session) throws SQLException {
		Customer customer2 = (Customer)session.getAttribute("customer_login");
		customer.setVai_tro(customer2.getVai_tro());
		customer.setPhone(customer2.getPhone());
		customer.setId(customer2.getId());
		jdbc.updateCustomer(customer, String.valueOf(customer2.getId()));
		session.setAttribute("customer_login", customer);
		return "redirect:/information";
	}
	
	@GetMapping("/qlsp")
	public String qlsp(Model model,HttpServletRequest request) throws SQLException {
		List<Product> list = new ArrayList<>();
		list = jdbc.getAllData();
		model.addAttribute("isadmin", -1);
		model.addAttribute("products", list);
		return "admin/admin";
	}
	
	@GetMapping("/oneproduct")
	public String Oneproduct(Model model,HttpServletRequest request) {
		int id = Integer.valueOf(request.getParameter("id"));
		Product product = jdbc.getOneProduct(id);
		model.addAttribute("product",product);
		model.addAttribute("isadmin",1);
		return "admin/admin";
	}
	
	@GetMapping("productnew")
	public String productNew(Model model,HttpServletRequest request) {
		String key = request.getParameter("id");
		Product product = jdbc.getOneProduct(Integer.valueOf(key));
		model.addAttribute("product",product);
		model.addAttribute("isadmin",2);
		return "admin/admin";
	}
	
	@PostMapping("/insertproduct")
	public String insertproduct(Model model, Product product, @RequestParam("file") MultipartFile file) {
		int i = jdbc.insertProduct(product);
		System.out.println(i);
		if (!file.isEmpty()) {
	        try {  
	            String originalFileName = String.valueOf(i)+".png";            
	            String fileName = "C:/Users/ASUS/Downloads/NguyenTracNang/NguyenTracNang/src/main/resources/static/img/" + originalFileName;
	            file.transferTo(new File(fileName));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
		model.addAttribute("isadmin",-1);
		return "redirect:/qlsp";
	}
	
	@GetMapping("/deleteproduct")
	public String deleteproduct(Model model, HttpServletRequest request) {
		int id = Integer.valueOf(request.getParameter("id"));
		Product product = jdbc.getOneProduct(id);
		model.addAttribute("product",product);
		model.addAttribute("isadmin",3);
		return "admin/admin";
	}
	
	@PostMapping("/deleteproduct")
	public String delete_product(Model model, HttpServletRequest request) {
		
		int id = Integer.valueOf(request.getParameter("id"));
		jdbc.deleteProduct(id);
		return "redirect:/qlsp";
		
	}
	
	@PostMapping("/productsp")
	public String productSP(Model model,Product product, HttpServletRequest request){
		String key = request.getParameter("id");
		jdbc.updateProduct(product, key);
		model.addAttribute("isadmin",-1);
		return "redirect:/qlsp";
	}
	
	
	@GetMapping("/allcustomer")
	public String allcustomer(Model model) {
		List<Customer> customers = jdbc.getAllCustomer();
		model.addAttribute("customers", customers);
		model.addAttribute("isadmin",5);
		return "admin/admin";
	}
	
	@GetMapping("/onecustomer")
	public String Onecustomer(Model model,HttpServletRequest request) {
		int id = Integer.valueOf(request.getParameter("id"));
		Customer customer = jdbc.getCustomer(id);
		model.addAttribute("customer",customer);
		model.addAttribute("isadmin",6);
		return "admin/admin";
	}
	
	@PostMapping("/customersp")
	public String customerSP(Model model,Customer customer, HttpServletRequest request) throws SQLException{
		String key = request.getParameter("id");
		jdbc.updateCustomer(customer, key);
		return "redirect:/allcustomer";
	}
	
	@GetMapping("/deletecustomer")
	public String deletecustomer(Model model, HttpServletRequest request) {
		int id = Integer.valueOf(request.getParameter("id"));
		Customer customer = jdbc.getCustomer(id);
		model.addAttribute("customer", customer);
		model.addAttribute("isadmin",4);
		return "admin/admin";
	}
	
	@PostMapping("/deletecustomer")
	public String delete_customer(Model model, HttpServletRequest request) {
		int id = Integer.valueOf(request.getParameter("id"));
		jdbc.deteleCustomer(id);
		return "redirect:/allcustomer";
		
	}
	
	@GetMapping("/customernew")
	public String customerNew(Model model,HttpServletRequest request) {
		String key = request.getParameter("id");
		Customer customer= jdbc.getCustomer(Integer.valueOf(key));
		model.addAttribute("customer",customer);
		model.addAttribute("isadmin",7);
		return "admin/admin";
	}
	
	@PostMapping("/insertcustomer")
	public String insertcustomer(Model model, Customer customer) {
		jdbc.insertCustomer(customer);
		return "redirect:/allcustomer";
	}
	
	@GetMapping("/shopping_cart")
	public String viewCart(Model model,HttpSession session,HttpServletRequest request) {
		Cart cart = null;
		Object o = session.getAttribute("cart");
		//có rồi
		if(o != null) {
			cart = (Cart)o;
		} else {
			cart = 	new Cart();
		}
		int num, id;
		try {
			num = 1;
			id= Integer.valueOf(request.getParameter("id"));
			Product product = jdbc.getOneProduct(id);
			long price = parseMoney(product.getDon_gia());
			
			Item t= new Item(product,num,price);
			cart.addItem(t);
			
		} catch (Exception e) {
			num = 1;
		}
		List<Item> list = cart.getItems();
		session.setAttribute("cart", cart);
		session.setAttribute("size", list.size());
		
		model.addAttribute("check1", check1);
		model.addAttribute("isLogin",4);
		return "client/trangchu";
	}
	@GetMapping("/process")
	public String viewCartprocess_1(Model model, HttpSession session, HttpServletRequest request) {
		Cart cart = null;
		Object o = session.getAttribute("cart");
		// có rồi
		if (o != null) {
			cart = (Cart) o;
		} else {
			cart = new Cart();
		}
		int num, id;
		try {
			num = Integer.valueOf(request.getParameter("num"));
			id = Integer.valueOf(request.getParameter("id"));
			if (num == -1 && (cart.getQuantityByID(id) <= 1)) {
				cart.removeItem(id);
			} else {
				Product product = jdbc.getOneProduct(id);
				long price = parseMoney(product.getDon_gia());

				Item t = new Item(product, num, price);
				cart.addItem(t);
			}
		} catch (Exception e) {
			num = 1;
		}
		List<Item> list = cart.getItems();
		session.setAttribute("cart", cart);
		session.setAttribute("size", list.size());
		
		model.addAttribute("check1", check1);
		model.addAttribute("isLogin", 4);
		return "client/trangchu";
	}
	@PostMapping("/process1")
	public String viewCartprocess_2(Model model, HttpSession session, HttpServletRequest request) {
		Cart cart = null;
		System.out.println("hello");
		Object o = session.getAttribute("cart");
		// có rồi
		if (o != null) {
			cart = (Cart) o;
		} else {
			cart = new Cart();
		}
		int id = Integer.valueOf(request.getParameter("id"));
		cart.removeItem(id);
		List<Item> list = cart.getItems();
		session.setAttribute("cart", cart);
		session.setAttribute("size", list.size());
		
		model.addAttribute("check1", check1);
		model.addAttribute("isLogin", 4);
		return "client/trangchu";
	}
	
	@PostMapping("/thanhtoan")
	public String thanhtoan(Model model,HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		Cart cart = null;
		Object o = session.getAttribute("cart");
		if(o != null) {
			cart = (Cart)o;
		} else {
			cart = new Cart();
		}
		Customer acount = null;
		Object a = session.getAttribute("customer_login");
		if(a != null) {
			acount =(Customer)a;
			jdbc.addOrder(acount, cart);
			session.removeAttribute("cart");
			session.removeAttribute("size");
		} else {
			return "redirect:/severlogin";
		}
		return "redirect:/client";
	}
	
	@GetMapping("/ordermanagement")
	public String getOrderManagement(Model model) {
		List<OrderManagement> orderManagements = jdbc.getALLDonHang();
		model.addAttribute("isadmin",8);
		model.addAttribute("orderManagements",orderManagements);
		return "admin/admin";
	}
	
	
	@GetMapping("/deleteordermanagement")
	public String getDeleteOrderMAnagement(Model model, HttpServletRequest request) {
		String id = request.getParameter("id");
		System.out.println(id);
		jdbc.deleteOrderManagement(Integer.valueOf(id));
		return "redirect:/ordermanagement";	
	}
	
	@GetMapping("/deleteordermanagement1")
	public String getDeleteOrderMAnagement1(Model model, HttpServletRequest request) {
		String id = request.getParameter("id");
		System.out.println(id);
		jdbc.deleteOrderManagement(Integer.valueOf(id));
		return "redirect:/information";	
	}
}
