package com.fabio.estacionamento.service;

import com.fabio.estacionamento.entity.Cliente;
import com.fabio.estacionamento.entity.ClienteVaga;
import com.fabio.estacionamento.entity.Vaga;
import com.fabio.estacionamento.util.EstacionamentoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class EstacionamentoService {

    private final ClienteVagaService clienteVagaService;
    private final ClienteService clienteService;
    private final VagaService vagaService;

    @Transactional
    public ClienteVaga checkIn(ClienteVaga clienteVaga) {
        Cliente cliente = clienteService.buscarPorCpf(clienteVaga.getCliente().getCpf()); // Verifica se o cliente já está cadastrado
        clienteVaga.setCliente(cliente); // Seta o cliente

        Vaga vaga = vagaService.buscarPorVagaLivre(); // Busca uma vaga livre
        vaga.setStatus(Vaga.StatusVaga.OCUPADA); // Seta a vaga como ocupada
        clienteVaga.setVaga(vaga); // Seta a vaga

        clienteVaga.setDataEntrada(LocalDateTime.now()); // Seta a data de entrada

        clienteVaga.setRecibo(EstacionamentoUtils.gerarRecibo()); // Gera o recibo

        return clienteVagaService.salvar(clienteVaga); // Salva o clienteVaga
    }

    @Transactional
    public ClienteVaga checkOut(String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.buscarPorRecibo(recibo); // Busca o clienteVaga pelo recibo

        LocalDateTime dataSaida = LocalDateTime.now(); // Pega a data de saída

        BigDecimal valor = EstacionamentoUtils.calcularCusto(clienteVaga.getDataEntrada(), dataSaida); // Calcula o valor
        clienteVaga.setValor(valor); // Seta o valor

        long totalDeVezes = clienteVagaService.getTotalDeVezesEstacionamentoCompleto(clienteVaga.getCliente().getCpf()); // Pega o total de vezes que o cliente estacionou

        BigDecimal desconto = EstacionamentoUtils.calcularDesconto(valor, totalDeVezes); // Calcula o desconto
        clienteVaga.setDesconto(desconto); // Seta o desconto

        clienteVaga.setDataSaida(dataSaida); // Seta a data de saída
        clienteVaga.getVaga().setStatus(Vaga.StatusVaga.LIVRE); // Seta a vaga como livre

        return clienteVagaService.salvar(clienteVaga); // Salva o clienteVaga
    }
}

