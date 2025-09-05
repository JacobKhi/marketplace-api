package br.com.iff.marketplace.address.repository;

import br.com.iff.marketplace.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Page<Address> findByUserId(Long userId, Pageable pageable);

}