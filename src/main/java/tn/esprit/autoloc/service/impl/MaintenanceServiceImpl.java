package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IMaintenanceService;
import tn.esprit.autoloc.web.dto.MaintenanceDTO;
import tn.esprit.autoloc.web.mapper.MaintenanceMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final MaintenanceMapper maintenanceMapper;

    @Override
    public MaintenanceDTO creer(MaintenanceDTO dto) {
        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable, id=" + dto.getVehiculeId()));

        Maintenance maintenance = maintenanceMapper.toEntity(dto);
        maintenance.setIdMaintenance(null);
        maintenance.setVehicule(vehicule);

        Maintenance saved = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceDTO findById(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance introuvable, id=" + id));
        return maintenanceMapper.toDto(maintenance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceDTO> findAll() {
        return maintenanceRepository.findAll().stream()
                .map(maintenanceMapper::toDto)
                .toList();
    }

    @Override
    public MaintenanceDTO update(Long id, MaintenanceDTO dto) {
        Maintenance existant = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance introuvable, id=" + id));

        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable, id=" + dto.getVehiculeId()));
            existant.setVehicule(vehicule);
        }

        existant.setDateDebut(dto.getDateDebut());
        existant.setDateFin(dto.getDateFin());
        existant.setDescription(dto.getDescription());

        Maintenance saved = maintenanceRepository.save(existant);
        return maintenanceMapper.toDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance introuvable, id=" + id);
        }
        maintenanceRepository.deleteById(id);
    }
}
