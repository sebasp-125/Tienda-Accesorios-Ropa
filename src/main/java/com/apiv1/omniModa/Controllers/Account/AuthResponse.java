package com.apiv1.omniModa.Controllers.Account;

public class AuthResponse {
    private boolean success;
    private String message;
    private String redirectUrl;
    private String userName;
    private String role;

    public AuthResponse() {}

    public AuthResponse(boolean success, String message, String redirectUrl, String userName, String role) {
        this.success = success;
        this.message = message;
        this.redirectUrl = redirectUrl;
        this.userName = userName;
        this.role = role;
    }

    public static AuthResponse ok(String message, String redirectUrl, String userName, String role) {
        return new AuthResponse(true, message, redirectUrl, userName, role);
    }

    public static AuthResponse error(String message) {
        return new AuthResponse(false, message, null, null, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
