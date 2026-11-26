package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.web.dto.EmployeDTO;
import tn.esprit.autoloc.web.mapper.EmployeMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeServiceImpl implements IEmployeService {

    private static final String EMPLOYE_INTROUVABLE = "Employé introuvable, id=";
    private static final String AGENCE_INTROUVABLE = "Agence introuvable, id=";

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;
    private final EmployeMapper employeMapper;

    @Override
    public EmployeDTO creer(EmployeDTO dto) {
        Employe employe = employeMapper.toEntity(dto);
        employe.setIdEmploye(null);

        if (dto.getAgenceId() != null) {
            employe.setAgence(trouverAgence(dto.getAgenceId()));
        }

        Employe saved = employeRepository.save(employe);
        return employeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeDTO findById(Long id) {
        return employeMapper.toDto(trouverEmploye(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeDTO> findAll() {
        return employeRepository.findAll().stream()
                .map(employeMapper::toDto)
                .toList();
    }

    @Override
    public EmployeDTO update(Long id, EmployeDTO dto) {
        Employe existant = trouverEmploye(id);

        existant.setNom(dto.getNom());
        existant.setPrenom(dto.getPrenom());
        existant.setRole(dto.getRole());

        if (dto.getAgenceId() != null) {
            existant.setAgence(trouverAgence(dto.getAgenceId()));
        } else {
            existant.setAgence(null);
        }

        Employe saved = employeRepository.save(existant);
        return employeMapper.toDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException(EMPLOYE_INTROUVABLE + id);
        }
        employeRepository.deleteById(id);
    }

    @Override
    public void affecterEmployeAAgence(Long employeId, Long agenceId) {
        Employe employe = trouverEmploye(employeId);
        Agence agence = trouverAgence(agenceId);
        employe.setAgence(agence);
        employeRepository.save(employe);
    }

    @Override
    public void desaffecterEmployeDeAgence(Long employeId) {
        Employe employe = trouverEmploye(employeId);
        if (employe.getAgence() == null) {
            throw new IllegalStateException("L'employé n'est rattaché à aucune agence");
        }
        employe.setAgence(null);
        employeRepository.save(employe);
    }

    private Employe trouverEmploye(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(EMPLOYE_INTROUVABLE + id));
    }

    private Agence trouverAgence(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AGENCE_INTROUVABLE + id));
    }
}