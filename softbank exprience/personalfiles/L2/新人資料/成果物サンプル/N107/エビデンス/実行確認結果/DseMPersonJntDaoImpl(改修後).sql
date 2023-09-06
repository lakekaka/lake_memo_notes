select
    m.GROUP_ID as M_GROUP_ID
    , d.CHARGE_ID as D_CHARGE_ID
    , d.CHARGE_NAME as D_CHARGE_NAME 
from
    DSE_M_PERSON d
    , DSE_M_GROUPRELATION m 
where
    m.GROUP_ID = 'cyuki' 
    and d.CHARGE_ID = '9000002L3e' 
    and m.CHARGE_ID = d.CHARGE_ID 
    AND d.provider_name = ( 
        SELECT
            TRIM(context) 
        FROM
            dse_m_conditionsetting 
        WHERE
            keyword = 'COM_PERSON_PROVIDER_NAME'
    )


SELECT
    p.provider_name AS providername
    , p.charge_id AS chargeid
    , p.charge_name AS chargename
    , p.user_first_name AS userfirstname
    , p.user_last_name AS userlastname
    , p.user_first_name_kana AS userfirstnamekana
    , p.user_last_name_kana AS userlastnamekana
    , p.authority_parameter AS autorityparameter
    , p.create_tmstmp AS createtmstmp
    , p.last_upd_tmstmp AS lastupdtmstmp
    , p.last_upd_charge_id AS lastupdchargeid
    , p.last_upd_charge_name AS lastupdchargename 
FROM
    dse_m_person p 
WHERE
    p.provider_name = 'SBTM' 
    AND p.charge_id NOT IN ( 
        SELECT
            g.charge_id 
        from
            dse_m_grouprelation g 
        where
            g.group_id = 'workgroup01'
    ) 
    AND p.DEL_FLG = '0' 
ORDER BY
    p.charge_id



SELECT
    p.CHARGE_ID AS CHARGEID
    , p.CHARGE_NAME AS CHARGENAME 
FROM
    DSE_M_PERSON p
    , DSE_M_GROUPRELATION g 
WHERE
    g.GROUP_ID = 'cyuki' 
    AND g.CHARGE_ID = p.CHARGE_ID 
    AND p.DEL_FLG = '0' 
ORDER BY
    p.CHARGE_ID 