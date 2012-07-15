package com.sunshine.support.installer.service;

public class InstallRecord {

	private String name;
	private String version;
	private String apk;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getApk() {
		return apk;
	}
	public void setApk(String apk) {
		this.apk = apk;
	}
	public InstallRecord(String name, String version, String apk) {
		super();
		this.name = name;
		this.version = version;
		this.apk = apk;
	}
	
}
