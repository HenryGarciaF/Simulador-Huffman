# Simulador de la codificación de Huffman

Proyecto de **Estructuras de Datos 2026-II** (Grupo 9). Animación paso a paso del algoritmo voraz de codificación de Huffman, construida sobre dos estructuras: un **min-heap** (guardado en un arreglo) y un **árbol binario**.

**Simulador en vivo:** https://henrygarciaf.github.io/Simulador-Huffman/

## Contenido del repositorio

| Archivo | Descripción |
|---|---|
| `index.html` | El simulador. Es un solo archivo, sin dependencias externas: se abre en cualquier navegador. |
| `Huffman.java` | Implementación en Java del algoritmo (nodo, heap con sift-up y sift-down, construcción del árbol, códigos y decodificación). |
| `presentacion.pdf` | Presentación de la exposición (diseñada en Canva y exportada a PDF). |
| `README.md` | Este documento. |

## Qué muestra el simulador

En cada paso se ve el estado de las estructuras y **por qué** el algoritmo toma esa decisión:

- **Heap** como árbol y como arreglo con sus índices.
- **Árbol de Huffman** en construcción, con los 0 (izquierda) y 1 (derecha) en las aristas.
- **Código Java** del núcleo, con la línea activa resaltada.
- **Contadores** de comparaciones, intercambios y rondas, para relacionar la animación con el Big O.

Cuatro fases: **1)** conteo y heap, **2)** rondas (sacar los dos menores, unirlos, devolver la unión), **3)** códigos, **4)** decodificación bit a bit.

Controles: botones de fase, paso anterior y siguiente, reproducción automática, barra de pasos y flechas del teclado. El botón **Opciones** permite elegir el ejemplo (incluye casos borde), escribir un texto propio (hasta 8 letras distintas), ver cada intercambio del heap y usar un cronómetro de ensayo.

## Código Java

Requiere Java 11 o superior. Desde la carpeta del archivo:

```
java Huffman.java
```

Salida esperada para `ABRACADABRA`:

```
A = 0
B = 110
C = 1110
D = 1111
R = 10
Texto codificado: 01101001110011110110100
Bits con Huffman: 23
Bits con ASCII:   88
Decodificado:     ABRACADABRA
```

Reglas de desempate (iguales en el simulador y en el código): las hojas entran al heap en el orden de aparición de las letras; una ficha solo sube o baja si es estrictamente menor; en sift-down, si los dos hijos empatan, se elige el izquierdo; al unir, la primera ficha extraída va a la izquierda (0).

## Complejidad

Sea **n** el largo del texto y **k** la cantidad de letras distintas.

| Parte | Costo |
|---|---|
| Contar frecuencias | O(n) |
| Armar el heap | O(k log k) |
| Bucle voraz (k − 1 rondas × 3 operaciones de heap) | O(k log k) |
| Generar códigos | O(k) |
| Codificar / decodificar | O(n) / O(bits) |

| Caso | Cuándo | Tiempo | Espacio |
|---|---|---|---|
| Mejor | k = 1 o 2 | O(n) | O(k) |
| Promedio | Texto normal (k mucho menor que n) | ≈ O(n) | O(k) |
| Peor | k = n (todas las letras distintas) | O(n log n) | O(k) |

Siempre son k − 1 rondas, sin importar las frecuencias: estas solo cambian la forma del árbol. Con un alfabeto fijo (por ejemplo 256 símbolos), k log k es constante y el algoritmo completo queda en O(n).

## Casos borde

- **Una sola letra** (`AAAA`): el bucle no se ejecuta y se asigna el código `0` a mano.
- **Frecuencias iguales** (`ABCD`): árbol balanceado; no hay ganancia frente a un código fijo a la medida.
- **Frecuencias tipo Fibonacci**: árbol en escalera, de profundidad k − 1.
- **Empates**: cambian la forma del árbol, no el total de bits.
- **Texto vacío**: no hay heap ni árbol; el programa debe detectarlo.
- **Textos cortos**: guardar el árbol puede costar más de lo que se ahorra.

## Integrantes

- Daniel Arturo Barrera Berrio
- Henry Gustavo García Fuentes
- Sharon Nicolle Martínez Mesa

## Créditos

- Simulador, código de apoyo y documentación elaborados con ayuda de **Claude (Anthropic)**, herramienta de IA.
- Algoritmo: David A. Huffman, *A Method for the Construction of Minimum-Redundancy Codes* (1952).
