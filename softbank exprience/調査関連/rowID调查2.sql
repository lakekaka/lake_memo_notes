select
 --     rowid
--      ,
     rownum,
     EVENT_ID
    , COMMENT_TEXT
    , DETERRENCE_01
    , DETERRENCE_02
    , DETERRENCE_03
    , DETERRENCE_04
    , DETERRENCE_05
    , DETERRENCE_06
    , DETERRENCE_07
    , ON_OFF_FLG
    , DEL_FLG 
from
 (select row_number() over () rownum,*
 from DSE_M_ONLINECONTROL ) b
     
order by
    2

