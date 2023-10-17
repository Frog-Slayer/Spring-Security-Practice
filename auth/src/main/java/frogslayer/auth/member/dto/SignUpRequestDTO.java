package frogslayer.auth.member.dto;


import frogslayer.auth.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequestDTO {
    private String email;
    private String password;

    public static Member toEntity(SignUpRequestDTO signUpRequestDTO){
        return Member.builder()
                .email(signUpRequestDTO.getEmail())
                .password(signUpRequestDTO.getPassword())
                .build();
    }
}
