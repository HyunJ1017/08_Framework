package edu.kh.daemoim.popup.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.daemoim.popup.dto.Popup;

public interface PopupService {

	// 팝업 불러오기
	Popup getPopup();

	// 팝업 저장하기
	int insertPopup(Popup popup, MultipartFile popupImage);

	// 팝업 리스트 불러오기
	List<Popup> getPopupList();

	// 팝업 불러오기
	Popup selectPopup(int popupNo);

	// 팝업 삭제하기
	List<Popup> deletePopup(int popupNo);

}
