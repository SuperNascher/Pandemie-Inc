<?php

$db = pg_connect('host=fly-by-wifi.de port=5432 dbname=pandemie2 user=flybywifi password=x0nsV&06')
    or die('Postgres mag mich nicht!' . pg_last_error());

$query = 'SELECT round.round_id, round.outcome, round.points, event.data FROM pandemie2.public.round
            JOIN pandemie2.public.event ON event.game_id = round.game_id AND event.round_id = round.round_id
            LIMIT 10;';
$result = pg_query($query) or die('Abfrage mag mich nicht!' . pg_last_error);

while ($row = pg_fetch_array($result, null, PGSQL_ASSOC)) {
    $json = 
}

pg_free_result($result);
pg_close($db);


require('index.html');
?>
