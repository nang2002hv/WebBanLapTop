package com.example.NguyenTracNang.Controller;

public class Product {
	private int id,don_vi_ban,don_vi_kho;
	private long ma_danh_muc,ma_hang_sx;
	private String don_gia,cpu,dung_luong_pin,he_dieu_hanh,man_hinh,ram,ten_san_pham,thiet_ke,bao_hanh,thong_tin_chung;
	private int quantity;
	
	public Product(int id, int don_vi_ban, int don_vi_kho, String don_gia, long ma_danh_muc, long ma_hang_sx, String cpu,
			String dung_luong_pin, String he_dieu_hanh, String man_hinh, String ram, String ten_san_pham,
			String thiet_ke, String bao_hanh, String thong_tin_chung, int quantity) {
		this.id = id;
		this.don_vi_ban = don_vi_ban;
		this.don_vi_kho = don_vi_kho;
		this.don_gia = don_gia;
		this.ma_danh_muc = ma_danh_muc;
		this.ma_hang_sx = ma_hang_sx;
		this.cpu = cpu;
		this.dung_luong_pin = dung_luong_pin;
		this.he_dieu_hanh = he_dieu_hanh;
		this.man_hinh = man_hinh;
		this.ram = ram;
		this.ten_san_pham = ten_san_pham;
		this.thiet_ke = thiet_ke;
		this.bao_hanh = bao_hanh;
		this.thong_tin_chung = thong_tin_chung;
		this.quantity = quantity;
	}

	
	
	public int getQuantity() {
		return quantity;
	}



	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



	public Product() {
		
	}

	public int getDon_vi_ban() {
		return don_vi_ban;
	}

	public void setDon_vi_ban(int don_vi_ban) {
		this.don_vi_ban = don_vi_ban;
	}

	public int getDon_vi_kho() {
		return don_vi_kho;
	}

	public void setDon_vi_kho(int don_vi_kho) {
		this.don_vi_kho = don_vi_kho;
	}

	public String getDon_gia() {
		return don_gia;
	}

	public void setDon_gia(String don_gia) {
		this.don_gia = don_gia;
	}

	public long getMa_danh_muc() {
		return ma_danh_muc;
	}

	public void setMa_danh_muc(long ma_danh_muc) {
		this.ma_danh_muc = ma_danh_muc;
	}

	public long getMa_hang_sx() {
		return ma_hang_sx;
	}

	public void setMa_hang_sx(long ma_hang_sx) {
		this.ma_hang_sx = ma_hang_sx;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getDung_luong_pin() {
		return dung_luong_pin;
	}

	public void setDung_luong_pin(String dung_luong_pin) {
		this.dung_luong_pin = dung_luong_pin;
	}

	public String getHe_dieu_hanh() {
		return he_dieu_hanh;
	}

	public void setHe_dieu_hanh(String he_dieu_hanh) {
		this.he_dieu_hanh = he_dieu_hanh;
	}

	public String getMan_hinh() {
		return man_hinh;
	}

	public void setMan_hinh(String man_hinh) {
		this.man_hinh = man_hinh;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getTen_san_pham() {
		return ten_san_pham;
	}

	public void setTen_san_pham(String ten_san_pham) {
		this.ten_san_pham = ten_san_pham;
	}

	public String getThiet_ke() {
		return thiet_ke;
	}

	public void setThiet_ke(String thiet_ke) {
		this.thiet_ke = thiet_ke;
	}



	public String getBao_hanh() {
		return bao_hanh;
	}

	public void setBao_hanh(String bao_hanh) {
		this.bao_hanh = bao_hanh;
	}

	public String getThong_tin_chung() {
		return thong_tin_chung;
	}

	public void setThong_tin_chung(String thong_tin_chung) {
		this.thong_tin_chung = thong_tin_chung;
	}

	public int getId() {
		return id;
	}
	
	
	
	
	
}
