SELECT 'HP' AS Atribut, Naziv, hp AS Vrijednost
FROM pokemon
WHERE hp = (SELECT MAX(Hp) FROM pokemon)
UNION ALL
SELECT 'Napad', Naziv, napad
FROM pokemon
WHERE napad = (SELECT MAX(napad) FROM pokemon)
UNION ALL
SELECT 'Posebni napad', Naziv, posebninapad
FROM pokemon
WHERE posebninapad = (SELECT MAX(posebninapad) FROM pokemon)
UNION ALL
SELECT 'Obrana', Naziv, obrana
FROM pokemon
WHERE obrana = (SELECT MAX(obrana) FROM pokemon)
UNION ALL
SELECT 'Posebna obrana', Naziv, posebnaobrana
FROM pokemon
WHERE posebnaobrana = (SELECT MAX(posebnaobrana) FROM pokemon)
UNION ALL
SELECT 'Brzina', Naziv, brzina
FROM pokemon
WHERE brzina = (SELECT MAX(brzina) FROM pokemon);
