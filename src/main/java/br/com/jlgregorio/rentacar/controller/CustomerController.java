package br.com.jlgregorio.rentacar.controller;

import br.com.jlgregorio.rentacar.dto.CustomerDTO;
import br.com.jlgregorio.rentacar.model.CustomerModel;
import br.com.jlgregorio.rentacar.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "This endpoint manages Customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @PostMapping
    @Operation(summary = "Persists a new Customer in database", tags = {"Customers"}, responses = {
            @ApiResponse(description = "Success!", responseCode = "200", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class))
            })
    })
    public CustomerDTO create(@RequestBody CustomerDTO dto){
        return service.create(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a Customer using the ID", tags = {"Customers"}, responses = {
            @ApiResponse(description = "Success!", responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema =
                        @Schema(implementation = CustomerDTO.class)
                    )
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = {@Content})
    })
    public CustomerDTO findById(@PathVariable("id") int id){
        CustomerDTO dto = service.findById(id);
        //..adding HATEOAS link
        buildEntityLink(dto);
        return dto;
    }

    @GetMapping
    public ResponseEntity<PagedModel<CustomerDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler<CustomerDTO> assembler
    ){
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));

        Page<CustomerDTO> customers = service.findAll(pageable);

        for (CustomerDTO customer:customers){
            buildEntityLink(customer);
        }
        return new ResponseEntity(assembler.toModel(customers), HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<PagedModel<CustomerDTO>> findByName(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            PagedResourcesAssembler<CustomerDTO> assembler
    ){
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));

        Page<CustomerDTO> customers = service.findByName(name, pageable);

        for (CustomerDTO customer:customers){
            buildEntityLink(customer);
        }
        return new ResponseEntity(assembler.toModel(customers), HttpStatus.OK);
    }

    @PutMapping
    public CustomerDTO update(@RequestBody CustomerDTO dto){
        return service.update(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        CustomerDTO dto = service.findById(id);
        service.delete(dto);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    public void buildEntityLink(CustomerDTO customer){
        //..self link
        customer.add(
                WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(
                                this.getClass()
                        ).findById(customer.getId())
                ).withSelfRel()
        );
    }

//    public void buildCollectionLink(CollectionModel<CustomerDTO> customers){
//        customers.add(
//                WebMvcLinkBuilder.linkTo(
//                        WebMvcLinkBuilder.methodOn(this.getClass()).findAll()
//                ).withRel(IanaLinkRelations.COLLECTION)
//        );
//    }




}
