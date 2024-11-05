package edu.kh.daemoim.sse.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.daemoim.sse.dto.Notification;

@Mapper
public interface SseMapper {

	int insertNotification(Notification notification);

	/* Map<String, Object> selectReceiveMember(int notificationNo); */

	List<Notification> selectNotificationList(int memberNo);

	int notReadCheck(int memberNo);

	void deleteNotification(int notificationNo);
	
	void deleteAllNotification(int memberNo);

	void updateNotification(int notificationNo);

	void updateAllNotification(int memberNo);


}
