package com.gpets.gpetsapi.domain.model;

public class Owner {
    private final String uid;   // Firebase uid
    private String fullName;
    private final String email;
    private String phone;
    private String address;
    private final long createdAt;
    private long updatedAt;

    public Owner(String uid, String email, long createdAt) {
        if (uid == null || uid.isBlank()) throw new IllegalArgumentException("uid_required");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email_required");
        if (createdAt <= 0) throw new IllegalArgumentException("createdAt_required");

        this.uid = uid;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    public void completeProfile(String fullName, String phone, String address, long now) {
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("fullName_required");
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("phone_required");
        if (address == null || address.isBlank()) throw new IllegalArgumentException("address_required");
        touch(now);
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
    }

    private void touch(long now) {
        if (now <= 0) throw new IllegalArgumentException("now_required");
        this.updatedAt = now;
    }

    // Getters
    public String uid() { return uid; }
    public String fullName() { return fullName; }
    public String email() { return email; }
    public String phone() { return phone; }
    public String address() { return address; }
    public long createdAt() { return createdAt; }
    public long updatedAt() { return updatedAt; }
}
