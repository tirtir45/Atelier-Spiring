package tn.esprit.autoloc.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public ReservationResponseDTO creer(ReservationRequestDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable, id=" + dto.getClientId()));
        Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable, id=" + dto.getVehiculeId()));

        Reservation reservation = reservationMapper.toEntity(dto);
        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        reservation.setStatut(StatutReservation.CONFIRMEE);

        Reservation saved = reservationRepository.save(reservation);
        return reservationMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation introuvable, id=" + id));
        return reservationMapper.toDto(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservationMapper::toDto)
                .toList();
    }

    @Override
    public ReservationResponseDTO update(Long id, ReservationRequestDTO dto) {
        Reservation existant = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation introuvable, id=" + id));

        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client introuvable, id=" + dto.getClientId()));
            existant.setClient(client);
        }

        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable, id=" + dto.getVehiculeId()));
            existant.setVehicule(vehicule);
        }

        existant.setDateDebut(dto.getDateDebut());
        existant.setDateFin(dto.getDateFin());

        Reservation saved = reservationRepository.save(existant);
        return reservationMapper.toDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation introuvable, id=" + id);
        }
        reservationRepository.deleteById(id);
    }
}
