package ru.realtorcrm.core_crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.realtorcrm.core_crm.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select exists (select u from User u where u.nickname = :nickname)")
    boolean isNicknameExists(String nickname);

    @Query("select exists (select u from User u where u.email = :email)")
    boolean isEmailExists(String email);
}
