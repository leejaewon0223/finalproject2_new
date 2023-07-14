package com.javalab.board.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javalab.board.dto.MemberDTO;
import com.javalab.board.dto.OrderDTO;
import com.javalab.board.dto.OrderProductDTO;
import com.javalab.board.entity.Member;
import com.javalab.board.entity.Order;
import com.javalab.board.repository.MemberRepository;
import com.javalab.board.repository.OrderRepository;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private OrderRepository orderRepository;
	
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void register(Member member) {
        // 회원 등록 로직 구현
        memberRepository.save(member);
    }

    @Override
    public boolean authenticate(String memberId, String memberPassword) {
        // 회원 인증 로직 구현
        Member member = memberRepository.findByMemberId(memberId);
        return member != null && member.getMemberPassword().equals(memberPassword);
    }
    
    @Override
    public Member getMemberById(String memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Override
    public void updateMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void deleteMember(String memberId) {
        memberRepository.deleteById(memberId);
    }

    
    
    //dto로 변환
    public static MemberDTO toDTO(Member member) {
        MemberDTO dto = new MemberDTO();
        dto.setMemberId(member.getMemberId());
        dto.setMemberPassword(member.getMemberPassword());
        dto.setMemberName(member.getMemberName());
        dto.setMemberPhone(member.getMemberPhone());
        dto.setMemberEmail(member.getMemberEmail());
        dto.setMemberAddress(member.getMemberAddress());
        dto.setAdmin(member.getAdmin());
        dto.setBaskets(member.getBaskets());
        dto.setOrders(member.getOrders());
        dto.setBoards(member.getBoards());
        return dto;
    }
    
    //엔티티로 변환
    public static Member toEntity(MemberDTO dto) {
        Member member = new Member();
        member.setMemberId(dto.getMemberId());
        member.setMemberPassword(dto.getMemberPassword());
        member.setMemberName(dto.getMemberName());
        member.setMemberPhone(dto.getMemberPhone());
        member.setMemberEmail(dto.getMemberEmail());
        member.setMemberAddress(dto.getMemberAddress());
        member.setAdmin(dto.getAdmin());
        member.setBaskets(dto.getBaskets());
        member.setOrders(dto.getOrders());
        member.setBoards(dto.getBoards());
        return member;
    }

	@Override
	public MemberDTO getMemberOrders(String memberId) {
		List<Order> orders = orderRepository.findOrderByMemberMemberId(memberId);
		MemberDTO memberDTO = new MemberDTO();
		
		
	    List<OrderDTO> orderDTOs = orders.stream()
	            .map(order -> {
	                OrderDTO orderDTO = new OrderDTO();
	                orderDTO.setOrderNo(order.getOrderNo());
	                orderDTO.setOrderDate(order.getOrderDate());
	                orderDTO.setOrderPaymentAmount(order.getOrderPaymentAmount());

	                List<OrderProductDTO> orderProductDTOs = order.getOrderProducts().stream()
	                        .map(this::convertToOrderProductDTO)
	                        .collect(Collectors.toList());

	                orderDTO.setOrderProducts(orderProductDTOs);
	                return orderDTO;
	            })
	            .collect(Collectors.toList());
		
		memberDTO.setOrderDTOs(orderDTOs);
		return memberDTO;
	}

}