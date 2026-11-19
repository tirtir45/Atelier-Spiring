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

    private final IEmployeRepository employeRepository;
    private final IAgenceRepository agenceRepository;
    private final EmployeMapper employeMapper;

    @Override
    public EmployeDTO creer(EmployeDTO dto) {
        Employe employe = employeMapper.toEntity(dto);
        employe.setIdEmploye(null);

        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable, id=" + dto.getAgenceId()));
            employe.setAgence(agence);
        }

        Employe saved = employeRepository.save(employe);
        return employeMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeDTO findById(Long id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable, id=" + id));
        return employeMapper.toDto(employe);
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
        Employe existant = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable, id=" + id));

        existant.setNom(dto.getNom());
        existant.setPrenom(dto.getPrenom());
        existant.setRole(dto.getRole());

        if (dto.getAgenceId() != null) {
            Agence agence = agenceRepository.findById(dto.getAgenceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable, id=" + dto.getAgenceId()));
            existant.setAgence(agence);
        } else {
            existant.setAgence(null);
        }

        Employe saved = employeRepository.save(existant);
        return employeMapper.toDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employé introuvable, id=" + id);
        }
        employeRepository.deleteById(id);
    }

    @Override
    public void affecterEmployeAAgence(Long employeId, Long agenceId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable, id=" + employeId));
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable, id=" + agenceId));
        employe.setAgence(agence);
        employeRepository.save(employe);
    }

    @Override
    public void desaffecterEmployeDeAgence(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employé introuvable, id=" + employeId));
        if (employe.getAgence() == null) {
            throw new IllegalStateException("L'employé n'est rattaché à aucune agence");
        }
        employe.setAgence(null);
        employeRepository.save(employe);
    }
}
