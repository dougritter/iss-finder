package br.com.douglasritter.issfinder;

import com.google.gson.annotations.SerializedName;

/**
 * Created by douglasritter on 4/3/15.
 */
public class ISSNow {

    /*
    {
          "iss_position": {
            "latitude": -36.14503323862732,
            "longitude": -84.19204676640621
          },
          "message": "success",
          "timestamp": 1428094152
        }
     */

    @SerializedName("iss_position")
    private ISSPosition issPosition;

    private String message;
    private long timestamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ISSPosition getIssPosition() {
        return issPosition;
    }

    public void setIssPosition(ISSPosition issPosition) {
        this.issPosition = issPosition;
    }

    @Override
    public String toString() {
        return "ISSNow{" +
                "issPosition=" + issPosition +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
