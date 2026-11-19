package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;
import tn.esprit.autoloc.web.mapper.ContratMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final ContratMapper contratMapper;

    @Override
    public ContratDTO creer(ContratDTO dto) {
        Contrat contrat = contratMapper.toEntity(dto);
        contrat.setIdContrat(null);
        Contrat saved = contratRepository.save(contrat);
        return contratMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ContratDTO findById(Long id) {
        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat introuvable, id=" + id));
        return contratMapper.toDto(contrat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratDTO> findAll() {
        return contratRepository.findAll().stream()
                .map(contratMapper::toDto)
                .toList();
    }

    @Override
    public ContratDTO update(Long id, ContratDTO dto) {
        Contrat existant = contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat introuvable, id=" + id));
        existant.setDateSignature(dto.getDateSignature());
        existant.setMontantTotal(dto.getMontantTotal());
        if (dto.getValide() != null) {
            existant.setValide(dto.getValide());
        }
        Contrat saved = contratRepository.save(existant);
        return contratMapper.toDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat introuvable, id=" + id);
        }
        contratRepository.deleteById(id);
    }
}
