SELECT b.Naziv, pt.Naziv AS PrimarniTip, st.Naziv AS SekundarniTip, b.Hp, b.Napad, b.PosebniNapad, b.Obrana, b.PosebnaObrana, b.Brzina 
FROM pokemon.pokemon AS b
LEFT JOIN pokemon.tip AS pt ON b.PrimarniTipId = pt.TipId
LEFT JOIN pokemon.tip AS st ON b.SekundarniTipId = st.TipId;