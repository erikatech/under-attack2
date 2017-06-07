package br.edu.ifam.underattack.dto;

/**
 * Created by erika.silva on 06/06/2017.
 */
public class ResultadoDesafioDto {

    private int totalBugsFase;

    private int totalBugsEncontrados;

    private String desempenho;

    private double totalEstrelas;

    public void setTotalBugsFase(int totalBugsFase) {
        this.totalBugsFase = totalBugsFase;
    }

    public void setTotalBugsEncontrados(int totalBugsEncontrados) {
        this.totalBugsEncontrados = totalBugsEncontrados;
    }

    public void setDesempenho(String desempenho) {
        this.desempenho = desempenho;
    }

    public void setTotalEstrelas(double totalEstrelas) {
        this.totalEstrelas = totalEstrelas;
    }
}
