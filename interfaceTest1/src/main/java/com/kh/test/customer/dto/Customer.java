package com.kh.test.customer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Customer {
  private int customerNo;
  private String customerName;
  private String customerTel;
  private String customerAddress;
}
