package ejercicioS7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorHeroe {

    private List<Heroe> heroes;

    public GestorHeroe() {
        heroes = new ArrayList<>();
    }

    public void agregar(Heroe h) throws Exception {
        validar(h);
        if (estaIncluido(h)) {
            throw new Exception("El héroe con ID " + h.getId() + " ya existe.");
        }
        heroes.add(h);
        Collections.sort(heroes);
    }
    public boolean estaIncluido(Heroe h) {
        int posicion = busquedaBinaria(h.getId());

        if (posicion != -1) {
            return true;
        } else {
            return false;
        }
    }
    public Heroe buscarPorId(int id) {
        int pos = busquedaBinaria(id);
        return pos != -1 ? heroes.get(pos) : null;
    }

    public int busquedaBinaria(int id) {
        int inicio = 0;
        int fin = heroes.size() - 1;
        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            int idMedio = heroes.get(medio).getId();
            if (idMedio == id) {
                return medio;
            }
            if (id > idMedio) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return -1;
    }

    public void modificar(Heroe h) throws Exception {
        validar(h);
        int pos = busquedaBinaria(h.getId());
        if (pos == -1) {
            throw new Exception("Héroe no encontrado.");
        }
        heroes.set(pos, h);
    }

    private void validar(Heroe h) throws Exception {
        if (h.getDificultad() < 1 || h.getDificultad() > 5) {
            throw new Exception(
                    "El nivel de dificultad debe estar entre 1 y 5.");
        }
        if (h.getPagoMensual() < 0) {
            throw new Exception(
                    "El pago mensual no puede ser negativo.");
        }
    }

    public double calcularAporte(Heroe h) {
        return h.getPagoMensual() * 0.10;
    }

    public double calcularImpuesto(Heroe h) {
        double pagoAnual = h.getPagoMensual() * 12;
        if (pagoAnual <= 60000) {
            return 0;
        } else if (pagoAnual <= 120000) {
            return (pagoAnual - 60000) * 0.12;
        } else if (pagoAnual <= 240000) {
            return (pagoAnual - 120000) * 0.25;
        } else {
            return (pagoAnual - 240000) * 0.35;
        }
    }

    public double calcularPagoNeto(Heroe h) {
        return h.getPagoMensual() - calcularAporte(h) - (calcularImpuesto(h) / 12);
    }

    public String generarInforme() {
        StringBuilder sb = new StringBuilder();
        for (Heroe h : heroes) {
            sb.append("Nombre: ").append(h.getNombre())
                    .append("\nSuperpoder: ").append(h.getSuperpoder())
                    .append("\nPago Mensual: ").append(String.format("%.2f", h.getPagoMensual()))
                    .append("\nAporte Fondo Heroes: ").append(String.format("%.2f", calcularAporte(h)))
                    .append("\nImpuesto: ").append(String.format("%.2f", calcularImpuesto(h)))
                    .append("\nPago Neto: ").append(String.format("%.2f", calcularPagoNeto(h)))
                    .append("\n------------------------\n");
        }
        return !sb.isEmpty() ? sb.toString() : "No hay héroes registrados";
    }

    public String imprimirHeroes() {
        StringBuilder sb = new StringBuilder();
        for (Heroe h : heroes) {
            sb.append(h.toString());
            sb.append("\n------------------------\n");
        }
        return !sb.isEmpty() ? sb.toString() : "No hay héroes registrados";
    }

    public List<Heroe> getHeroes() {
        return heroes;
    }
}

