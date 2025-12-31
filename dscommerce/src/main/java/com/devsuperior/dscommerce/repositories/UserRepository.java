package com.devsuperior.dscommerce.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devsuperior.dscommerce.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	@Query(nativeQuery = true, value = """
			SELECT u.email AS username, u.password, r.id AS roleId, r.authority
			FROM tb_user u
			INNER JOIN tb_user_role ur ON u.id = ur.user_id
			INNER JOIN tb_role r ON r.id = ur.role_id
			WHERE u.email = :email
			""")
	List<UserDetailsProjection> searchUserAndRolesByEmail(@Param("email") String email);
}
