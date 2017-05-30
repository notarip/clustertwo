package ar.com.notarip.teocom.graphs.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "country")
public class Country {

	@Id
	private Long id;
	private String name;
	private String iso;
	private String region;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIso() {
		return iso;
	}

	public void setIso(String iso) {
		this.iso = iso;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public boolean equals(Country obj) {
		return this.id.equals(obj.getId());

	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	@Override
	public String toString() {
		return id.toString() + " " + name;
	}
}
