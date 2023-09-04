/*
 * Created on 2009/05/19
 */
package jp.co.softbankmobile.dse.integration.dao.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jp.co.softbankmobile.dse.integration.entity.DseDConfirmmessage;
import jp.co.softbankmobile.dse.integration.entity.DsePagerInfo;
import ctc.jframework.common.exception.ApplicationException;
import ctc.jframework.integration.persistence.PersistenceData;
import ctc.jframework.integration.persistence.binder.Binder;
import ctc.jframework.integration.persistence.pagehandle.PageHandler;
import ctc.jframework.integration.persistence.spring.support.PersistenceDaoSupport;

/**
 * Copyright(C)SOFTBANK MOBILE Corp.
 * All rights reserved
 *
 * 確認メッセージD情報取得DAOの実装クラス
 *
 * @author CTC
 * @version 1.00 09/05/19
 *
 */
public class DseDConfirmmessageDaoImpl extends PersistenceDaoSupport implements
    IdseDConfirmmessageDao {

    /**
     * 発生年月時分秒終了位置
     */
    private static final int OUT_BREAK_TIME_END = 17;

    /**
     * 確認メッセージレコードを確認メッセージドメインに変換します.
     *
     * @param row カテゴリレコード
     * @return 仮貸倒償却情報ドメイン
     */
    private static DseDConfirmmessage toDomain(Map<String, PersistenceData> row) {

        DseDConfirmmessage dseMSGConfirm = new DseDConfirmmessage();

        dseMSGConfirm.setBillGroupId(row.get("BILL_GROUP_ID").toString());
        dseMSGConfirm.setOutbreakTime(row.get("OUTBREAK_TM").toString());
        dseMSGConfirm.setMssgId(row.get("MSSG_ID").toString());
        dseMSGConfirm.setMessageText(row.get("MESSAGE_TEXT").toString());
        dseMSGConfirm.setDelFlg(row.get("DEL_FLG").toString());

        return dseMSGConfirm;
    }

    /**
     * {@inheritDoc}
     * @see jp.co.softbankmobile.dse.integration.dao.data.IdseDConfirmmessageDao#findDseMSGConfirmList(java.lang.String, jp.co.softbankmobile.dse.integration.entity.DsePagerInfo)
     */
    @SuppressWarnings("unchecked")
    public List<DseDConfirmmessage> findDseMSGConfirmList(String userId,
        DsePagerInfo dsePagerInfo) {

        String sql = "SELECT  c.bill_group_id,"
            + "        TO_CHAR(c.outbreak_tmstmp,'yyyymmddhh24missff3') AS outbreak_tm, "
            + "        c.mssg_id,"
            + "        c.del_flg,"
            + "        m.message_text "
            + "FROM dse_m_grouprelation g, dse_d_billgroupperson b, "
            + "     dse_d_confirmmessage c, dse_m_message m "
            + "WHERE g.charge_id = ${CHARGE_ID} "
            + "  AND g.group_id = b.group_id "
            + "  AND b.bill_group_id = c.bill_group_id "
            + "  AND c.mssg_id = m.business_id || m.message_type || m.message_level || m.process_no || m.seq_no "
            + "ORDER BY outbreak_tm DESC, c.bill_group_id ASC, c.mssg_id ASC ";

        Binder binder = getBinder(sql);
        binder.bind("CHARGE_ID", userId);

        PageHandler ph = query(binder, dsePagerInfo.getCurrentPage(),
                               dsePagerInfo.getPerPageQuantity());
        dsePagerInfo.setTotalItemCnt(ph.getTotalCount());
        dsePagerInfo.setTotalPage(ph.getTotalPage());

        ph.setCurrentPage(dsePagerInfo.getCurrentPage());
        List<Map<String, PersistenceData>> rows = (List<Map<String, PersistenceData>>)ph
            .getResultList();
        List<DseDConfirmmessage> dseMSGConfirmList = null;
        if ( rows.size() > 0) {
            dseMSGConfirmList = new ArrayList<DseDConfirmmessage>(rows.size());
            for (Map<String, PersistenceData> row : rows) {
                dseMSGConfirmList.add(toDomain(row));
            }
        } else {
            dseMSGConfirmList = Collections.<DseDConfirmmessage> emptyList();
        }

        return dseMSGConfirmList;
    }

    /**
     * {@inheritDoc}
     * @see jp.co.softbankmobile.dse.integration.dao.data.IdseDConfirmmessageDao#findConfirmFlag(java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public String findConfirmFlag(String billGroupId, String outbreakTime)
        throws ApplicationException {

        String sql = "SELECT  del_flg "
            + "FROM dse_d_confirmmessage "
            + "WHERE bill_group_id = ${BILL_GROUP_ID}"
            + "  AND  TO_CHAR(outbreak_tmstmp,'yyyymmddhh24missff3') = ${OUTBREAK_TMSTMP} ";

        Binder binder = getBinder(sql);
        binder.bind("BILL_GROUP_ID", billGroupId);
        binder.bind("OUTBREAK_TMSTMP", outbreakTime);
        List<Map<String, PersistenceData>> rows = query(binder);

        String confirmFlag = "";

        if ( rows.size() > 0) {
            confirmFlag = rows.get(0).get("DEL_FLG").toString();
        } else {
            throw new ApplicationException("該当メッセージが存在しません.");
        }

        return confirmFlag;
    }

    /**
     * {@inheritDoc}
     * @see jp.co.softbankmobile.dse.integration.dao.data.IdseDConfirmmessageDao#updateConfirmFlag(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public int updateConfirmFlag(String billGroupId, String outbreakTime,
        String userId, String userName) {

        String sql = "UPDATE dse_d_confirmmessage "
            + "SET    del_flg = ${DEL_FLG}, "
            + "       last_upd_tmstmp = GYOMUTIMESTAMP, "
            + "       last_upd_charge_id = ${LAST_UPD_CHARGE_ID}, "
            + "       last_upd_charge_name = ${LAST_UPD_CHARGE_NAME} "
            + "WHERE  bill_group_id = ${BILL_GROUP_ID}"
            + "  AND  TO_CHAR(outbreak_tmstmp,'yyyymmddhh24missff3') = ${OUTBREAK_TMSTMP} ";

        Binder binder = getBinder(sql);
        binder.bind("DEL_FLG", 1);
        binder.bind("LAST_UPD_CHARGE_ID", userId);
        binder.bind("LAST_UPD_CHARGE_NAME", userName);
        binder.bind("BILL_GROUP_ID", billGroupId);
        binder.bind("OUTBREAK_TMSTMP", outbreakTime);

        return update(binder);
    }

    /**
     * {@inheritDoc}
     * @see jp.co.softbankmobile.dse.integration.dao.data.IdseDConfirmmessageDao#updateConfirmFlag(java.lang.String[], java.lang.String, java.lang.String)
     */
    public int updateConfirmFlag(String[] outbreakTimeBillGroupIds,
        String userId, String userName) throws ApplicationException {

        int ret = 0;
        for (int i = 0; i < outbreakTimeBillGroupIds.length; i++) {
            /* 発生年月時分秒  yyyyMMddHHmmss*/
            String outbreakTime = outbreakTimeBillGroupIds[i]
                .substring(0, OUT_BREAK_TIME_END);
            /* 請求グループID */
            String billGroupId = outbreakTimeBillGroupIds[i]
                .substring(OUT_BREAK_TIME_END);

            /* 確認フラグ取得 */
            String confirmFlag = findConfirmFlag(billGroupId, outbreakTime);
            /* 確認済判定 */
            if ( "0".equals(confirmFlag)) {
                updateConfirmFlag(billGroupId, outbreakTime, userId, userName);
                ret++;
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     * @see jp.co.softbankmobile.dse.integration.dao.data.IdseDConfirmmessageDao#findFileoutputData(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, PersistenceData>> findFileoutputData(String userId) {

        String sql = "SELECT c.bill_group_id "
            + "FROM dse_m_grouprelation g, dse_d_billgroupperson b, "
            + "     dse_d_confirmmessage c "
            + "WHERE g.charge_id     = ${CHARGE_ID} "
            + "  AND g.group_id      = b.group_id "
            + "  AND b.bill_group_id = c.bill_group_id "
            + "  AND c.del_flg       = '0' " + "ORDER BY c.bill_group_id ";

        Binder binder = getBinder(sql);
        binder.bind("CHARGE_ID", userId);

        List<Map<String, PersistenceData>> fileoutputData = (List<Map<String, PersistenceData>>)query(binder);
        if ( fileoutputData.size() == 0) {
            fileoutputData = Collections
                .<Map<String, PersistenceData>> emptyList();
        }

        return fileoutputData;
    }

}
