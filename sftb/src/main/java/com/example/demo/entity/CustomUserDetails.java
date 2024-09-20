package com.example.demo.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	
    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //사용자가 가진 권한(역할)을 반환합니다.
        return null; // 권한 설정이 필요하다면 여기에 추가
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserID();
    }

    @Override
    public boolean isAccountNonExpired() {//계정이 만료되지 않았는지 여부를 반환합니다.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {//계정이 잠기지 않았는지 여부를 반환합니다.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { //비밀번호가 만료되지 않았는지 여부를 반환합니다.
        return true;
    }

    @Override
    public boolean isEnabled() { //계정이 활성화되었는지 여부를 반환합니다.
        return true;
    }
}
