/*
 * Created on 2009/05/08
 */
package jp.co.softbankmobile.dse.integration.dao.master;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ctc.jframework.integration.persistence.PersistenceData;
import ctc.jframework.integration.persistence.binder.Binder;
import ctc.jframework.integration.persistence.spring.support.PersistenceDaoSupport;
import jp.co.softbankmobile.dse.common.ConstDefine;
import jp.co.softbankmobile.dse.integration.entity.DseMGroup;
import jp.co.softbankmobile.dse.integration.entity.DseMPersonJnt;
import jp.co.softbankmobile.dse.integration.entity.DseMPersonModelImpl;

/**
 * Copyright(C)SOFTBANK MOBILE Corp.
 * All rights reserved
 *
 * 担当者M情報DAOの実装クラス
 *
 * @author CTC
 * @version 1.00 2009/05/26
 *
 */
public class DseMPersonJntDaoImpl extends PersistenceDaoSupport implements
    IdseMPersonJntDao {

    /**
     * {@inheritDoc}
     * @see IdseMPersonJntDao.business.dao.IdseMPersonDao#findbyChargeId(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public DseMGroup findbyChargeId(DseMGroup dseMGroup) {

        /* sql文作成 */
        StringBuilder sql = new StringBuilder();
        sql.append("select m.GROUP_ID as M_GROUP_ID, d.CHARGE_ID as D_CHARGE_ID, d.CHARGE_NAME as D_CHARGE_NAME ");
        sql.append("from DSE_M_PERSON d, DSE_M_GROUPRELATION m, DSE_M_GROUP g ");
        sql.append("where m.CHARGE_ID = d.CHARGE_ID ");
        sql.append("  and m.GROUP_ID = g.GROUP_ID ");
        sql.append("  and m.GROUP_ID = ${ID} ");
        sql.append("  and d.provider_name = (select TRIM(context) FROM dse_m_conditionsetting where keyword =${KEYWORD}) ");
        sql.append("order by D_CHARGE_ID");

        /* Binderクラスに格納 */
        Binder binder = getBinder(sql.toString());
        binder.bind("ID", dseMGroup.getGroupId());
        binder.bind("KEYWORD", ConstDefine.COM_PERSON_PROVIDER_NAME);

        List<Map<String, PersistenceData>> result = query(binder);

        List<DseMPersonJnt> list = null;

        if ( result.size() > 0) {
            list = new ArrayList<DseMPersonJnt>(result.size());
            for (Map<String, PersistenceData> row : result) {
                list.add(toDomain(row));
            }
        } else {
            list = Collections.<DseMPersonJnt> emptyList();
        }

        dseMGroup.setList(list);

        return dseMGroup;
    }

    /**
     *
     * {@inheritDoc}
     * @see jp.co.softbankmobile.dse.integration.dao.master.IdseMPersonJntDao#findbyChargeIdDelFlg(jp.co.softbankmobile.dse.integration.entity.DseMGroup)
     */
    @SuppressWarnings("unchecked")
    @Override
    public DseMGroup findbyChargeIdDelFlg(DseMGroup dseMGroup) {

        /* sql文作成 */
        StringBuilder sql = new StringBuilder();
        sql.append("select m.GROUP_ID as M_GROUP_ID, d.CHARGE_ID as D_CHARGE_ID, d.CHARGE_NAME as D_CHARGE_NAME ");
        sql.append("from DSE_M_PERSON d, DSE_M_GROUPRELATION m, DSE_M_GROUP g ");
        sql.append("where m.CHARGE_ID = d.CHARGE_ID ");
        sql.append("  and m.GROUP_ID = g.GROUP_ID ");
        sql.append("  and m.GROUP_ID = ${ID} ");
        sql.append("  and d.DEL_FLG <> '1' ");
        sql.append("  and g.DEL_FLG <> '1' ");
        sql.append("  and d.provider_name = (select TRIM(context) FROM dse_m_conditionsetting where keyword =${KEYWORD}) ");
        sql.append("order by D_CHARGE_ID");

        /* Binderクラスに格納 */
        Binder binder = getBinder(sql.toString());
        binder.bind("ID", dseMGroup.getGroupId());
        binder.bind("KEYWORD", ConstDefine.COM_PERSON_PROVIDER_NAME);

        List<Map<String, PersistenceData>> result = query(binder);

        List<DseMPersonJnt> list = null;

        if ( result.size() > 0) {
            list = new ArrayList<DseMPersonJnt>(result.size());
            for (Map<String, PersistenceData> row : result) {
                list.add(toDomain(row));
            }
        } else {
            list = Collections.<DseMPersonJnt> emptyList();
        }

        dseMGroup.setList(list);

        return dseMGroup;
    }

    /**
     * 担当者M情報リスト取得(事業者名&グループ別).
     * {@inheritDoc}
     * @see jp.co.softbankmobile.dse.integration.dao.master.IdseMPersonJntDao#findbyProviderDelFlg(java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<DseMPersonModelImpl> findbyProviderDelFlg(String providerName,
        String groupId) throws ParseException {

        /* sql文作成 */

        String sql = "SELECT p.provider_name AS providername,"
            + "       p.charge_id AS chargeid,"
            + "       p.charge_name AS chargename,"
            + "       p.user_first_name AS userfirstname,"
            + "       p.user_last_name  AS userlastname,"
            + "       p.user_first_name_kana AS userfirstnamekana,"
            + "       p.user_last_name_kana AS userlastnamekana,"
            /*  2011/04/13  UPD START SBTM CC&B対応 Y.TAIRA  */
            //+ "       p.autority_parameter AS autorityparameter,"
            + "       p.authority_parameter AS autorityparameter,"
            /*  2011/04/13  UPD END SBTM CC&B対応 Y.TAIRA  */
            + "       p.create_tmstmp  AS createtmstmp,"
            + "       p.last_upd_tmstmp AS lastupdtmstmp,"
            + "       p.last_upd_charge_id AS lastupdchargeid,"
            + "       p.last_upd_charge_name AS lastupdchargename "
            + "FROM   dse_m_person p "
            + "WHERE  p.provider_name = ${PROVIDER_NAME} "
            + "  AND  p.charge_id NOT IN (SELECT g.charge_id from dse_m_grouprelation g where g.group_id = ${GROUP_ID}) "
            + "  AND  p.DEL_FLG = '0' "
            + "ORDER  BY p.charge_id";

        /* Binderクラスに格納 */
        Binder binder = getBinder(sql);
        binder.bind("PROVIDER_NAME", providerName);
        binder.bind("GROUP_ID", groupId);

        List<Map<String, PersistenceData>> result = query(binder);
        List<DseMPersonModelImpl> list = null;
        try {
            if ( result.size() > 0) {
                list = new ArrayList<DseMPersonModelImpl>(result.size());
                for (Map<String, PersistenceData> row : result) {
                    list.add(toDomainPerson(row));
                }
            } else {
                list = Collections.<DseMPersonModelImpl>emptyList();
            }
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(), e.getErrorOffset());
        }

        return list;
    }

    /**
     * 担当者M情報リスト取得(事業者名&グループ別).
     * {@inheritDoc}
     * @see jp.co.softbankmobile.dse.integration.dao.master.IdseMPersonJntDao#findbyProviderGroupDelFlg(java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<DseMPersonModelImpl> findbyProviderGroupDelFlg(String providerName,
        String groupId) throws ParseException {

        /* sql文作成 */

        String sql = "SELECT p.provider_name AS providername,"
            + "       p.charge_id AS chargeid,"
            + "       p.charge_name AS chargename,"
            + "       p.user_first_name AS userfirstname,"
            + "       p.user_last_name  AS userlastname,"
            + "       p.user_first_name_kana AS userfirstnamekana,"
            + "       p.user_last_name_kana AS userlastnamekana,"
            /*  2011/04/13  UPD START SBTM CC&B対応 Y.TAIRA  */
            //+ "       p.autority_parameter AS autorityparameter,"
            + "       p.authority_parameter AS autorityparameter,"
            /*  2011/04/13  UPD END SBTM CC&B対応 Y.TAIRA  */
            + "       p.create_tmstmp  AS createtmstmp,"
            + "       p.last_upd_tmstmp AS lastupdtmstmp,"
            + "       p.last_upd_charge_id AS lastupdchargeid,"
            + "       p.last_upd_charge_name AS lastupdchargename "
            + "FROM   dse_m_person p, dse_m_grouprelation g "
            + "WHERE  p.provider_name = ${PROVIDER_NAME} "
            + "  AND  g.group_id = ${GROUP_ID} "
            + "  AND  g.charge_id = p.charge_id "
            + "  AND  p.DEL_FLG = '0' "
            + "ORDER  BY p.charge_id";

        /* Binderクラスに格納 */
        Binder binder = getBinder(sql);
        binder.bind("PROVIDER_NAME", providerName);
        binder.bind("GROUP_ID", groupId);

        List<Map<String, PersistenceData>> result = query(binder);
        List<DseMPersonModelImpl> list = null;
        try {
            if ( result.size() > 0) {
                list = new ArrayList<DseMPersonModelImpl>(result.size());
                for (Map<String, PersistenceData> row : result) {
                    list.add(toDomainPerson(row));
                }
            } else {
                list = Collections.<DseMPersonModelImpl>emptyList();

            }
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(), e.getErrorOffset());
        }

        return list;
    }




    /*  2011/03/28  ADD START SBTM CC&B対応 H.TOMITA  */
    /**
     * 担当者M情報リスト取得(事業者名&グループ別).
     * {@inheritDoc}
     * @see jp.co.softbankmobile.dse.integration.dao.master.IdseMPersonJntDao#findbyProviderGroupDelFlg(java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<DseMPersonModelImpl> findbyProviderGroupDelFlgElse(String providerName)
    throws ParseException {

        /* sql文作成 */


        String sql = "SELECT p.provider_name AS providername,"
        + "                  p.charge_id AS chargeid,"
        + "                  p.charge_name AS chargename,"
        + "                  p.user_first_name AS userfirstname,"
        + "                  p.user_last_name  AS userlastname,"
        + "                  p.user_first_name_kana AS userfirstnamekana,"
        + "                  p.user_last_name_kana AS userlastnamekana,"
        /*  2011/04/13  UPD START SBTM CC&B対応 Y.TAIRA  */
        //+ "                  p.autority_parameter AS autorityparameter,"
        + "                  p.authority_parameter AS autorityparameter,"
        /*  2011/04/13  UPD END SBTM CC&B対応 Y.TAIRA  */
        + "                  p.create_tmstmp  AS createtmstmp,"
        + "                  p.last_upd_tmstmp AS lastupdtmstmp,"
        + "                  p.last_upd_charge_id AS lastupdchargeid,"
        + "                  p.last_upd_charge_name AS lastupdchargename "
        + "            FROM   dse_m_person p, dse_m_grouprelation g ,"
        + "                 ( SELECT group_id"
        + "                     FROM dse_m_group"
        + "                    WHERE rownum=1"
        + "                   ORDER BY GROUP_ID) h"
        + "            WHERE  p.provider_name = ${PROVIDER_NAME}"
        + "              AND  g.group_id = h.group_id"
        + "              AND  g.charge_id = p.charge_id "
        + "              AND  p.DEL_FLG = '0' "
        + "                   ORDER  BY p.charge_id";


        /* Binderクラスに格納 */
        Binder binder = getBinder(sql);
        binder.bind("PROVIDER_NAME", providerName);

        List<Map<String, PersistenceData>> result2 = query(binder);
        List<DseMPersonModelImpl> list = null;
        try {
            if ( result2.size() > 0) {
                list = new ArrayList<DseMPersonModelImpl>(result2.size());
                for (Map<String, PersistenceData> row : result2) {
                    list.add(toDomainPerson(row));
                }
            } else {
                list = Collections.<DseMPersonModelImpl>emptyList();

            }
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(), e.getErrorOffset());
        }

        return list;
    }

    /*  2011/03/28  ADD END SBTM CC&B対応 H.TOMITA  */


    /**
     * ドメイン変換.
     * @param row テーブル情報
     * @return 倒償却理由登録画面サービス
     */
    private DseMPersonJnt toDomain(Map<String, PersistenceData> row) {

        DseMPersonJnt dseMPerson = new DseMPersonJnt();
        // 2023/01/17 UPDATE START 英大文字が英小文字になります。
        /*dseMPerson.setChargeId(row.get("D_CHARGE_ID").toString());
        dseMPerson.setChargeName(row.get("D_CHARGE_NAME").toString());*/
        dseMPerson.setChargeId(row.get("d_charge_id").toString());
        dseMPerson.setChargeName(row.get("d_charge_name").toString());
        // 2023/01/17 UPDATE END 英大文字が英小文字になります。

        return dseMPerson;
    }

    /**
     * ドメイン変換.
     * @param row テーブル情報
     * @return 担当者M情報
     * @throws ParseException 解析エラー
     */
    private DseMPersonModelImpl toDomainPerson(Map<String, PersistenceData> row)
        throws ParseException {

        DseMPersonModelImpl dseMPerson = new DseMPersonModelImpl();

        SimpleDateFormat createTmp = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        SimpleDateFormat lastTmp = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
        Timestamp createTime = null;
        Timestamp lastUpdTime = null;
        try {
        	// 2023/01/17 UPDATE START 英大文字が英小文字になります。
            createTime = new Timestamp(createTmp.parse(
//                                                       row.get("CREATETMSTMP")
                                                       row.get("createtmstmp")
                                                           .toString())
                .getTime());
            lastUpdTime = new Timestamp(lastTmp.parse(
//                                                      row.get("LASTUPDTMSTMP")
                                                      row.get("lastupdtmstmp")
                                                          .toString())
                .getTime());
            // 2023/01/17 UPDATE END 英大文字が英小文字になります。
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(), e.getErrorOffset());
        }

        // 2023/01/17 UPDATE START 英大文字が英小文字になります。
        /*dseMPerson.setProviderName(row.get("PROVIDERNAME").toString());
        dseMPerson.setChargeId(row.get("CHARGEID").toString());
        dseMPerson.setChargeName(row.get("CHARGENAME").toString());
        dseMPerson.setUserFirstName(row.get("USERFIRSTNAME").toString());
        dseMPerson.setUserLastName(row.get("USERLASTNAME").toString());
        dseMPerson
            .setUserFirstNameKana(row.get("USERFIRSTNAMEKANA").toString());
        dseMPerson.setUserLastNameKana(row.get("USERLASTNAMEKANA").toString());
        dseMPerson
            .setAutorityParameter(row.get("AUTORITYPARAMETER").toString());
        dseMPerson.setCreateTmstmp(createTime);
        dseMPerson.setLastUpdTmstmp(lastUpdTime);
        dseMPerson.setLastUpdChargeId(row.get("LASTUPDCHARGEID").toString());
        dseMPerson
            .setLastUpdChargeName(row.get("LASTUPDCHARGENAME").toString());*/
        dseMPerson.setProviderName(row.get("providername").toString());
        dseMPerson.setChargeId(row.get("chargeid").toString());
        dseMPerson.setChargeName(row.get("chargename").toString());
        dseMPerson.setUserFirstName(row.get("userfirstname").toString());
        dseMPerson.setUserLastName(row.get("userlastname").toString());
        dseMPerson
            .setUserFirstNameKana(row.get("userfirstnamekana").toString());
        dseMPerson.setUserLastNameKana(row.get("userlastnamekana").toString());
        dseMPerson
            .setAutorityParameter(row.get("autorityparameter").toString());
        dseMPerson.setCreateTmstmp(createTime);
        dseMPerson.setLastUpdTmstmp(lastUpdTime);
        dseMPerson.setLastUpdChargeId(row.get("lastupdchargeid").toString());
        dseMPerson
            .setLastUpdChargeName(row.get("lastupdchargename").toString());
        // 2023/01/17 UPDATE END 英大文字が英小文字になります。

        return dseMPerson;
    }

    /**
     *
     * {@inheritDoc}
     * @see jp.co.softbankmobile.dse.integration.dao.master.IdseMPersonJntDao#findChargeName(java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public DseMPersonJnt findChargeName(String groupId, String chargeId) {

        /* sql文作成 */
        String sql = "select m.GROUP_ID as M_GROUP_ID, d.CHARGE_ID as D_CHARGE_ID, d.CHARGE_NAME as D_CHARGE_NAME "
            + "from DSE_M_PERSON d, DSE_M_GROUPRELATION m "
            + "where m.GROUP_ID =${ID} and d.CHARGE_ID = ${chargeId} and m.CHARGE_ID = d.CHARGE_ID "
            + " AND d.provider_name = (SELECT TRIM(context) FROM dse_m_conditionsetting "
            + "WHERE keyword ='COM_PERSON_PROVIDER_NAME')";

        Binder binder = getBinder(sql);
        binder.bind("ID", groupId);
        binder.bind("chargeId", chargeId);

        List<Map<String, PersistenceData>> result = query(binder);

        DseMPersonJnt dseMPersonJnt = new DseMPersonJnt();
        if ( result.size() > 0) {
            dseMPersonJnt = toDomain(result.get(0));
        }

        return dseMPersonJnt;
    }

    /* 2011/03/31 ADD START SBTM CC&B対応 M.HATA */
    /**
     * 担当者M情報リスト取得(グループ別).
     * {@inheritDoc}
     * @see jp.co.softbankmobile.dse.integration.dao.master.IdseMPersonJntDao#findbyChargeId(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List <DseMPersonJnt> findbyChargeId(String groupId) throws ParseException {

        /* sql文作成 */

        String sql = "SELECT p.CHARGE_ID AS CHARGEID,"
            + "       p.CHARGE_NAME AS CHARGENAME "
            + "FROM   DSE_M_PERSON p, DSE_M_GROUPRELATION g "
            + "WHERE  g.GROUP_ID = ${GROUP_ID} "
            + "  AND  g.CHARGE_ID = p.CHARGE_ID "
            + "  AND  p.DEL_FLG = '0' "
            + "ORDER  BY p.CHARGE_ID";

        /* Binderクラスに格納 */
        Binder binder = getBinder(sql);
        binder.bind("GROUP_ID", groupId);

        List<Map<String, PersistenceData>> result = query(binder);
        List<DseMPersonJnt> list = null;

        if ( result.size() > 0) {
            list = new ArrayList<DseMPersonJnt>(result.size());
            for (Map<String, PersistenceData> row : result) {
                DseMPersonJnt dseMPersonJnt = new DseMPersonJnt();

                // 2023/01/17 UPDATE START 英大文字が英小文字になります。
                /*dseMPersonJnt.setChargeId(row.get("CHARGEID").toString());
                dseMPersonJnt.setChargeName(row.get("CHARGENAME").toString());*/
                dseMPersonJnt.setChargeId(row.get("chargeid").toString());
                dseMPersonJnt.setChargeName(row.get("chargename").toString());
                // 2023/01/17 UPDATE END 英大文字が英小文字になります。

                list.add(dseMPersonJnt);
            }
        } else {
            list = Collections.<DseMPersonJnt>emptyList();
        }
        return list;
    }

    /**
     * {@inheritDoc}
     * @see IdseMPersonJntDao.business.dao.IdseMPersonDao#findbyChargeId(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public DseMGroup findbyGroupId(DseMGroup dseMGroup) {

        /* sql文作成 */
        StringBuilder sql = new StringBuilder();
        sql.append("select d.CHARGE_ID as D_CHARGE_ID, d.CHARGE_NAME as D_CHARGE_NAME ");
        sql.append("from DSE_M_PERSON d, DSE_M_GROUPRELATION m ");
        sql.append("where m.CHARGE_ID = d.CHARGE_ID ");
        sql.append("  and m.GROUP_ID = ${ID} ");
        sql.append("  and d.DEL_FLG = '0' ");
        sql.append("order by D_CHARGE_ID");

        /* Binderクラスに格納 */
        Binder binder = getBinder(sql.toString());
        binder.bind("ID", dseMGroup.getGroupId());

        List<Map<String, PersistenceData>> result = query(binder);

        List<DseMPersonJnt> list = null;

        if ( result.size() > 0) {
            list = new ArrayList<DseMPersonJnt>(result.size());
            for (Map<String, PersistenceData> row : result) {
                list.add(toDomain(row));
            }
        } else {
            list = Collections.<DseMPersonJnt> emptyList();
        }

        dseMGroup.setList(list);

        return dseMGroup;
    }
    /* 2011/03/31 ADD END SBTM CC&B対応 M.HATA */

}
