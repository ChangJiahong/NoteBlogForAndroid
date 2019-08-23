package top.pythong.noteblog.data;


/**
* @author ChangJiahong
* @date 2019/7/16
*/
public class RestResponse<T> {

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private T data;

    private Boolean ok;

    /**
     * 服务器响应时间
     */
    private long timestamp;

    /**
     * 两个参数
     * status、msg
     * @param status
     * @param msg
     */
    private RestResponse(int status, String msg){
        this(status, msg, null);
    }

    /**
     * 三个参数
     * @param status
     * @param msg
     * @param data
     */
    private RestResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.timestamp = System.currentTimeMillis() / 1000;
    }


    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }



}
