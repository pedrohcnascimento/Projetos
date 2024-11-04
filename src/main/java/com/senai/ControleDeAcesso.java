package com.senai;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class ControleDeAcesso {

    public static String[][] matrizRegistrosDeAcesso;
    static String[][] matrizCadastro = {null};
    static String[] cabecalho = {"ID", "Nome", "Telefone", "Email"};
    static Scanner scanner = new Scanner(System.in);
    static File bancoDeDaddos = new File("C:\\Users\\Aluno\\IdeaProjects\\Projeto\\ControleDeAcesso\\src\\main\\resources\\bancoDeDados.txt");

    public static void main(String[] args) {
        carregarDadosDoArquivo();
        matrizCadastro[0] = cabecalho;
        menuDoUser();
        scanner.close();
    }
    private  static void menuDoUser(){
        int opcao;
        String menu = """
                    -------------------------------------------------
                    |           Escolha uma opção                   |
                    |       1 - Para cadastrar um usuário           |
                    |       2 - Para exibir os cadastros            |
                    |       3 - Para atualizar um usuário           |
                    |       4 - Para deletar um usuário             |
                    |       0 - Sair                                |
                    -------------------------------------------------
                    """;
        do {
            System.out.println(menu);
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarUsuario();
                    break;
                case 2:
                    exibirCadastro();
                    break;
                case 3:
                    atualizarUsuario();
                    break;
                case 4:
                    deletarUsuario();
                    break;
                case 0:
                    System.out.println("Fim do Programa!");
                    break;
                default:
                    System.out.println("Opção Inválida!");
            }
        } while (opcao != 0);
    }
    private static void cadastrarUsuario() {
        System.out.print("digite a quantidade de pessoas a serem cadastradas:");
        int qtdPessoas = scanner.nextInt();
        scanner.nextLine();
        String[][] novaMatriz = new String[matrizCadastro.length+qtdPessoas][matrizCadastro[0].length];

        for (int linhas = 0; linhas < matrizCadastro.length; linhas++) {
            novaMatriz[linhas] = Arrays.copyOf(matrizCadastro[linhas],matrizCadastro[linhas].length);
        }

        System.out.println("Preencha os dados a seguir: ");

        for (int linhas = matrizCadastro.length; linhas < novaMatriz.length; linhas++) {
            novaMatriz[linhas][0] = String.valueOf(linhas);
            System.out.println(matrizCadastro[0][0] + " - " + linhas);

            for (int colunas = 1; colunas < matrizCadastro[0].length; colunas++) {
                System.out.print(matrizCadastro[0][colunas] + ": ");
                novaMatriz[linhas][colunas] = scanner.nextLine();
            }
        }
        matrizCadastro = novaMatriz;
        salvarDadosNoArquivo();
    }
    private static void exibirCadastro() {
        StringBuilder tabela = new StringBuilder();
        for (String[] usuarioLinhas : matrizCadastro) {
            for (int colunas = 0; colunas < matrizCadastro[0].length; colunas++) {
                int larguraColuna = colunas == 0 ? 6 : (colunas == 2 ? 16 : 25);
                tabela.append(String.format("%-" + larguraColuna + "s |", usuarioLinhas[colunas]));
            }
            tabela.append("\n");
        }
        System.out.println(tabela);
    }

    private static void atualizarUsuario() {
        exibirCadastro();
        System.out.print("Informe o ID do usuário que deseja modificar: ");
        int idBusca = scanner.nextInt();

        System.out.println("\nQual informação deseja modificar?");
        System.out.println("1 - Nome");
        System.out.println("2 - Telefone");
        System.out.println("3 - Email");
        System.out.print("Escolha uma opção: ");
        int opcaoModifica = scanner.nextInt();
        scanner.nextLine();

        switch (opcaoModifica) {
            case 1:
                System.out.print("Informe o novo nome: ");
                matrizCadastro[idBusca][1] = scanner.nextLine();
                break;
            case 2:
                System.out.print("Informe o novo telefone: ");
                matrizCadastro[idBusca][2] = scanner.nextLine();
                break;
            case 3:
                System.out.print("Informe o novo email: ");
                matrizCadastro[idBusca][3] = scanner.nextLine();
                break;
            default:
                System.out.println("Opção inválida.");
        }
        System.out.println("Dados modificados com sucesso!");
        exibirCadastro();
        salvarDadosNoArquivo();
    }
    private static void deletarUsuario(){
        exibirCadastro();
        System.out.print("Digite o id do usuario que deseja deletar:");
        int idUsuario = scanner.nextInt();
        scanner.nextLine();

        String[][] novaMatriz =  new String[matrizCadastro.length-1][matrizCadastro[0].length];
        novaMatriz[0] = cabecalho;

        for (int linhasMatrizCadastro = 1, linhasNovaM = 1; linhasMatrizCadastro < matrizCadastro.length; linhasMatrizCadastro++) {
            if(linhasMatrizCadastro == idUsuario){
                continue;
            }
            novaMatriz[linhasNovaM] = matrizCadastro[linhasMatrizCadastro];
            novaMatriz[linhasNovaM][0] = String.valueOf(linhasNovaM);
            linhasNovaM++;
        }
        matrizCadastro = novaMatriz;
        System.out.println("Usuário deletado com sucesso!");
        exibirCadastro();
        salvarDadosNoArquivo();
    }

    public static void salvarDadosNoArquivo(){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(bancoDeDaddos))) {
            for( String[] linhaMatriz : matrizCadastro){
                bufferedWriter.write(String.join(",", linhaMatriz)+"\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void carregarDadosDoArquivo(){
        if(!bancoDeDaddos.exists()){
            return;
        }
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(bancoDeDaddos))) {
            String linha;
            StringBuilder conteudo = new StringBuilder();
            while ((linha = bufferedReader.readLine()) != null){
                conteudo.append(linha).append("\n");
            }
            String[]  linhaUsuario = conteudo.toString().split("\n");
            matrizCadastro = new String[linhaUsuario.length][linhaUsuario[0].split(",").length];
            for (int i = 0; i < linhaUsuario.length; i++) {
                matrizCadastro[i] = linhaUsuario[i].split(",");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
