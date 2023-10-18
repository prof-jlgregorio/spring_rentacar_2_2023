package br.com.jlgregorio.rentacar.service;

import br.com.jlgregorio.rentacar.dto.VehicleDTO;
import br.com.jlgregorio.rentacar.exception.ResourceNotFoundException;
import br.com.jlgregorio.rentacar.mapper.CustomModelMapper;
import br.com.jlgregorio.rentacar.model.VehicleModel;
import br.com.jlgregorio.rentacar.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository repository;

    public VehicleDTO create(VehicleDTO dto){
        VehicleModel model = CustomModelMapper.parseObject(dto, VehicleModel.class);
        return CustomModelMapper.parseObject(repository.save(model), VehicleDTO.class);
    }

    public VehicleDTO findById(int id){
        VehicleModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Não Encontrado!"));
        return CustomModelMapper.parseObject(model, VehicleDTO.class);
    }

    public List<VehicleDTO> findAll(){
        List<VehicleModel> list = repository.findAll();
        return CustomModelMapper.parseObjectList(list, VehicleDTO.class);
    }

    public VehicleDTO update(VehicleDTO dto){
        VehicleModel model = repository.findById(dto.getId()).orElseThrow(() -> new ResourceNotFoundException("Não encontrado!"));
        model = CustomModelMapper.parseObject(dto, VehicleModel.class);
        return CustomModelMapper.parseObject(repository.save(model), VehicleDTO.class);
    }

    public void delete(int id){
        VehicleModel model = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Não Encontrado!"));
        repository.delete(model);
    }

}
