package br.com.jlgregorio.rentacar.repository;

import br.com.jlgregorio.rentacar.model.CustomerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, Integer> {

    public Page<CustomerModel> findAll(Pageable pageable);

    public Page<CustomerModel> findByFirstNameStartsWithIgnoreCase(String name, Pageable pageable);

}
