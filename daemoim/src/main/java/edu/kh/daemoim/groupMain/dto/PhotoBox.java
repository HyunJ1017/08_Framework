package edu.kh.daemoim.groupMain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PhotoBox {

	private int boardImgNo;
	private String boardImgPath;
	private String boardImgOriginalName;
	private String boardImgRename;
	private int boardImgOrder;
	private int boardNo;
	
	private int groupNo;
	private String groupHeaderImg;
	
	
	private String fileLocation;
}
