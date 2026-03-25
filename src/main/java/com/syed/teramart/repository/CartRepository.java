package com.syed.teramart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.syed.teramart.entity.CartItem;

import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer> {
	
	// Fetching cart items by User and Product
	@Query("SELECT c FROM CartItem c WHERE c.user.userId = :userId AND c.product.productId = :productId")
	Optional<CartItem> findByUserAndProduct(int userId, int productId);
	
	// Counting total cart items
	@Query("SELECT COALESCE(SUM(c.quantity), 0) FROM CartItem c WHERE c.user.userId = :userId")
	int countTotalItems(int userId);
	
	@Query("SELECT c FROM CartItem c JOIN FETCH c.product p LEFT JOIN ProductImage pi ON "
			+ "p.productId = pi.product.productId WHERE c.user.userId = :userId")
	List<CartItem> findCartItemsWithProductDetailsByUserId(int userId);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM CartItem c WHERE c.user.userId = :userId AND c.product.productId = :productId")
	void deleteCartItem(int userId, int producId);
	
	@Query("SELECT c FROM CartItem c WHERE c.user.userId = :userId")
	List<CartItem> findByUserId(int userId);

	@Modifying
	@Transactional
	@Query("DELETE FROM CartItem c WHERE c.user.userId = :userId")
	void clearCart(int userId);

}
