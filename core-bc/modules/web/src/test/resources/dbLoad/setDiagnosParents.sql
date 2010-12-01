update diagnos_kod cdk set parent_id = (select pdk.id from diagnos_kod pdk where char_length(pdk.kod) = 4 and pdk.kod = substring(cdk.kod from 1 for 4) )
where char_length(cdk.kod) = 5