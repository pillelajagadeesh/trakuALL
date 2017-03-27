package com.tresbu.trakeye.security.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.tresbu.trakeye.domain.Tenant;

public class TrakEyeUser extends org.springframework.security.core.userdetails.User {
	
	public TrakEyeUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		
	}

	public TrakEyeUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		// TODO Auto-generated constructor stub
	}
	
	public TrakEyeUser(String username, String password, Collection<? extends GrantedAuthority> authorities,Tenant tenant) {
		super(username, password, authorities);
		this.tenant = tenant;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6975666437558077513L;
	private Tenant tenant;

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	
}
