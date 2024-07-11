package com.tallerwebi.dominio;

public class ReservaDTO {
    private Reserva reserva;
    private boolean pagada;

    public ReservaDTO(Reserva reserva, boolean pagada) {
        this.reserva = reserva;
        this.pagada = pagada;
    }

    public Reserva getReserva() { return reserva; }
    public void setReserva(Reserva reserva) { this.reserva = reserva; }
    public boolean isPagada() { return pagada; }
    public void setPagada(boolean pagada) { this.pagada = pagada; }
}

