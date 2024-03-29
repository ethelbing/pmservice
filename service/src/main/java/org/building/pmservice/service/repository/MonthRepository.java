package org.building.pmservice.service.repository;

import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.building.pmservice.service.utils.ProcedureUtils.ResultHash;

/**
 * Created by hrb on 2018/5/15.
 */
@Repository
public class MonthRepository {

    private JdbcTemplate template;

    @Autowired
    public void setTest1DataSource(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    private List<HashMap> ResultHash(ResultSet rs) throws SQLException {

        List<HashMap> result = new ArrayList<HashMap>();

        ResultSetMetaData rsm = rs.getMetaData();

        int colNum = 0;

        colNum = rsm.getColumnCount();

        while (rs.next()) {
            HashMap model = new HashMap();
            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                if (rsm.getColumnType(i) == 91) {
                    model.put(rsm.getColumnName(i),
                            rs.getString(i) == null ? "" : rs.getString(i)
                                    .split("\\.")[0]);
                } else {
                    if (rsm.getColumnType(i) == 2) {
                        if (rs.getString(i) == null) {
                            model.put(rsm.getColumnName(i), "");
                        } else {
                            model.put(rsm.getColumnName(i), rs.getDouble(i));
                        }
                    } else {
                        model.put(rsm.getColumnName(i),
                                rs.getString(i) == null ? "" : rs.getString(i)
                                        .toString().replaceAll("\\n", ""));
                    }
                }
            }
            result.add(model);
        }
        rs.close();

        return result;
    }

