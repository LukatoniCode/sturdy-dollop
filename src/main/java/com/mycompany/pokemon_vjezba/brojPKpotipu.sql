SELECT t.Naziv, COUNT(p.Naziv) AS broj_pokemona
FROM pokemon p
JOIN tip t ON p.PrimarniTipId = t.TipId
GROUP BY t.Naziv
ORDER BY broj_pokemona DESC;

