package com.kgu.life_watch.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kgu.life_watch.domain.user.entity.ElderlyProfile;

public interface ElderlyProfileRepository extends JpaRepository<ElderlyProfile, Long> {}
