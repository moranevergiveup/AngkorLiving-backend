//package com.mora.angkorleving_backend.Repository;
//
//import com.mora.angkorleving_backend.model.Payment;
//import com.mora.angkorleving_backend.model.Rental;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//public interface PaymentRepository extends JpaRepository<Payment, Long> {
//    Payment findByQrMd5(String md5);
//    // Find all payments by rental
////    List<Payment> findByRental(Rental rental);
////
////    // Find latest payment for a rental (order by dueDate descending)
////    Optional<Payment> findTopByRentalOrderByDueDateDesc(Rental rental);
////
////    // Find all pending payments
////    List<Payment> findByStatus(String status);
////
////    // Find overdue payments (dueDate < today and status = PENDING)
////    List<Payment> findByDueDateBeforeAndStatus(LocalDate date, String status);
//    // ទាញយក payment records ទាំងអស់សម្រាប់ rental មួយ
//    List<Payment> findByRental(Rental rental);
//
//    // ទាញយក payment ចុងក្រោយ (latest) តាម dueDate
//    Optional<Payment> findTopByRentalOrderByDueDateDesc(Rental rental);
//
//    // ទាញយក payments ដែល overdue (dueDate < today និង status = PENDING)
//    List<Payment> findByDueDateBeforeAndStatus(LocalDate date, String status);
//
//}
//
package com.mora.angkorleving_backend.Repository;

import com.mora.angkorleving_backend.model.Payment;
import com.mora.angkorleving_backend.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByRental(Rental rental);
    List<Payment> findAllByRentalId(Long rentalId);
    Payment findByQrMd5(String md5);
    boolean existsByRentalAndDueDateBetween(
            Rental rental,
            LocalDate startDate,
            LocalDate endDate
    );
}