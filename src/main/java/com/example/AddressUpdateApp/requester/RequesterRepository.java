package com.example.AddressUpdateApp.requester;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequesterRepository extends JpaRepository<Requester, String> {

    @Query("SELECT s from Requester s WHERE s.uid = ?1")
    Optional<Requester> findRequesterByUid(String uid);
}
