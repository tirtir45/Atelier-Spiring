package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.web.dto.ClientDTO;
import tn.esprit.autoloc.web.mapper.ClientMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientDTO creer(ClientDTO dto) {
        Client client = clientMapper.toEntity(dto);
        client.setIdClient(null);
        Client saved = clientRepository.save(client);
        return clientMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable, id=" + id));
        return clientMapper.toDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    public ClientDTO update(Long id, ClientDTO dto) {
        Client existant = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable, id=" + id));
        existant.setNom(dto.getNom());
        existant.setPrenom(dto.getPrenom());
        existant.setEmail(dto.getEmail());
        existant.setTelephone(dto.getTelephone());
        existant.setNumPermis(dto.getNumPermis());
        existant.setDateInscription(dto.getDateInscription());
        Client saved = clientRepository.save(existant);
        return clientMapper.toDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client introuvable, id=" + id);
        }
        clientRepository.deleteById(id);
    }
}
