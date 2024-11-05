package edu.kh.daemoim.main.dto;

import java.time.LocalDateTime;
import java.util.List;

import edu.kh.daemoim.myPage.dto.MyPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MainDTO {
	private String groupMainImg;
    private String groupName;
    private String groupIntroduce;
    private String categoryName;
    private int currentPopulation; // 현재 해당 모임의 인원
    private int maxPopulation;
    private int groupNo;
    
    private LocalDateTime latestChatDate; // 마지막 채팅 조회 
    
    private List<MyPage> members;
}