    //month download
    public Map OutMonthData(String V_V_YEAR, String V_V_MONTH, String V_ORG_CODE, String V_DEPT_CODE) {
        return template.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con)
                    throws SQLException {
                String sql = "{call PRO_MONTH_DATA_DOWNLOAD(:V_V_YEAR,:V_V_MONTH,:V_ORG_CODE,:V_DEPT_CODE,:INFO,:RET)}";

                CallableStatement statement = con.prepareCall(sql);

                statement.setString("V_V_YEAR", V_V_YEAR);
                statement.setString("V_V_MONTH", V_V_MONTH);
                statement.setString("V_ORG_CODE", V_ORG_CODE);
                statement.setString("V_DEPT_CODE", V_DEPT_CODE);

                statement.registerOutParameter("INFO", OracleTypes.VARCHAR);
                statement.registerOutParameter("RET", OracleTypes.CURSOR);
                return statement;
            }
        }, new CallableStatementCallback<Map>() {
            @Override
            public Map doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                Map result = new HashMap();
                result.put("list", ResultHash((ResultSet) callableStatement.getObject("RET")));
                result.put("RET", (String) callableStatement.getString("INFO"));
                return result;
            }
        });
    }

    public Map OutWeekGuidByMonth(String MonthGuid) {
        return template.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con)
                    throws SQLException {
                String sql = "{call PRO_WEEKDATA_BY_MONTH(:V_V_MONTHGUID,:V_CURSOR)}";

                CallableStatement statement = con.prepareCall(sql);
                statement.setString("V_V_MONTHGUID", MonthGuid);
                statement.registerOutParameter("V_CURSOR", OracleTypes.CURSOR);
                return statement;
            }
        }, new CallableStatementCallback<Map>() {
            @Override
            public Map doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                Map result = new HashMap();
                result.put("list", ResultHash((ResultSet) callableStatement.getObject("V_CURSOR")));
                return result;
            }
        });
    }

    public Map OutDefectGuidByMonth(String MonthGuid){
        return template.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con)
                    throws SQLException {
                String sql = "{call PRO_DEFECTDATA_BY_MONTH(:V_V_MONTHGUID,:V_CURSOR)}";

                CallableStatement statement = con.prepareCall(sql);
                statement.setString("V_V_MONTHGUID", MonthGuid);
                statement.registerOutParameter("V_CURSOR", OracleTypes.CURSOR);
                return statement;
            }
        }, new CallableStatementCallback<Map>() {
            @Override
            public Map doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                Map result = new HashMap();
                result.put("list", ResultHash((ResultSet) callableStatement.getObject("V_CURSOR")));
                return result;
            }
        });
    }

    public Map OutWeekData(String V_V_YEAR, String V_V_MONTH, String V_V_WEEK, String V_V_ORG_CODE, String V_V_DEPT_CODE) {
        return template.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con)
                    throws SQLException {
                String sql = "{call PRO_WEEK_TO_DD_DATA(:V_V_YEAR,:V_V_MONTH,:V_V_WEEK,:V_V_ORG_CODE,:V_V_DEPT_CODE,:V_INFO,:V_CURSOR)}";

                CallableStatement statement = con.prepareCall(sql);
                statement.setString("V_V_YEAR", V_V_YEAR);
                statement.setString("V_V_MONTH", V_V_MONTH);
                statement.setString("V_V_WEEK", V_V_WEEK);
                statement.setString("V_V_ORG_CODE", V_V_ORG_CODE);
                statement.setString("V_V_DEPT_CODE", V_V_DEPT_CODE);

                statement.registerOutParameter("V_INFO", OracleTypes.VARCHAR);
                statement.registerOutParameter("V_CURSOR", OracleTypes.CURSOR);
                return statement;
            }
        }, new CallableStatementCallback<Map>() {
            @Override
            public Map doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                Map result = new HashMap();
                result.put("list", ResultHash((ResultSet) callableStatement.getObject("V_CURSOR")));
                result.put("V_INFO", (String) callableStatement.getString("V_INFO"));
                return result;
            }
        });
    }

    public Map OutWorkGuidByWeek(String WeekGuid){
        return template.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con)
                    throws SQLException {
                String sql = "{call PRO_WORKDATA_BY_WEEK(:V_V_WEEKGUID,:V_CURSOR)}";

                CallableStatement statement = con.prepareCall(sql);
                statement.setString("V_V_WEEKGUID", WeekGuid);
                statement.registerOutParameter("V_CURSOR", OracleTypes.CURSOR);
                return statement;
            }
        }, new CallableStatementCallback<Map>() {
            @Override
            public Map doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                Map result = new HashMap();
                result.put("list", ResultHash((ResultSet) callableStatement.getObject("V_CURSOR")));
                return result;
            }
        });
    }

    public Map OutDefGuidByWeek(String WeekGuid){
        return template.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con)
                    throws SQLException {
                String sql = "{call PM_DEFGUID_BY_WEEK(:V_V_WEEKGUID,:V_CURSOR)}";

                CallableStatement statement = con.prepareCall(sql);
                statement.setString("V_V_WEEKGUID", WeekGuid);
                statement.registerOutParameter("V_CURSOR", OracleTypes.CURSOR);
                return statement;
            }
        }, new CallableStatementCallback<Map>() {
            @Override
            public Map doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                Map result = new HashMap();
                result.put("list", ResultHash((ResultSet) callableStatement.getObject("V_CURSOR")));
                return result;
            }
        });
    }

    public Map OutWorkOrderData(String v_v_year, String v_v_month, String v_v_orgcode, String v_v_deptcode) {
        return template.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con)
                    throws SQLException {
                String sql = "{call PRO_WORKORDER_TO_DD_DATA(:V_V_YEAR,:V_V_MONTH,:V_V_ORG_CODE,:V_V_DEPT_CODE,:V_INFO,:V_CURSOR)}";

                CallableStatement statement = con.prepareCall(sql);
                statement.setString("V_V_YEAR", v_v_year);
                statement.setString("V_V_MONTH", v_v_month);
                statement.setString("V_V_ORG_CODE", v_v_orgcode);
                statement.setString("V_V_DEPT_CODE", v_v_deptcode);

                statement.registerOutParameter("V_INFO", OracleTypes.VARCHAR);
                statement.registerOutParameter("V_CURSOR", OracleTypes.CURSOR);
                return statement;
            }
        }, new CallableStatementCallback<Map>() {
            @Override
            public Map doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                Map result = new HashMap();
                result.put("list", ResultHash((ResultSet) callableStatement.getObject("V_CURSOR")));
                result.put("V_INFO", (String) callableStatement.getString("V_INFO"));
                return result;
            }
        });
    }

    public Map OutDefGuidByWork(String WorkGuid){
        return template.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con)
                    throws SQLException {
                String sql = "{call PM_DEFGUID_BY_WORK(:V_V_WORKGUID,:V_CURSOR)}";

                CallableStatement statement = con.prepareCall(sql);
                statement.setString("V_V_WORKGUID", WorkGuid);
                statement.registerOutParameter("V_CURSOR", OracleTypes.CURSOR);
                return statement;
            }
        }, new CallableStatementCallback<Map>() {
            @Override
            public Map doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                Map result = new HashMap();
                result.put("list", ResultHash((ResultSet) callableStatement.getObject("V_CURSOR")));
                return result;
            }
        });
    }

    public Map OutWorkOrderMMData(String v_v_orderguid) {
        return template.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con)
                    throws SQLException {
                String sql = "{call PRO_PM_WORKORDER_SPARE_VIEW(:V_V_ORDERGUID,:V_CURSOR)}";

                CallableStatement statement = con.prepareCall(sql);
                statement.setString("V_V_ORDERGUID", v_v_orderguid);
                statement.registerOutParameter("V_CURSOR", OracleTypes.CURSOR);
                return statement;
            }
        }, new CallableStatementCallback<Map>() {
            @Override
            public Map doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                Map result = new HashMap();
                result.put("list", ResultHash((ResultSet) callableStatement.getObject("V_CURSOR")));
                return result;
            }
        });
    }

    public Map OutDefectData(String v_v_year, String v_v_month, String v_v_orgcode, String v_v_deptcode) {
        return template.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con)
                    throws SQLException {
                String sql = "{call PRO_DEFECT_TO_DD_DATA(:V_V_YEAR,:V_V_MONTH,:V_V_ORGCODE,:V_V_DEPTCODE,:V_CURSOR)}";

                CallableStatement statement = con.prepareCall(sql);
                statement.setString("V_V_YEAR", v_v_year);
                statement.setString("V_V_MONTH", v_v_month);
                statement.setString("V_V_ORGCODE", v_v_orgcode);
                statement.setString("V_V_DEPTCODE", v_v_deptcode);
                statement.registerOutParameter("V_CURSOR", OracleTypes.CURSOR);
                return statement;
            }
        }, new CallableStatementCallback<Map>() {
            @Override
            public Map doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                Map result = new HashMap();
                result.put("list", ResultHash((ResultSet) callableStatement.getObject("V_CURSOR")));
                return result;
            }
        });
    }

    //设备可调拨接口状态查询
    public Map OutEquCodeData(String V_EQU_CODE,String V_STR1,String V_STR2,
                              String V_STR3,String V_STR4,String V_STR5) {
        return template.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con)
                    throws SQLException {
                String sql = "{call PRO_EQU_SEL_WORKADEF(:V_EQU_CODE,:V_STR1,:V_STR2,:V_STR3,:V_STR4,:V_STR5,:V_INFO,:RET)}";

                CallableStatement statement = con.prepareCall(sql);
                statement.setString("V_EQU_CODE", V_EQU_CODE);
                statement.setString("V_STR1", V_STR1);
                statement.setString("V_STR2", V_STR2);
                statement.setString("V_STR3", V_STR3);
                statement.setString("V_STR4", V_STR4);
                statement.setString("V_STR5", V_STR5);

                statement.registerOutParameter("V_INFO", OracleTypes.VARCHAR);
                statement.registerOutParameter("RET", OracleTypes.CURSOR);
                return statement;
            }
        }, new CallableStatementCallback<Map>() {
            @Override
            public Map doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                Map result = new HashMap();
                result.put("list", ResultHash((ResultSet) callableStatement.getObject("RET")));
                result.put("V_INFO", (String) callableStatement.getString("V_INFO"));
                return result;
            }
        });
    }
    //设备可调拨接口状态查询-子工单查询
    public Map OutEquToWorkId(String V_EQU_CODE) {
        return template.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con)
                    throws SQLException {
                String sql = "{call PRO_EQU_SELECT_WORKID(:V_EQU_CODE,:RET)}";

                CallableStatement statement = con.prepareCall(sql);
                statement.setString("V_EQU_CODE", V_EQU_CODE);

                statement.registerOutParameter("RET", OracleTypes.CURSOR);
                return statement;
            }
        }, new CallableStatementCallback<Map>() {
            @Override
            public Map doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
                callableStatement.execute();
                Map result = new HashMap();
                result.put("list", ResultHash((ResultSet) callableStatement.getObject("RET")));

                return result;
            }
        });
    }
}
