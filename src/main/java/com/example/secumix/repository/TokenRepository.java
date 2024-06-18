package com.example.secumix.repository;

import com.example.secumix.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

  @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and t.tokenType='BEARER'  and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByUser(Integer id);
  @Query("select o from Token o where o.token=:token and o.tokenType='BEARER' ")
  Optional<Token> findByToken(String token);
  @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and t.tokenType='RESETPASSWORD'  and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidResetPassWord(Integer id);
  @Query("select o from Token o where o.token=:token and o.tokenType='RESETPASSWORD' ")
  Optional<Token> findResetPassToken(String token);
}
