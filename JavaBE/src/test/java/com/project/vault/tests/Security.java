package com.project.vault.tests;

public class Security {
//
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails admin = User.withDefaultPasswordEncoder().username("mimmo").password("vault").roles("USER").build();
//		UserDetails user1 = User.withDefaultPasswordEncoder().username("user").password("vault").roles("USER").build();
//		return new InMemoryUserDetailsManager(admin, user1);
//	}
//}
//private String executeWithAuthorization(HttpServletRequest req, Long userId, Callable<String> code) {
//	JSONObject response = new JSONObject();
//	if (authServ.existById(userId)) {
//		if (!authServ.getTokenAndValidateRequestByUsername(authServ.getUsernameById(userId), req)) {
//			response.put("result", "bad token credentials");
//			return response.toString();
//		}
//		try {
//			return code.call();
//		} catch (Exception e) {
//			e.printStackTrace();
//			response.put("result", "generic unchecked error");
//			return response.toString();
//
//		}
//	} else {
//		response.put("result", "user id not found");
//		return response.toString();
//	}
//}

}