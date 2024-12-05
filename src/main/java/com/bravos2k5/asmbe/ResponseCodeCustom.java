package com.bravos2k5.asmbe;

import jakarta.servlet.http.HttpServletResponse;

public interface ResponseCodeCustom extends HttpServletResponse {

    int SC_INVALID_TOKEN = 498;
    int SC_TOKEN_IS_EXPIRED = 499;

}
