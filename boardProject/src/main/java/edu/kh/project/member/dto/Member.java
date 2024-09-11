package edu.kh.project.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Lombok : Java 개발시 자주 사용하는 구문을 컴파일 시 자동완성하는 라이브러리
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Member {
  private int 		memberNo;
  private String 	memberEmail;
  private String 	memberPw;
  private String 	memberNickname;
  private String 	memberTel;
  private String 	memberAddress;
  private String 	profileImg;
  private String 	enrollDate;
  private String 	memberDelFl;
  private int 		authority; 
}
