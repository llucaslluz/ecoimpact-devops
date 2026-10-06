package br.com.fiap.ecoimpact.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "simulacoes")
public class Simulacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private String atividade;

    private Double quantidade;

    private Double impactoKgCo2;

    private LocalDateTime dataCriacao;

    public Simulacao() {
    }

    public Simulacao(
            Categoria categoria,
            String atividade,
            Double quantidade,
            Double impactoKgCo2
    ) {
        this.categoria = categoria;
        this.atividade = atividade;
        this.quantidade = quantidade;
        this.impactoKgCo2 = impactoKgCo2;
        this.dataCriacao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Double getImpactoKgCo2() {
        return impactoKgCo2;
    }

    public void setImpactoKgCo2(Double impactoKgCo2) {
        this.impactoKgCo2 = impactoKgCo2;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}