package com.cleanBoard.model.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Long id;
	private String userName;
	private String nickName;
	private String password;
	private LocalDateTime modDate,regDate;
}
