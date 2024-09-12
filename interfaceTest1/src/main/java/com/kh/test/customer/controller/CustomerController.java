package com.kh.test.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.test.customer.dto.Customer;
import com.kh.test.customer.service.CustomerService;

@RequestMapping("customer")
@Controller
public class CustomerController {

  @Autowired
  private CustomerService service;

  @PostMapping("insertCustomer") 
  public String insertCustomer(Customer customer, Model model) {
    int result = service.insertCustomer(customer);

    
    if (result > 0)
      model.addAttribute("message", "추가 성공!!!");
    else
      model.addAttribute("message", "추가 실패...");
    model.addAttribute("customerName", customer.getCustomerName());
    return "result"; 
  }
}

//1. index.html에서 "/customer/insertCustomer" 요청을 서버로 보내는데
//컨트롤러 CustomerController에서 요청받을 주소가
//매서드의 @PostMapping("insertCustomer") 밖에 존재하지 않는데
//클래스 상단에 @requestMapping("customer") 어노테이션이 누락되어있음
//
//2. CustomerServiceImpl 에서 Mapper 인터페이스를 사용하기 위해
//mapper 변수명을 사용하려고 정의하는데
//Mapper는 인터페이스여서 바로 상속받아 이용할 수 없고
//@Mapper 어노테이션이 만들어준 상속받은 Mapper 클래스를
//Bean에서 찾아줄 @Autowired 어노테이션이 누락되어 있음
//
//3. @Mapper 어노테이션은 Mapper 클래스의 매서드 이름과 동일한
//이름을 가진 xml파일 내의 sql구문과 연결해 주는데
//Mapper클래스의 메서드 값과, 사용하려는 customer-mapper.xml의 id값이 서로 달라
//서로 연결하지 못하고 있음
//
//4. customer-mapper.xml에서 parameterType을 Customer 클래스를 이용해
//데이터를 전달받아 DB에 전달해 주려고 하는데
//#{} 안에 작성되어야 할 필드값이 Customer 클래스의 필드값과 달라
//Customer 객체의 데이터를 읽어 오지 못함
//
//5. 메서드 실행 후 결과값을담은 응답을 result.html로 포워드 했는데
//result.html 에서 출력할 데이터는 message와 customerName 두가지인데
//Model Attribute에 저장해둔 값은 message밖에 없어
//customerName 을 표시하지 못하는 문제가 발생함
//
//=================================
//
//1. 클래스 상단에 @requestMapping("customer") 어노테이션 추가
//
//  @Controller
//  public class CustomerController {...}
//
//  =>
//
//  @RequestMapping("customer")
//  @Controller
//  public class CustomerController {...}
//
//
//
//2. private CustomerMapper mapper; 상단에 @Autowired 어노테이션 추가
//
//  @Service
//  public class CustomerServiceImpl implements CustomerService {
//  
//    private CustomerMapper mapper;
//  ...}
//
//=>
//
//  @Service
//  public class CustomerServiceImpl implements CustomerService {
//    @Autowired
//    private CustomerMapper mapper;
//  ...}
//
//
//
//3. customer-mapper.xml의 id값을 "insertCustomer"로 변경
//
//  <insert id="insert" ...> ... </insert>
//
//  =>
//
//  <insert id="insertCustomer" ...> ... </insert>
//
//
//4. #{} 안에 작성된 필드명 수정
//
//  <insert ...>
//    ...
//    VALUES(SEQ_CUSTOMER_NO.NEXTVAL, #{name}, #{tel}, #{address})
//  </insert>
//
//  =>
//
//  <insert id="insertCustomer" parameterType="Customer">
//    INSERT INTO CUSTOMER
//    VALUES(SEQ_CUSTOMER_NO.NEXTVAL, #{customerName}, #{customerTel}, #{customerAddress})
//  </insert>
//
//
//
//5. Model Attribute 를 이용해 customerName 에 출력될 데이터 저장
//
//  public String insertCustomer()
//  메서드 아무곳에나
//
//  model.addAttribute("customerName", customer.getCustomerName());
//  구문 추가