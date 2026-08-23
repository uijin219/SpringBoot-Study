//스프링 시큐리티가 로그인 시 사용 서비스
package com.mysite.sbb.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service

public class UserSecurityService implements UserDetailsService{
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		//loadUserByUsername 메서드에 의해 리턴된 User객체의 비밀번호가 입력받은 번호와 일치하는지 검사하는 기능이 있음
		Optional<SiteUser> _siteUser = this.userRepository.findByUsername(username);
		if(_siteUser.isEmpty()) { //사용자 명으로 SiteUser 객체를 조회햇는데 데이터가 없으면
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		SiteUser siteUser = _siteUser.get();
		List<GrantedAuthority> authorities = new ArrayList<>();
		if("admin".equals(username)) { //객체가 어드민인 경우 ADMIN권한 부여
			authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		}else { //이외에는 USER권한 부여
			authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
		//User객체 생성해 반환, 이는 스프링 시큐리티에서 사용하고 User생성자에는 사용자명, 비밀번호, 권한 리스트 전달
	}
	

}
