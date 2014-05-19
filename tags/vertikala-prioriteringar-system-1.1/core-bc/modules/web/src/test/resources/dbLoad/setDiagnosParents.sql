-- Skapa upp en nivå till
insert into diagnos_kod (id, kod)
select nextval('hibernate_sequence'), 
d.kod
from (
  select distinct substring(kod from 1 for 4) kod 
  from diagnos_kod 
  where char_length(kod) > 4 
  and substring(kod from 1 for 4) not in (select kod from diagnos_kod)
) d; 

-- Knyt ihop 4-koder med 5-koder. 
update diagnos_kod cdk set parent_id = (select pdk.id from diagnos_kod pdk where char_length(pdk.kod) = 4 and pdk.kod = substring(cdk.kod from 1 for 4) )
where char_length(cdk.kod) > 4;

-- Skapa upp en nivå till
insert into diagnos_kod (id, kod)
select nextval('hibernate_sequence'), 
d.kod
from (select distinct substring(kod from 1 for 3) kod from diagnos_kod where char_length(kod) = 4) d; 

-- Knyt ihop 3-koder med 4-koder.
update diagnos_kod cdk set parent_id = (select distinct pdk.id from diagnos_kod pdk where char_length(pdk.kod) = 3 and pdk.kod = substring(cdk.kod from 1 for 3) )
where char_length(cdk.kod) = 4;

-- Skapa upp en nivå till
insert into diagnos_kod (id, kod)
select nextval('hibernate_sequence'), 
d.kod
from (select distinct substring(kod from 1 for 2) kod from diagnos_kod where char_length(kod) = 3) d; 

-- Knyt ihop 2-koder med 3-koder.
update diagnos_kod cdk set parent_id = (select distinct pdk.id from diagnos_kod pdk where char_length(pdk.kod) = 2 and pdk.kod = substring(cdk.kod from 1 for 2) )
where char_length(cdk.kod) = 3;