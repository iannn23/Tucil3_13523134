# Tucil3_13523134
Repository untuk memenuhi Tugas Kecil 3 mata kuliah IF2211 Strategi Algoritma Semester IV tahun 2024/2025

Penyelesaian Puzzle Rush Hour Menggunakan Algoritma Pathfinding
Uniform Cost Search (UCS), Greedy Best First Search, dan A*

Puzzle Rush Hour merupakan permainan logika grid 6x6 yang mengharuskan pemain untuk mengeluarkan mobil utama (berlabel P) dari kemacetan dengan menggeser kendaraan lain secara horizontal atau vertikal, sesuai orientasinya.

### Input
- File teks `.txt` berformat:
  - Baris pertama: ukuran papan (contoh: `6 6`)
  - Baris kedua: jumlah kendaraan
  - Baris selanjutnya: grid puzzle berisi karakter mobil (P untuk primary car), `.` untuk kosong, dan `K` untuk pintu keluar

Contoh file `.txt` input:

```bash
6 7
12
AAB..F
..BCDF
GPPCDFK
GH.III
GHJ...
LLJMM.
```
### Output

- Solusi ditampilkan sebagai animasi gerakan tiap kendaraan
- Menampilkan informasi:
  - Banyak node yang dieksplorasi
  - Waktu komputasi (ms)
  - Jumlah langkah solusi
 
 ### Tech Stack yang digunakan
 Java

Struktur Folder dan File
 ```shell
 Tucil3_13523134/
│
├── bin/                        
├── doc/                       
├── src/                          
│   ├── algorithm/               
│   │   ├── AStar.java
│   │   ├── GreedyBestFirst.java
│   │   └── UCS.java
│   │
│   ├── IO/                      
│   │   ├── InputParser.java
│   │   └── OutputFile.java
│   │
│   ├── object/                  
│   │   ├── Board.java         
│   │   ├── Car.java       
│   │   └── State.java      
│   │
│   └── Main.java       
│
├── test/                     
│   ├── solution_test1.txt   
│   └── test1.txt     
```

## Requirement dan Instalasi
[Java Development Kit (JDK) 17 atau lebih tinggi](https://adoptium.net/en-GB/temurin/releases/)

## Menjalankan Program
 ```shell
git clone
Jalankan program dengan
javac -d bin src/**/*.java
java -cp bin Main
 ```

Program ini dibuat oleh :

> 13523134 Sebastian Enrico Nathanael <br>

Tugas Kecil 3 IF2211 Strategi Algoritma
