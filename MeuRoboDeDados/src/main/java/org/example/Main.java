package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.json.JSONObject;

public class Main {
    private static double ultimoPreco = 0.0;
    private static final String NOME_ARQUIVO = "historico_dolar.txt";

    public static void main(String[] args) {
        // --- RECUPERANDO A MEMÓRIA AO INICIAR ---
        ultimoPreco = carregarUltimoPrecoDoArquivo();

        JFrame janela = new JFrame("Camara - Monitor de Câmbio");
        janela.setSize(450, 400);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLayout(new BorderLayout());
        janela.setLocationRelativeTo(null);

        JPanel painelTopo = new JPanel();
        JButton botao = new JButton("💰 Consultar Dólar Agora");
        painelTopo.add(new JLabel("Monitor de Tendência: "));
        painelTopo.add(botao);

        JEditorPane areaResultado = new JEditorPane();
        areaResultado.setContentType("text/html");
        areaResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaResultado);

        StringBuilder historicoHTML = new StringBuilder("<html><body>");
        if(ultimoPreco > 0) {
            historicoHTML.append("<div style='color: gray; font-family: sans-serif;'><i>Memória carregada. Último valor: R$ " + ultimoPreco + "</i></div><hr>");
        }

        botao.addActionListener(e -> {
            new Thread(() -> {
                try {
                    URL url = new URL("https://economia.awesomeapi.com.br/last/USD-BRL");
                    HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                    JSONObject json = new JSONObject(new BufferedReader(new InputStreamReader(conexao.getInputStream())).lines().reduce("", String::concat));

                    double precoAtual = json.getJSONObject("USDBRL").getDouble("bid");
                    String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                    String cor = "black";
                    String seta = "▬";

                    if (ultimoPreco > 0) {
                        if (precoAtual > ultimoPreco) {
                            cor = "green"; seta = "▲";
                        } else if (precoAtual < ultimoPreco) {
                            cor = "red"; seta = "▼";
                        }
                    }

                    historicoHTML.append("<div style='color: ").append(cor).append("; font-family: sans-serif;'>")
                            .append("<b>[").append(data).append("]</b> ")
                            .append(seta).append(" Dólar: R$ ").append(String.format("%.4f", precoAtual))
                            .append("</div>");

                    areaResultado.setText(historicoHTML.toString() + "</body></html>");

                    // Salva no arquivo
                    FileWriter arq = new FileWriter(NOME_ARQUIVO, true);
                    PrintWriter gravarArq = new PrintWriter(arq);
                    gravarArq.println(data + " - R$ " + precoAtual);
                    arq.close();

                    ultimoPreco = precoAtual;

                } catch (Exception ex) {
                    areaResultado.setText(areaResultado.getText() + "<br><span style='color:red;'>Erro na conexão!</span>");
                }
            }).start();
        });

        janela.add(painelTopo, BorderLayout.NORTH);
        janela.add(scroll, BorderLayout.CENTER);
        janela.setVisible(true);
    }

    // FUNÇÃO PARA LER O ARQUIVO TXT
    private static double carregarUltimoPrecoDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return 0.0;

        String ultimaLinha = "";
        try (Scanner scanner = new Scanner(arquivo)) {
            while (scanner.hasNextLine()) {
                ultimaLinha = scanner.nextLine();
            }
            if (!ultimaLinha.isEmpty()) {
                // Pega o valor depois do "R$ "
                String valorTexto = ultimaLinha.split("R\\$ ")[1].trim();
                return Double.parseDouble(valorTexto);
            }
        } catch (Exception e) {
            System.err.println("Ainda não há histórico para carregar.");
        }
        return 0.0;
    }
}