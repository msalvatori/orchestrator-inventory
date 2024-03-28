package com.msbank.inventory.core.constants;

public class Constants {

    public static class Kafka {

        public static final String INVENTORY_DEBIT = "inventory-debit";
        public static final String INVENTORY_CREDIT = "inventory-credit";
        public static final String INVENTORY = "inventory";
        public static final String SUCCESSFULLY_CONSUMED = "successfully consumed {}={}";
    }

    public static class Iventory {
        public static final String INICIO_DA_SEPARACAO_DE_MERCADORIA = "Início da separação de mercadoria.";
        public static final String FIM_DA_SEPARACAO_DE_MERCADORIA = "Fím da separação de mercadoria.";        public static final String ESTOQUE_NAO_ENCONTRADO = "Estoque não encontrado";
        public static final String OPERACAO_R_SUCESSO = "Atualizado c/sucesso {}";
        public static final String INICIO_DA_DEVOLUCAO_DA_MERCADORIA = "Início da devolução da mercadoria.";
        public static final String FIM_DA_DEVOLUCAO_DA_MERCADORIA = "Fím da devolução da mercadoria.";
        public static final String ERROR_MESSAGE = "---done::error --creatMessage::doOnError:: caused by";
        public static final String OPERACAO_ROLLBACK = "Rollback inventory executado";
        public static final String OPERACAO_ENVIO = "Envio update inventory realizado c/sucesso";
        public static final String ESTOQUE_INSUFICIENTE = "Estoque insuficiente";
    }

}
