package model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;

    @ToString.Exclude
    private String password;
    private String name;
    private String email;

    public boolean login(String password) {
        return this.password.equals(password);
    }
}
