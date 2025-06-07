package com.kalavastra.api.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
	private String email;
	private String password;
}
