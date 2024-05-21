package com.fabio.estacionamento.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // Construtor privado para evitar instanciação
public class EstacionamentoUtils {
    private static final double PRIMEIROS_15_MINUTES = 5.00;
    private static final double PRIMEIROS_60_MINUTES = 9.25;
    private static final double ADICIONAL_15_MINUTES = 1.75;
    private static final double DESCONTO_PERCENTUAL = 0.30;

    //Método para calcular o custo do estacionamento
    public static BigDecimal calcularCusto(LocalDateTime entrada, LocalDateTime saida) {
        long minutes = entrada.until(saida, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {
            total = PRIMEIROS_15_MINUTES; // Se o tempo for menor ou igual a 15 minutos, o custo é de 5.00
        } else if (minutes <= 60) {
            total = PRIMEIROS_60_MINUTES; // Se o tempo for menor ou igual a 60 minutos, o custo é de 9.25
        } else {
            long addicionalMinutes = minutes - 60; // Se o tempo for maior que 60 minutos, calcula o tempo adicional
            Double totalParts = ((double) addicionalMinutes / 15); // Calcula o tempo adicional em partes de 15 minutos
            if (totalParts > totalParts.intValue()) { // 4.66 > 4
                total += PRIMEIROS_60_MINUTES + (ADICIONAL_15_MINUTES * (totalParts.intValue() + 1)); // 4 + 1
            } else { // 4.0
                total += PRIMEIROS_60_MINUTES + (ADICIONAL_15_MINUTES * totalParts.intValue()); // 4
            }
        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    // Método para calcular o desconto
    public static BigDecimal calcularDesconto(BigDecimal custo, long numeroDeVezes) {
        BigDecimal desconto = ((numeroDeVezes > 0) && (numeroDeVezes % 10 == 0)) 
                ? custo.multiply(new BigDecimal(DESCONTO_PERCENTUAL)) // Se o número de vezes for maior que 0 e for múltiplo de 10, então aplica o desconto
                : new BigDecimal(0); // Senão, não aplica desconto
        return desconto.setScale(2, RoundingMode.HALF_EVEN); // Arredonda o desconto
    }


    // Método para gerar o recibo. O número de recibo vai ficar da seguite forma: 20230316-152121
    public static String gerarRecibo() {
        LocalDateTime date = LocalDateTime.now(); // Pega a data e hora atual
        String recibo = date.toString().substring(0,19); // Pega a data e hora atual e converte para string pegando os 19 primeiros caracteres
        return recibo.replace("-", "") // encontrar o caracter "-" e substituir por ""
                .replace(":", "") // encontrar o caracter ":" e substituir por ""
                .replace("T", "-"); // encontrar o caracter "T" e substituir por "-"
    }
}
