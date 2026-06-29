package hotel;

import java.io.Serializable;

public class Guest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String email;
    private final String phone;
    private final String guestId;

    public Guest(String guestId, String name, String email, String phone) {
        this.guestId = guestId;
        this.name    = name;
        this.email   = email;
        this.phone   = phone;
    }

    public String getGuestId() { return guestId; }
    public String getName()    { return name; }
    public String getEmail()   { return email; }
    public String getPhone()   { return phone; }

    public String toSerialString() {
        return guestId + "|" + name + "|" + email + "|" + phone;
    }

    public static Guest fromSerialString(String s) {
        String[] p = s.split("\\|");
        return new Guest(p[0], p[1], p[2], p[3]);
    }

    @Override
    public String toString() {
        return String.format("Guest[%s] %s | %s | %s", guestId, name, email, phone);
    }
}
