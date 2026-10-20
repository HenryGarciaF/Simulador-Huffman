public class Huffman {

    // ============================================================
    //  LA FICHA (NODO): sirve para hojas y para nodos internos
    // ============================================================
    static class Nodo {
        char letra;          // solo importa en las hojas
        int frecuencia;
        Nodo izquierdo;      // "puntero" al hijo izquierdo (null = no hay)
        Nodo derecho;        // "puntero" al hijo derecho  (null = no hay)

        // Constructor de HOJA: una letra con su frecuencia
        Nodo(char letra, int frecuencia) {
            this.letra = letra;
            this.frecuencia = frecuencia;
        }

        // Constructor de nodo INTERNO: une dos nodos
        Nodo(Nodo izq, Nodo der) {
            this.frecuencia = izq.frecuencia + der.frecuencia;
            this.izquierdo = izq;
            this.derecho = der;
        }

        boolean esHoja() {
            return izquierdo == null && derecho == null;
        }
    }

    // ============================================================
    //  EL HEAP: un arreglo de nodos, con los indices de la Parte 3
    //  hijo izquierdo de i = 2i+1, hijo derecho = 2i+2, padre = (i-1)/2
    // ============================================================
    static Nodo[] heap = new Nodo[256];
    static int tamano = 0;      // cuantas fichas hay en el heap

    static void intercambiar(int i, int j) {
        Nodo temporal = heap[i];
        heap[i] = heap[j];
        heap[j] = temporal;
    }

    // METER (sift-up): O(log k)
    static void meter(Nodo nuevo) {
        heap[tamano] = nuevo;           // lo pongo en la primera casilla libre
        int i = tamano;
        tamano++;
        while (i > 0) {
            int padre = (i - 1) / 2;
            if (heap[i].frecuencia < heap[padre].frecuencia) {
                intercambiar(i, padre); // es menor que su padre: sube
                i = padre;
            } else {
                break;                  // ya esta bien ubicado
            }
        }
    }

    // SACAR EL MENOR (sift-down): O(log k)
    static Nodo sacarMenor() {
        Nodo menor = heap[0];           // el menor siempre esta en la raiz
        tamano--;
        heap[0] = heap[tamano];         // el ultimo pasa a la raiz
        heap[tamano] = null;
        int i = 0;
        while (true) {
            int izq = 2 * i + 1;
            int der = 2 * i + 2;
            int menorIndice = i;
            if (izq < tamano && heap[izq].frecuencia < heap[menorIndice].frecuencia) {
                menorIndice = izq;
            }
            if (der < tamano && heap[der].frecuencia < heap[menorIndice].frecuencia) {
                menorIndice = der;
            }
            if (menorIndice == i) {
                break;                  // ya no tiene hijos menores: se queda
            }
            intercambiar(i, menorIndice); // baja intercambiando con su hijo menor
            i = menorIndice;
        }
        return menor;
    }

    // ============================================================
    //  CONSTRUIR EL ARBOL (el corazon del algoritmo)
    // ============================================================
    static Nodo construirArbol(String texto) {
        // PASO 0: contar frecuencias (un contador por cada codigo ASCII, 0 a 255)
        int[] frecuencias = new int[256];
        char[] orden = new char[256];   // letras distintas, en orden de aparicion
        int distintas = 0;              // esto es k
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (frecuencias[c] == 0) {  // primera vez que veo esta letra
                orden[distintas] = c;
                distintas++;
            }
            frecuencias[c]++;
        }

        if (distintas == 0) {
            return null;                // caso borde: texto vacio
        }

        // PASO 1: una hoja por letra, todas al heap
        for (int j = 0; j < distintas; j++) {
            meter(new Nodo(orden[j], frecuencias[orden[j]]));
        }

        // RONDAS: mientras haya mas de una ficha
        while (tamano > 1) {
            Nodo a = sacarMenor();      // saco el menor
            Nodo b = sacarMenor();      // saco el siguiente menor
            meter(new Nodo(a, b));      // los uno y meto la union
        }

        return sacarMenor();            // la ultima ficha es la raiz
    }

    // ============================================================
    //  GENERAR LOS CODIGOS (recorre el arbol: izquierda 0, derecha 1)
    // ============================================================
    static void generarCodigos(Nodo n, String camino, String[] tabla) {
        if (n.esHoja()) {
            if (camino.equals("")) {
                camino = "0";           // caso borde: una sola letra
            }
            tabla[n.letra] = camino;
            return;
        }
        generarCodigos(n.izquierdo, camino + "0", tabla);
        generarCodigos(n.derecho, camino + "1", tabla);
    }

    // ============================================================
    //  DECODIFICAR (bajar por el arbol bit a bit)
    // ============================================================
    static String decodificar(String bits, Nodo raiz) {
        String resultado = "";
        if (raiz.esHoja()) {            // caso borde: una sola letra
            for (int i = 0; i < bits.length(); i++) {
                resultado += raiz.letra;
            }
            return resultado;
        }
        Nodo actual = raiz;
        for (int i = 0; i < bits.length(); i++) {
            if (bits.charAt(i) == '0') {
                actual = actual.izquierdo;
            } else {
                actual = actual.derecho;
            }
            if (actual.esHoja()) {      // llegue a una letra
                resultado += actual.letra;
                actual = raiz;          // vuelvo a la raiz
            }
        }
        return resultado;
    }

    // ============================================================
    //  PROGRAMA PRINCIPAL
    // ============================================================
    public static void main(String[] args) {
        String texto = "ABRACADABRA";

        Nodo raiz = construirArbol(texto);
        if (raiz == null) {
            System.out.println("Texto vacio: no hay nada que comprimir");
            return;
        }

        String[] tabla = new String[256];
        generarCodigos(raiz, "", tabla);

        String bits = "";
        for (int i = 0; i < texto.length(); i++) {
            bits += tabla[texto.charAt(i)];
        }

        for (int c = 0; c < 256; c++) {
            if (tabla[c] != null) {
                System.out.println((char) c + " = " + tabla[c]);
            }
        }
        System.out.println("Texto codificado: " + bits);
        System.out.println("Bits con Huffman: " + bits.length());
        System.out.println("Bits con ASCII:   " + texto.length() * 8);
        System.out.println("Decodificado:     " + decodificar(bits, raiz));
    }
}
