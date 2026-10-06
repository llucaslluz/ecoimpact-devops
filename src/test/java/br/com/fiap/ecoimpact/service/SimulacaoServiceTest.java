package br.com.fiap.ecoimpact.service;

import br.com.fiap.ecoimpact.model.Categoria;
import br.com.fiap.ecoimpact.model.Simulacao;
import br.com.fiap.ecoimpact.repository.SimulacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimulacaoServiceTest {

    @Mock
    private SimulacaoRepository repository;

    @InjectMocks
    private SimulacaoService service;

    @Test
    void deveCalcularImpactoDeCarro() {

        when(repository.save(any(Simulacao.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Simulacao resultado = service.criar(
                Categoria.TRANSPORTE,
                "CARRO",
                50.0
        );

        assertEquals(15.5, resultado.getImpactoKgCo2(), 0.001);
    }

    @Test
    void deveCalcularImpactoDeOnibus() {

        when(repository.save(any(Simulacao.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Simulacao resultado = service.criar(
                Categoria.TRANSPORTE,
                "ONIBUS",
                100.0
        );

        assertEquals(8.0, resultado.getImpactoKgCo2(), 0.001);
    }

    @Test
    void deveCalcularImpactoDeEnergiaEletrica() {

        when(repository.save(any(Simulacao.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Simulacao resultado = service.criar(
                Categoria.ENERGIA,
                "ENERGIA_ELETRICA",
                100.0
        );

        assertEquals(8.4, resultado.getImpactoKgCo2(), 0.001);
    }
}