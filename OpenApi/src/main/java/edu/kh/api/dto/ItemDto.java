package edu.kh.api.dto;

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
public class ItemDto {
	private String dataTime;
  private String pm10Value;
  private int pm10Grade;
  private String pm25Value;
  private int pm25Grade;
  private String sidoName;

  private int coGrade;
  private String coValue;
  private String coFlag;
  private int khaiGrade;
  private String khaiValue;
  private int no2Grade;
  private double no2Value;
  private String no2Flag;
  private int o3Grade;
  private String o3Value;
  private String o3Flag;
  private int so2Grade;
  private String so2Value;
  private String so2Flag;
  private String stationName;
  
  private String pm10Flag;
  private String pm25Flag;
}
