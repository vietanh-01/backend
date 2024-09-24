package com.web.repository;

import com.web.entity.Favorite;
import com.web.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("select f from Favorite f where f.user.id = ?1 and f.realEstate.id = ?2")
    public Optional<Favorite> findByUserAndBds(Long userId, Long bdsId);

    @Query("select f from Favorite f where f.user.id = ?1 and f.realEstate.status = ?2")
    public List<Favorite> findByUser(Long userId, Status status);
}
