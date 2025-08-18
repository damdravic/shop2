package ro.pyc22.shop.model.modelDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserDTO {


    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;
    private String title;

    private String bio;
    private String imageUrl;

    private boolean enabled;
    private boolean isNotLocked;
    private boolean isUsingMfa;
    private LocalDate createdAt;
    List<GrantedAuthority> authorities;


}
