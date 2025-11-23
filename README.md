# VIR Képböngésző

Kötelező program az SZTE Programtervező Informatikus MSc szak Vállalati információs rendszerek tárgyához.

Készítette: Kozma Kristóf, UQ13LD

## Téma: VIR3: Egy kép böngésző asztali alkalmazás jogosultsági szintekkel

Készítsünk egy asztali Java alkalmazást, ami felhasználói bejelentkezést kér. Apache Shiro segítségével oldjuk meg a felhasználók és jogosultságok kezelését. A felhasználókat és jelszavaikat egy adatbázisban tároljuk, a jelszavak hash-elve legyenek elmentve. Alapvetően egy admin és egy sima user szerepkört hozzunk létre, de több fajta jogosultsággal. A bejelentkezett felhasználó egy előre beállított könyvtárban levő képeket tudja böngészni. Ám minden felhasználó csak azokat a kiterjesztésű képfájlokat láthatja, amelyre jogosultsága van (pl. jpg, gif, png). Az admin felhasználó minden képkiterjesztést láthat és ő meg is tudja változtatni az egyes felhasználók kép formátum jogosultságait.
