package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.service.IEquipementService;
import tn.esprit.autoloc.web.dto.EquipementDTO;
import tn.esprit.autoloc.web.mapper.EquipementMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;
    private final EquipementMapper equipementMapper;

    @Override
    public EquipementDTO creer(EquipementDTO dto) {
        Equipement equipement = equipementMapper.toEntity(dto);
        equipement.setIdEquipement(null);
        Equipement saved = equipementRepository.save(equipement);
        return equipementMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EquipementDTO findById(Long id) {
        Equipement equipement = equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement introuvable, id=" + id));
        return equipementMapper.toDto(equipement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipementDTO> findAll() {
        return equipementRepository.findAll().stream()
                .map(equipementMapper::toDto)
                .toList();
    }

    @Override
    public EquipementDTO update(Long id, EquipementDTO dto) {
        Equipement existant = equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement introuvable, id=" + id));
        existant.setLibelle(dto.getLibelle());
        Equipement saved = equipementRepository.save(existant);
        return equipementMapper.toDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!equipementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Équipement introuvable, id=" + id);
        }
        equipementRepository.deleteById(id);
    }
}
