package com.javalab.board.service;

import com.javalab.board.dto.MemberDTO;
import com.javalab.board.dto.OrderProductDTO;
import com.javalab.board.entity.Member;
import com.javalab.board.entity.OrderProduct;

public interface MemberService {
    void register(Member member);
    boolean authenticate(String memberId, String memberPassword);
    // 다른 회원 관련 기능에 대한 메소드 선언
	
    Member getMemberById(String memberId);
    void updateMember(Member member);
    void deleteMember(String memberId);
    
    public MemberDTO getMemberOrders(String memberId);
    
    default OrderProductDTO convertToOrderProductDTO(OrderProduct orderProduct) {
        OrderProductDTO orderProductDTO = new OrderProductDTO();
        orderProductDTO.setOrderProductAmount(orderProduct.getOrderProductAmount());
        orderProductDTO.setOrderQty(orderProduct.getOrderQty());
//        orderProductDTO.setProductDTO(orderProduct.getProduct());

        return orderProductDTO;
    }
}
