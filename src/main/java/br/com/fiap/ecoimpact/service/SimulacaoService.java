package br.com.fiap.ecoimpact.service;

import br.com.fiap.ecoimpact.model.Categoria;
import br.com.fiap.ecoimpact.model.Simulacao;
import br.com.fiap.ecoimpact.repository.SimulacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SimulacaoService {

    private final SimulacaoRepository repository;

    public SimulacaoService(SimulacaoRepository repository) {
        this.repository = repository;
    }

    public List<Simulacao> listarTodas() {
        return repository.findAll();
    }

    public Optional<Simulacao> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Simulacao criar(
            Categoria categoria,
            String atividade,
            Double quantidade
    ) {
        double impacto = calcularImpacto(atividade, quantidade);

        Simulacao simulacao = new Simulacao(
                categoria,
                atividade,
                quantidade,
                impacto
        );

        return repository.save(simulacao);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    private double calcularImpacto(String atividade, Double quantidade) {

        double fator = switch (atividade.toUpperCase()) {
            case "CARRO" -> 0.31;
            case "ONIBUS" -> 0.08;
            case "MOTO" -> 0.12;
            case "ENERGIA_ELETRICA" -> 0.084;
            case "BANHO" -> 0.02;
            default -> 0.10;
        };

        return quantidade * fator;
    }
}