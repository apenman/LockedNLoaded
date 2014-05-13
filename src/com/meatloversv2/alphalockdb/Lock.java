package com.meatloversv2.alphalockdb;

public class Lock {
	private long m_id;
	private String m_code;
	private String m_package;
	private String m_name;
	
	public Lock(Long id, String code, String name, String pack) {
		m_id = id;
		m_code = code;
		m_package = pack;
		m_name = name;
	}
	
	public Lock(String code, String name, String pack) {
		m_code = code;
		m_package = pack;
		m_name = name;
	}
	
	public long getId() {
		return m_id;
	}
	
	public String getCode() {
		return m_code;
	}
	
	public String getPackage() {
		return m_package;
	}
	
	public String getName() {
		return m_name;
	}
	
	public void setCode(String code) {
		this.m_code = code;
	}
	
	public void setPackage(String pack) {
		this.m_package = pack;
	}
	
	public void setName(String name) {
		this.m_name = name;
	}
}
