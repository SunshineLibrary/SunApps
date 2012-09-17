package com.ssl.support.data.models;

public class Package {
	
	public String name;
	public int version;
	public int id;

    public Package() {}

	public String getName() {
		return name;
	}

	public int getVersion() {
		return version;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + version;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		Package other = (Package) obj;
        if (version != other.version)
            return false;
        if (id != other.id)
            return false;
        if (name == null)
            return other.name == null;
        return name.equals(other.name);
	}

    @Override
    public String toString() {
        return name + "_" + version;
    }
}
