# cargoservice — Sprint0

## Struttura del progetto

```
cargoservice/
├── build.gradle
├── settings.gradle
├── unibolibs/                        ← metti qui i .jar del prof
│   ├── uniboInterfaces.jar
│   ├── unibo.basicomm23-1.0.jar
│   ├── unibo.qakactor23-5.0.jar
│   └── 2p301.jar
├── src/
│   ├── main/
│   │   ├── java/cargoservice/
│   │   │   └── model/
│   │   │       └── Hold.java         ← POJO della stiva (completo)
│   │   └── resources/
│   │       └── cargoservice.qak      ← modello qak (architettura logica)
│   └── test/
│       └── java/cargoservice/
│           └── HoldTest.java         ← test T1.x sulla Hold
```

## Cosa fare subito

### 1. Scarica le jar del prof

Dal repo del corso (issLab2026), nella cartella `unibolibs/`, copia questi file
nella cartella `unibolibs/` del tuo progetto:

- `uniboInterfaces.jar`
- `unibo.basicomm23-1.0.jar`
- `unibo.qakactor23-5.0.jar`
- `2p301.jar`

### 2. Apri in Eclipse con il plugin Qak

Il file `cargoservice.qak` va aperto con Eclipse + plugin Xtext/Qak
(lo stesso ambiente usato a lezione). Il plugin genera automaticamente
il codice Kotlin degli attori da quel file.

### 3. Esegui i test della Hold

```bash
./gradlew test
```

Deve passare `HoldTest` (5 test, nessun robot né sonar necessari).

### 4. Sprint1 — cosa implementare dopo

Nei corpi degli attori nel file `.qak` ci sono commenti `// TODO Sprint1:`.
Il lavoro dello Sprint1 è riempire quei TODO:

- `cargorobot`: tradurre `moveto(DEST)` in comandi cril verso VirtualRobot26
- `sonarproxy`: connessione UDP al PicoW, parsing `distance(DIRECTION,D)`,
  logica timer 3 secondi per le soglie
- `ioporthandler`: gestione GPIO pushbutton e display fisico
