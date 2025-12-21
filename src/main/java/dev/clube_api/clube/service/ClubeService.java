package dev.clube_api.clube.service;


import dev.clube_api.clube.dto.ClubeCreateDTO;
import dev.clube_api.clube.dto.ClubeResponseDTO;
import dev.clube_api.clube.dto.ClubeUpdateDTO;
import dev.clube_api.clube.mapper.ClubeMapper;
import dev.clube_api.clube.model.ClubeModel;
import dev.clube_api.clube.repository.ClubeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;
    private final ClubeMapper clubeMapper;

    public ClubeService(ClubeRepository clubeRepository, ClubeMapper clubeMapper) {
        this.clubeRepository = clubeRepository;
        this.clubeMapper = clubeMapper;
    }

    public ClubeResponseDTO criarClube(ClubeCreateDTO dto) {
        ClubeModel clube = clubeMapper.toEntity(dto);
        ClubeModel salvo = clubeRepository.save(clube);
        return clubeMapper.toResponseDTO(salvo);
    }

    public ClubeResponseDTO buscarPorId(Long id) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clube não encontrado"));
        return clubeMapper.toResponseDTO(clube);
    }

    public List<ClubeResponseDTO> listar() {
        return clubeRepository.findAll()
                .stream()
                .map(clubeMapper::toResponseDTO)
                .toList();
    }

    public ClubeResponseDTO atualizar(Long id, ClubeUpdateDTO dto) {
        ClubeModel clube = clubeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clube não encontrado"));

        clubeMapper.updateEntity(clube, dto);

        ClubeModel atualizado = clubeRepository.save(clube);
        return clubeMapper.toResponseDTO(atualizado);
    }


}
