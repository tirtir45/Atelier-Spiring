package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.MaintenanceDTO;

import java.util.List;

public interface IMaintenanceService {
    MaintenanceDTO creer(MaintenanceDTO dto);
    MaintenanceDTO findById(Long id);
    List<MaintenanceDTO> findAll();
    MaintenanceDTO update(Long id, MaintenanceDTO dto);
    void deleteById(Long id);
}
