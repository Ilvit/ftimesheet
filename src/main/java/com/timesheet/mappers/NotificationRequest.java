package com.timesheet.mappers;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class NotificationRequest {
	private String msgObject;
	private String msgBody;
}
