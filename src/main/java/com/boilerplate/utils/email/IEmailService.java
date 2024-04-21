package com.boilerplate.utils.email;

import java.util.Map;

public interface IEmailService {

    void sendTestEmail(String email, Map<String, String> model);
}
