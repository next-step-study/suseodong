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
}
