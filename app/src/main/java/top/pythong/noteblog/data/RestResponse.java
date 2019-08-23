package top.pythong.noteblog.data;


/**
 * 返回viewModel层的数据类
 *
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

    private RestResponse(Boolean ok, int status, String msg, T data) {
        this.ok = ok;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }


    public static <T> RestResponse<T> fail(int status, String msg) {
        return new RestResponse<T>(false, status, msg, null);
    }

    public Boolean isOk() {
        return ok;
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


    @Override
    public String toString() {
        return "RestResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", ok=" + ok +
                ", timestamp=" + timestamp +
                '}';
    }
}
