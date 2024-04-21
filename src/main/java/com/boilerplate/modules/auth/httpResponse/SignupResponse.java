package com.boilerplate.modules.auth.httpResponse;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponse {
    private boolean success;
    private boolean emailSent;
}
