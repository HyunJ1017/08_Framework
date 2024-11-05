package edu.kh.daemoim.popup.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Popup {
	
	private int popupNo;
	private String popupName;
	private String popupLocation;
	private String popupRename;
	private int popupLeft;
	private int popupUp;
	private char popupDelFl;

}
