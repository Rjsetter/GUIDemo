package cn.yq.data;

import com.alibaba.fastjson.annotation.JSONField;

public class IdbQuery {
    @JSONField(ordinal = 1)
    private String dbenv;   //idb环境
    @JSONField(ordinal = 2)
    private String dbname;  //idb库名
    @JSONField(ordinal = 3)
    private String tbname;  //idb表名
    @JSONField(ordinal = 4)
    private String splitcol ;
    @JSONField(ordinal = 5)
    private String splitcolmode ;
    @JSONField(ordinal = 6)
    private String sql;       //sql语句
    @JSONField(ordinal = 7)
    private String selectmod ;
    @JSONField(ordinal = 8)
    private String iscount ;


    public IdbQuery() {
        super();
    }

    public IdbQuery(String dbenv, String dbname, String tbname, String splitcol,String splitcolmode, String sql, String selectmod,String iscount) {
        super();
        this.dbenv = dbenv;
        this.dbname = dbname;
        this.tbname = tbname;
        this.splitcol = splitcol;
        this.splitcolmode = splitcolmode;
        this.sql = sql;
        this.selectmod = selectmod;
        this.iscount = iscount;
    }

    public String getDbenv() {
        return dbenv;
    }

    public void setDbenv(String dbenv) {
        this.dbenv = dbenv;
    }
    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }
    public String getIscount() {
        return iscount;
    }

    public void setIscount(String iscount) {
        this.iscount = iscount;
    }

    public String getSplitcolmode() {
        return splitcolmode;
    }

    public void setSplitcolmode(String splitcolmode) {
        this.splitcolmode = splitcolmode;
    }

    public String getSelectmod() {
        return selectmod;
    }

    public void setSelectmod(String selectmod) {
        this.selectmod = selectmod;
    }

    public String getSplitcol() {
        return splitcol;
    }

    public void setSplitcol(String splitcol) {
        this.splitcol = splitcol;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getTbname() {
        return tbname;
    }

    public void setTbname(String tbname) {
        this.tbname = tbname;
    }
}
