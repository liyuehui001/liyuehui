package com.smec.wms.android.module;

import java.util.List;

/**
 * Created by apple on 2016/12/14.
 */

public class OtherQueryMatnrResponse {
    private String code ;
    private List<OhterQueryMatnrEntity> data ;

    public static class OhterQueryMatnrEntity {
        private String matnr ;
        private String gwgno ;
        private String remark ;
        private String gcode ;
        private String lcode ;

        public String getMatnr() {
            return matnr;
        }

        public void setMatnr(String matnr) {
            this.matnr = matnr;
        }

        public String getGwgno() {
            return gwgno;
        }

        public void setGwgno(String gwgno) {
            this.gwgno = gwgno;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getGcode() {
            return gcode;
        }

        public void setGcode(String gcode) {
            this.gcode = gcode;
        }

        public String getLcode() {
            return lcode;
        }

        public void setLcode(String lcode) {
            this.lcode = lcode;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<OhterQueryMatnrEntity> getData() {
        return data;
    }

    public void setData(List<OhterQueryMatnrEntity> queryMatnrEntityList) {
        this.data = queryMatnrEntityList;
    }
}
