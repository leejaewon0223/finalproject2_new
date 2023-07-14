package com.javalab.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javalab.board.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{


	List<Order> findOrderByMemberMemberId(String memberId);

}
