package com.hytera.fcls.mqtt.bean;

import java.util.List;

/**
 * Created by Tim on 17/2/28.
 */

public class FireCaseBean {

    /**
     * data : null
     * success : true
     * msg : null
     * url : null
     * list : [{"guid":"800AB379A2074746984321CA9702356E","title":"新流程图","initNum":18,"nodes":null,"lines":null,"areas":null},{"guid":"04E8AE51747F4E65A040CE07249A6976","title":"dsdsddddd","initNum":27,"nodes":null,"lines":null,"areas":null},{"guid":"D61077BBF7994D67B5F9F740A8606A7C","title":"10086网状流程","initNum":61,"nodes":null,"lines":null,"areas":null},{"guid":"5BC4EF89AC74435C896179F98D775BCF","title":"请假流程","initNum":12,"nodes":null,"lines":null,"areas":null},{"guid":"184E1ED2C11742A59E3811DC117A87EC","title":"预案系统1","initNum":46,"nodes":null,"lines":null,"areas":null},{"guid":"7BDBFF7911BC458DAB2637044F6BD1D1","title":"newFlow_1","initNum":14,"nodes":null,"lines":null,"areas":null}]
     * map : null
     */

    private Object data;
    private boolean success;
    private Object msg;
    private Object url;
    private Object map;
    private List<ListBean> list;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public Object getMap() {
        return map;
    }

    public void setMap(Object map) {
        this.map = map;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * guid : 800AB379A2074746984321CA9702356E
         * title : 新流程图
         * initNum : 18
         * nodes : null
         * lines : null
         * areas : null
         */

        private String guid;
        private String title;
        private int initNum;
        private Object nodes;
        private Object lines;
        private Object areas;

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getInitNum() {
            return initNum;
        }

        public void setInitNum(int initNum) {
            this.initNum = initNum;
        }

        public Object getNodes() {
            return nodes;
        }

        public void setNodes(Object nodes) {
            this.nodes = nodes;
        }

        public Object getLines() {
            return lines;
        }

        public void setLines(Object lines) {
            this.lines = lines;
        }

        public Object getAreas() {
            return areas;
        }

        public void setAreas(Object areas) {
            this.areas = areas;
        }
    }
}
