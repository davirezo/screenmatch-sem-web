package br.com.alura.screenmatch.dto;

public record SerieDTO(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       String genero,
                       String atores,
                       String poster,
                       String sinopse) {
}
