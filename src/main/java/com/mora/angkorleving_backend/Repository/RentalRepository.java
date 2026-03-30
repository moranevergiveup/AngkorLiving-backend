package com.mora.angkorleving_backend.Repository;

//package com.mora.backendangkorliving.repository;

import com.mora.angkorleving_backend.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByTenant_Id(Long tenantId);
    List<Rental> findByRoom_Id(Long roomId);

}

