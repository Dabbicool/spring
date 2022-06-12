package com.example.storeeverything;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareRepository extends JpaRepository<Share, Long> {
    @Query(value = "SELECT * from share s where 1 ORDER BY s.share_date DESC", nativeQuery = true)
    public List<Share> getLastShares(long num);

    public List<Share> findByUser(User user);
}
