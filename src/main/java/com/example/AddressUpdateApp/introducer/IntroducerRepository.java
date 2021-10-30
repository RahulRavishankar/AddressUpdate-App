package com.example.AddressUpdateApp.introducer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IntroducerRepository extends JpaRepository<Introducer, String> {

    @Query("SELECT s from Introducer s WHERE s.uid = ?1")
    Optional<Introducer> findIntroducerByUid(String uid);

    @Query(value = "SELECT s from Introducer s WHERE s.uid = ?1 and s.requester_uid = ?2", nativeQuery = true)
    Optional<Introducer> findIntroducerByUidAndRequesterUid(String uid, String requesterUid);
}
