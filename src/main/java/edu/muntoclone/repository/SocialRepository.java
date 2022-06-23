package edu.muntoclone.repository;

import edu.muntoclone.entity.Social;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialRepository extends JpaRepository<Social, Long> {

    Page<Social> findAllByCategoryId(Long id, Pageable pageable);
}
