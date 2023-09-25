package ro.pds.PaperDisseminationSystem.view.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtDto {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String metamaskAddress;
    private String role;

    public JwtDto(String accessToken, Long id, String username, String email, String metamaskAddress, String role) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.metamaskAddress = metamaskAddress;
        this.email = email;
        this.role = role;
    }
}