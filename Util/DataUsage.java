package Util;

import java.util.Date;
import java.io.Serializable;

public class DataUsage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int usageCount = 1;
    private Date lastUsed = new Date();

    public void updateUsage() {
        usageCount++;
        lastUsed = new Date();
    }

    public int getUsageCount() {
        return usageCount;
    }

    public Date getLastUsed() {
        return lastUsed;
    }
}
