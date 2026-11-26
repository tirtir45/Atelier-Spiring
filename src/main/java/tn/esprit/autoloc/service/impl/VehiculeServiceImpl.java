package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeDTO;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeServiceImpl implements IVehiculeService {

    private static final String VEHICULE_INTROUVABLE = "Véhicule introuvable, id=";
    private static final String AGENCE_INTROUVABLE = "Agence introuvable, id=";

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public VehiculeDTO creer(VehiculeDTO dto) {
        Vehicule vehicule = vehiculeMapper.toEntity(dto);
        vehicule.setIdVehicule(null);

        if (dto.getAgenceId() != null) {
            vehicule.setAgence(trouverAgence(dto.getAgenceId()));
        }

        Vehicule saved = vehiculeRepository.save(vehicule);
        return vehiculeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculeDTO findById(Long id) {
        return vehiculeMapper.toDto(trouverVehicule(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDTO> findAll() {
        return vehiculeRepository.findAll().stream()
                .map(vehiculeMapper::toDto)
                .toList();
    }

    @Override
    public VehiculeDTO update(Long id, VehiculeDTO dto) {
        Vehicule existant = trouverVehicule(id);

        existant.setImmatriculation(dto.getImmatriculation());
        existant.setMarque(dto.getMarque());
        existant.setModele(dto.getModele());
        existant.setCategorie(dto.getCategorie());
        existant.setTarifJournalier(dto.getTarifJournalier());
        existant.setStatut(dto.getStatut());

        if (dto.getAgenceId() != null) {
            existant.setAgence(trouverAgence(dto.getAgenceId()));
        } else {
            existant.setAgence(null);
        }

        Vehicule saved = vehiculeRepository.save(existant);
        return vehiculeMapper.toDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException(VEHICULE_INTROUVABLE + id);
        }
        vehiculeRepository.deleteById(id);
    }

    @Override
    public void affecterVehiculeAAgence(Long vehiculeId, Long agenceId) {
        Vehicule vehicule = trouverVehicule(vehiculeId);
        Agence agence = trouverAgence(agenceId);
        vehicule.setAgence(agence);
        vehiculeRepository.save(vehicule);
    }

    @Override
    public void desaffecterVehiculeDeAgence(Long vehiculeId) {
        Vehicule vehicule = trouverVehicule(vehiculeId);
        if (vehicule.getAgence() == null) {
            throw new IllegalStateException("Le véhicule n'est rattaché à aucune agence");
        }
        vehicule.setAgence(null);
        vehiculeRepository.save(vehicule);
    }

    private Vehicule trouverVehicule(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICULE_INTROUVABLE + id));
    }

    private Agence trouverAgence(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AGENCE_INTROUVABLE + id));
    }
}