package com.fabio.estacionamento.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// Todos esses campos são referentes ao retorno do Page, que é um objeto que contém várias informações sobre a paginação de um objeto
@Data
public class PageableDto {
    private List content = new ArrayList<>(); // Isso porque no page tudo vem dentro de content, ex: { "content": [ {"id": 6,"nome": "Fábio Oliveira '"...restante do objeto}]}
    private boolean first;
    private boolean last;
    @JsonProperty("page") // trocando o nome da propriedade para page
    private int number;
    private int size;
    @JsonProperty("pageElements") // trocando o nome da propriedade para pageElements
    private int numberOfElements;
    private int totalPages;
    private int totalElements;

}

