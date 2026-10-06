package br.com.fiap.ecoimpact.controller;

import br.com.fiap.ecoimpact.dto.SimulacaoRequest;
import br.com.fiap.ecoimpact.model.Simulacao;
import br.com.fiap.ecoimpact.service.SimulacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/simulacoes")
public class SimulacaoController {

    private final SimulacaoService service;

    public SimulacaoController(SimulacaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Simulacao> listarTodas() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Simulacao> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Simulacao> criar(
            @Valid @RequestBody SimulacaoRequest request
    ) {
        Simulacao simulacao = service.criar(
                request.getCategoria(),
                request.getAtividade(),
                request.getQuantidade()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(simulacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
