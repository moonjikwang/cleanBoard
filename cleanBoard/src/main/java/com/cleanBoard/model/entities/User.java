package com.cleanBoard.model.entities;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String nickName;
    @Builder.Default
    private String role = "user";
    private String password;

    
    /** 
     * 회원정보 수정
     * @param nickname 아이디
     * @param password 비밀번호
     */
    public void modify(String nickname, String password) {
        this.nickName = nickname;
        this.password = password;
    }
}
