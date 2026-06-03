package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao,
                         @JsonAlias("Genre") String genero,
                         @JsonAlias("Actors") String atores,
                         @JsonAlias("Poster") String poster,
                         @JsonAlias("Plot") String sinopse)
{
  @Override
  public String toString() {
    return "DadosSerie: " +
            "\n titulo= '" + titulo + '\'' +
            "\n totalTemporadas= " + totalTemporadas +
            "\n avaliacao= '" + avaliacao + '\'' +
            "\n genero= '" + genero + '\'' +
            "\n atores= '" + atores + '\'' +
            "\n poster= '" + poster + '\'' +
            "\n sinopse= '" + sinopse;
  }
}
